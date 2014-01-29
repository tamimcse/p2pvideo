/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package seeder;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import leecher.Config;
import messages.MessageManager;

/**
 *
 * @author Tamim
 */
public class MessageSender extends Thread
{
    @Override
    public void run()
    {
        Socket socket = null;
        String response;
        try
        {
            socket = new Socket(Config.SEEDER_IP, Config.SEEDER_PORT);
            send(socket, MessageManager.getHandshakeMessage("1", "4888884", Config.NUM_OF_FILES));
            socket.close();
            
            for(int i = 1; i <= Config.NUM_OF_FILES; i++)
            {
                socket = new Socket(Config.SEEDER_IP, Config.SEEDER_PORT);
                byte[] bytes = Files.readAllBytes(Paths.get(String.format("%s/%s_%d.mp4", Config.localDir, Config.fileName, i)));
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
