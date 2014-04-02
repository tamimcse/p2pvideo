/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package leecher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import messages.MessageManager;

/**
 *
 * @author Tamim
 */
public class SocketSender implements Runnable
{
    String ip;
    byte [] data;
    int port;

    public SocketSender(String ip, int port, String data)
    {
        this.ip = ip;
        this.data = data.getBytes();
        this.port = port;
    }
    
    public SocketSender(String ip, int port, byte [] data)
    {
        this.ip = ip;
        this.data = new byte[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.port = port;
    }
    
    @Override
    public void run()
    {
        Socket socket = null;
        String response;
        try
        {
            if(!Config.isProxy)
            {
                socket = new Socket(ip, port);
                send(socket, data);
                socket.close();                
            }
            else
            {
                socket = new Socket(Config.proxy_server, Config.proxy_port);
                byte [] addressField = (this.ip+":"+this.port+"$$$").getBytes();
                byte [] headerLength = ByteBuffer.allocate(4).putInt(addressField.length+4).array();
                byte [] message = new byte[headerLength.length+addressField.length+data.length];
                System.arraycopy(headerLength, 0, message, 0, headerLength.length);
                System.arraycopy(addressField, 0, message, headerLength.length, addressField.length);
                System.arraycopy(data, 0, message, headerLength.length+addressField.length, data.length);
                send(socket, message);
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
