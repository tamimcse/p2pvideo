package leecher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Tamim
 */
public class Leecher extends Thread
{

    ServerSocket serverSocket = null;

    public Leecher()
    {
        try
        {
            serverSocket = new ServerSocket(Config.LOCAL_PORT);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void run()
    {
        Socket clientSocket = null;
        while (true)
        {
            try
            {
                clientSocket = serverSocket.accept();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            MessageReceiver messageHandler = new MessageReceiver(clientSocket);
            messageHandler.start();
        }
    }
}
