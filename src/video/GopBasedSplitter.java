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
public class GopBasedSplitter
{

    private File videoFile = null;
    private int maximumSegmentInterval = 15;

//	private static final TimeUnit TIME_UNIT = TimeUnit.MICROSECONDS;	
    public GopBasedSplitter(File video, int maximumSegmentInterval)
    {
        if (video == null || !video.exists() || video.isDirectory())
        {
            throw new RuntimeException("The video file is not valid:" + video);
        }
        this.videoFile = video;
        this.maximumSegmentInterval = maximumSegmentInterval;
    }

    public int splitFiles() throws Exception
    {
        long inputTimeIntervalInMillies = TimeUnit.MICROSECONDS.convert(maximumSegmentInterval, TimeUnit.SECONDS);
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
            if (cutter.isKeyFrame() || cutter.getTimeCounter() > maximumSegmentInterval)
            {
                System.out.println("inputTimeIntervalInMillies=" + inputTimeIntervalInMillies);
                cutter.removeListener(writer);
                writer.close();// flusing and closing earlier writers..
                fileCounter++;
                url = videoFile.getAbsolutePath().substring(0, videoFile.getAbsolutePath().lastIndexOf(".")) + "_" + fileCounter +"."+ Config.FILE_EXTENSION;
                writer = ToolFactory.makeWriter(url, mediaReader);
                writer.addListener(ToolFactory.makeDebugListener());
                inputTimeIntervalInMillies = inputTimeIntervalInMillies + TimeUnit.MICROSECONDS.convert(maximumSegmentInterval, TimeUnit.SECONDS); // next time slot..
                cutter.addListener(writer);
            }
        }
//        writer.close();// flusing and closing earlier writers..
//        mediaReader.close();
        return fileCounter;
    }

    public class Cutter extends MediaToolAdapter
    {
        private long timeCounterInMillies = 0;
        private boolean isKeyFrame = false;

        @Override
        public void onVideoPicture(IVideoPictureEvent arg0)
        {
            timeCounterInMillies = arg0.getTimeStamp();
            isKeyFrame = arg0.getPicture().isKeyFrame();
            super.onVideoPicture(arg0);
        }

        public boolean isKeyFrame()
        {
            return isKeyFrame;
        }
        
        public long getTimeCounter()
        {
            return timeCounterInMillies;
        }        
    }
}
