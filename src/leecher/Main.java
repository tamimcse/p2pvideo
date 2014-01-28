package leecher;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import java.io.File;
import java.io.IOException;
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
        VideoSplitter v = new VideoSplitter(new File("D:\\Newfolder\\files\\12.mp4"), 5);
//        VLCMediaPlayer.INSTANCE.play("D:\\Newfolder\\12.mp4");
//        v.splitFiles();
        System.out.println("chunk_size: " + Config.CHUNK_SIZE);
        Leecher seeder = new Leecher();
        seeder.start();
        MessageSender leecher = new MessageSender();
        leecher.start();
    }

}
