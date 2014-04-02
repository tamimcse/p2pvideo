package leecher;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageManager;
import messages.MessageType;
import org.apache.commons.io.FileUtils;
import seeder.MessageSender;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import video.VLCMediaPlayer;

/**
 *
 * @author Tamim
 */
public class MessageReceiver extends Thread
{

    static Object obj = new Object();
    static AtomicInteger count = new AtomicInteger(0);
    static int nextFile = 1;
    Socket clientSocket;
    ArrayList<String> chunks = new ArrayList<String>();
    static TreeMap<String, String> sortMap = new TreeMap<String, String>();
    static MediaListPlayer mediaListPlayer = null;

    public MessageReceiver(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
//                try
//                {
//                    Thread.sleep(30000);
//                }
//                catch (InterruptedException ex)
//                {
//                    Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
//                }
        
        try
        {
            byte[] bytes = receiveByte(clientSocket);
            System.out.println("size of message"+bytes.length+" Receiving message: "+new String(bytes));
            
            MessageType type = MessageManager.getType(bytes);

            if (type == MessageType.HELLO && Config.IS_SEEDER)
            {
                String[] s = new String(bytes).split(",");
                Config.LEECHER_IP = s[1];
                Config.LEECHER_PORT = Integer.parseInt(s[2]);
                MessageSender seeder = new MessageSender();
                seeder.send();
            }
            else if (type == MessageType.HANDSHAKE)
            {
                Config.NUM_OF_FILES = Integer.parseInt(new String(bytes).split(",")[3]);
            }
            else
            {
                byte[] fileData = new byte[bytes.length - 8];
                int id = bytes[3] | (bytes[2] << 1) | (bytes[1] << 2) | (bytes[0] << 3);
                System.arraycopy(bytes, 8, fileData, 0, fileData.length);
                String filePath = String.format("%s/%s_%d.%s", Config.localDir, Config.fileName, id, Config.FILE_EXTENSION);
                Files.write(Paths.get(filePath), fileData, StandardOpenOption.CREATE);
                if (Config.NUM_OF_FILES == count.incrementAndGet())
                {
                    FileUtils.write(new File("temp.txt"), Config.NUM_OF_FILES + "");
                    Config.IS_SEEDER = true;
                }

                //Cludge!!! VLC does not take / style path on windows
                String filePath1 = filePath.replace("/", "\\");

                synchronized (obj)
                {
                    while (id != nextFile)
                    {
                        obj.wait();
                    }
                    System.out.println("Adding Path " + filePath1);
                    VLCMediaPlayer.INSTANCE.play(filePath1);
                    nextFile++;
                    obj.notifyAll();
                }
                
                if(Config.IS_SEEDER)
                {
                    FileUtils.write(new File("result.txt"), VLCMediaPlayer.getNumStall()+"");
                }
            }
            clientSocket.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    String receive(Socket clientSocket)
    {
        try
        {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            StringBuffer response = new StringBuffer();
            int result;
            while ((result = input.read()) != -1)
            {
                response.append(Character.toChars(result));
//                System.out.println(result);
            }
            input.close();
            return response.toString();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return null;
    }

    byte[] receiveByte(Socket clientSocket)
    {
        try
        {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            for (int s; (s = input.read(buffer)) != -1;)
            {
                baos.write(buffer, 0, s);
            }
            byte result[] = baos.toByteArray();
            input.close();
            return result;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        return null;
    }
}
