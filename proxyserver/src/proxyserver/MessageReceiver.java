package proxyserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Tamim
 */
public class MessageReceiver extends Thread
{
    Socket clientSocket;

    public MessageReceiver(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            byte[] bytes = receiveByte(clientSocket);
            String incomingMessage = new String(bytes);
            String [] s = incomingMessage.split("\\$\\$\\$");
            if(s.length == 2)
            {
                String [] ip_port = s[0].split(":");
                if(ip_port.length == 2)
                {
                    Thread.sleep(10000);
                   new SocketSender(ip_port[0], Integer.parseInt(ip_port[1]), s[1]).start(); 
                }
                else
                {
                    System.out.println("No IP Port information");
                }
            }
            else
            {
                System.out.println("Incorrect Data");
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
