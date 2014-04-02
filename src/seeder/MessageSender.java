/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package seeder;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import leecher.Config;
import leecher.SocketSender;
import messages.MessageManager;

/**
 *
 * @author Tamim
 */
public class MessageSender
{

    static ExecutorService executor = Executors.newFixedThreadPool(Config.THREAD_POOL);

    public MessageSender()
    {
    }

    public void send()
    {
        try
        {
            SocketSender replyHello = new SocketSender(Config.LEECHER_IP, Config.LEECHER_PORT, MessageManager.getHandshakeMessage("1", "4888884", Config.NUM_OF_FILES));
            executor.execute(replyHello);
            
            for (int i = 1; i <= Config.NUM_OF_FILES; i++)
            {
                byte[] bytes = Files.readAllBytes(Paths.get(String.format("%s/%s_%d.%s", Config.localDir, Config.fileName, i, Config.FILE_EXTENSION)));
                SocketSender sendPiece = new SocketSender(Config.LEECHER_IP, Config.LEECHER_PORT, MessageManager.getVideoPieceMessage(i, bytes));
                executor.execute(sendPiece);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void addTask(Runnable r)
    {
        executor.execute(r);
    }
}
