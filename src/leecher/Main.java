package leecher;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import messages.MessageManager;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import seeder.MessageSender;
import video.ISplitter;
import video.SplitterFactory;

/**
 *
 * @author Tamim
 */
public class Main
{

//    private static final String inputFilename = "D:/Newfolder/Wildlife.mpg";
    public static void main(String[] args) throws IOException, Exception
    {
        
        if (Files.exists(Paths.get("temp.txt"), LinkOption.NOFOLLOW_LINKS))
        {
            Config.IS_SEEDER = true;
            Config.NUM_OF_FILES = Integer.parseInt(FileUtils.readFileToString(new File("temp.txt")));
        }
                
        if (!Files.exists(Paths.get("temp.txt"), LinkOption.NOFOLLOW_LINKS) && Files.exists(Paths.get(Config.localDir + "/" + Config.fileName + "." + Config.FILE_EXTENSION), LinkOption.NOFOLLOW_LINKS))
        {
            ISplitter splitter = SplitterFactory.INSTANCE.getSplitter(Config.IS_GOP_BASED_SPLITTING);
            Config.NUM_OF_FILES = splitter.splitFiles(new File(Config.localDir + "/" + Config.fileName + "." + Config.FILE_EXTENSION), Config.CHUNK_SIZE);
            FileUtils.write(new File("temp.txt"), Config.NUM_OF_FILES+"");
            Config.IS_SEEDER = true;
            
            if(Config.NUM_OF_FILES == 0)
            {
                System.out.println("Number of files to be sent is zero");
            }
        }

        System.out.println("chunk_size: " + Config.CHUNK_SIZE);
        SocketListener listener = new SocketListener();
        listener.start();
        
        if(Config.IS_SEEDER)
        {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/port", new SeederPortHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        }
        else
        {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://"+Config.SEEDER_IP+":8000/port");
            HttpResponse response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader
              (new InputStreamReader(response.getEntity().getContent()));
    
            String line = "";
            if ((line = rd.readLine()) != null) 
            {
                Config.SEEDER_PORT = Integer.parseInt(line);
            } 
            
            SocketSender sendHello = new SocketSender(Config.SEEDER_IP, Config.SEEDER_PORT, MessageManager.getHelloMessage(Inet4Address.getLocalHost().getHostAddress(),Config.LOCAL_PORT));
            MessageSender.addTask(sendHello);
        }
    }
    
       static class SeederPortHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = ""+Config.SEEDER_PORT;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
