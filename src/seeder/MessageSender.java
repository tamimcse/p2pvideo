/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package seeder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import messages.MessageManager;
import org.apache.commons.io.FileUtils;
import leecher.Config;

/**
 *
 * @author Tamim
 */
public class MessageSender extends Thread
{
    ArrayList<String> splitFile() throws IOException
    {
        File file = new File(String.format("%s/%s", Config.localDir, Config.fileName));
        String content = FileUtils.readFileToString(file);
        int numberOfChunks = (int) Math.ceil(content.length() / ((double) Config.CHUNK_SIZE));
        ArrayList<String> chunks = new ArrayList<String>();
        for (int i = 0; i < numberOfChunks; i++)
        {
            if (numberOfChunks == 1)
            {
                chunks.add(content);
            }
            else if (i == numberOfChunks - 1)
            {
                chunks.add(content.substring(i * Config.CHUNK_SIZE, content.length()));
            }
            else
            {
                chunks.add(content.substring(i * Config.CHUNK_SIZE, (i + 1) * Config.CHUNK_SIZE));
            }
        }
        return chunks;
    }

    @Override
    public void run()
    {
        Socket socket = null;
        String response;
        try
        {
            socket = new Socket(Config.SEEDER_IP, Config.SEEDER_PORT);
            send(socket, MessageManager.getHandshakeMessage("1", "4888884"));
            socket.close();
            
            for(int i = 0; i < 5; i++)
            {
                socket = new Socket(Config.SEEDER_IP, Config.SEEDER_PORT);
                byte[] bytes = Files.readAllBytes(Paths.get(String.format("%s/files/%s%d.mpg", Config.localDir, Config.fileName, i)));
                send(socket, MessageManager.getVideoPieceMessage(i, bytes));
                socket.close();                          
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static void send(Socket clientSocket, String content)
    {

        try
        {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeBytes(content);
            output.flush();
            output.close();

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
    
    static void send(Socket clientSocket, byte [] content)
    {

        try
        {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.write(content);
            output.flush();
            output.close();

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }    
}
