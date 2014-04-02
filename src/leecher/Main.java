package leecher;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import messages.MessageManager;
import org.apache.commons.io.FileUtils;
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
                
        if (!Files.exists(Paths.get("temp.txt"), LinkOption.NOFOLLOW_LINKS) && Files.exists(Paths.get(Config.localDir + "\\" + Config.fileName + "." + Config.FILE_EXTENSION), LinkOption.NOFOLLOW_LINKS))
        {
            ISplitter splitter = SplitterFactory.INSTANCE.getSplitter(Config.IS_GOP_BASED_SPLITTING);
            Config.NUM_OF_FILES = splitter.splitFiles(new File(Config.localDir + "\\" + Config.fileName + "." + Config.FILE_EXTENSION), Config.CHUNK_SIZE);
            FileUtils.write(new File("temp.txt"), Config.NUM_OF_FILES+"");
            Config.IS_SEEDER = true;
        }

        System.out.println("chunk_size: " + Config.CHUNK_SIZE);
        SocketListener listener = new SocketListener();
        listener.start();
        
        if(!Config.IS_SEEDER)
        {
            SocketSender sendHello = new SocketSender(Config.SEEDER_IP, Config.SEEDER_PORT, MessageManager.getHelloMessage(Inet4Address.getLocalHost().getHostAddress(),Config.LOCAL_PORT));
            MessageSender.addTask(sendHello);
        }
    }
}
