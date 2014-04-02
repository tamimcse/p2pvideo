/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;
import leecher.Config;

/**
 *
 * @author Tamim
 */
public class GopBasedSplitter implements ISplitter
{
    public static final int NUM_OF_GOP = 50;
    private static int count = 0;

    @Override
    public int splitFiles(File videoFile, int numberOfGOP) throws Exception
    {
        //create a media reader
        IMediaReader mediaReader = ToolFactory.makeReader(videoFile.getAbsolutePath());
        // have the reader create a buffered image that others can reuse
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        Cutter cutter = new Cutter();

        //add a viewer to the reader, to see the decoded media
        mediaReader.addListener(cutter);
        //read and decode packets from the source file and
        //dispatch decoded audio and video to the writer
        int fileCounter = 1;
        String url = videoFile.getAbsolutePath().substring(0, videoFile.getAbsolutePath().lastIndexOf(".")) + "_" + fileCounter +"."+ Config.FILE_EXTENSION;
        IMediaWriter writer = ToolFactory.makeWriter(url, mediaReader);
        cutter.addListener(writer);
        while (mediaReader.readPacket() == null)
        {
            if (cutter.isComplete)
            {
                if(count < NUM_OF_GOP)
                {
                    count++;
                    continue;
                }
                count = 0;
                cutter.removeListener(writer);
                writer.close();// flusing and closing earlier writers..
                fileCounter++;
                url = videoFile.getAbsolutePath().substring(0, videoFile.getAbsolutePath().lastIndexOf(".")) + "_" + fileCounter +"."+ Config.FILE_EXTENSION;
                writer = ToolFactory.makeWriter(url, mediaReader);
                writer.addListener(ToolFactory.makeDebugListener());
                cutter.addListener(writer);
            }
        }
//        writer.close();// flusing and closing earlier writers..
//        mediaReader.close();
        return fileCounter;
    }

    public class Cutter extends MediaToolAdapter
    {
        private boolean isComplete = false;

        @Override
        public void onVideoPicture(IVideoPictureEvent arg0)
        {
            isComplete = arg0.getPicture().isComplete();
            super.onVideoPicture(arg0);
        }
    }
}
