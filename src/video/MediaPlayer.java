package video;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.ToolFactory;

/**
 *
 * @author Tamim
 */
public class MediaPlayer
{
    public static void playVideo(String inputFilename)
    {
        // create a media reader
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
        // create a media viewer with stats enabled
        IMediaViewer mediaViewer = ToolFactory.makeViewer(true);
        // add a viewer to the reader, to see the decoded media
        mediaReader.addListener(mediaViewer);
        while (mediaReader.readPacket() == null);
    }
}
