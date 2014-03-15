package proxyserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Tamim
 */
public class SocketListener extends Thread
{

    ServerSocket serverSocket = null;

    public SocketListener()
    {
        try
        {
            serverSocket = new ServerSocket(30);
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
//            byte [] message = receiveByte(clientSocket);
//            String s = new String(message);
//            System.out.println("Received Data: "+s);
        }
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