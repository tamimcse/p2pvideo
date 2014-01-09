package leecher;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import messages.MessageManager;
import messages.MessageType;
import org.apache.commons.io.FileUtils;
import video.VLCMediaPlayer;

/**
 *
 * @author Tamim
 */
public class MessageReceiver extends Thread
{
    static int count = 0;
    Socket clientSocket;
    ArrayList<String> chunks = new ArrayList<String>();
    static TreeMap<String, String> sortMap = new TreeMap<String, String>();

    public MessageReceiver(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            byte [] bytes = receiveByte(clientSocket);
            MessageType type = MessageManager.getType(bytes);
            if (type == MessageType.HANDSHAKE)
            {
                System.out.println("Hand shake message received");
            }
            else
            {
                byte [] fileData = new byte[bytes.length - 8];
                System.arraycopy(bytes, 8, fileData, 0, fileData.length);
                Files.write(Paths.get(String.format("%s/download/%s%d.mpg", Config.localDir, Config.fileName, count++)), fileData, StandardOpenOption.CREATE);
                VLCMediaPlayer.play();

            }
            clientSocket.close();
        }
        catch (IOException e)
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
            for(int s; (s=input.read(buffer)) != -1; )
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
