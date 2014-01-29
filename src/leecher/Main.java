package leecher;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;
import seeder.MessageSender;
import org.apache.commons.io.FileUtils;
import video.MediaPlayer;
import video.VLCMediaPlayer;
import video.VideoSplitter;

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
            VideoSplitter v = new VideoSplitter(new File(Config.localDir + "\\" + Config.fileName + "." + Config.FILE_EXTENSION), Config.CHUNK_SIZE);
            Config.NUM_OF_FILES = v.splitFiles();
            FileUtils.write(new File("temp.txt"), Config.NUM_OF_FILES+"");
        }

        System.out.println("chunk_size: " + Config.CHUNK_SIZE);
        Leecher leecher = new Leecher();
        leecher.start();
        MessageSender seeder = new MessageSender();
        seeder.start();
    }
}
