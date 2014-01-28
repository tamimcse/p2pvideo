/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import com.sun.jna.NativeLibrary;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListItem;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

/**
 *
 * @author Tamim
 */
public enum VLCMediaPlayer
{

    INSTANCE;
    MediaListPlayer mediaListPlayer;
    MediaPlayerFactory mediaPlayerFactory;
    boolean isPlayStarted = false;
    
    VLCMediaPlayer()
    {
        NativeLibrary.addSearchPath("libvlc", "C:/Program Files (x86)/VideoLAN/VLC");
        mediaPlayerFactory = new MediaPlayerFactory();

        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);

        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(videoSurface);

        mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();

        mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter()
        {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl)
            {
                
                System.out.println("nextItem()");

            }
        });
        
        mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter()
        {
        });

        mediaListPlayer.setMediaPlayer(mediaPlayer); // <--- Important, associate the media player with the media list player

        JPanel cp = new JPanel();
        cp.setBackground(Color.black);
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);

        JFrame f = new JFrame("P2P Video");
//        f.setIconImage(new ImageIcon(TestMediaListEmbeddedPlayer.class.getResource("/icons/vlcj-logo.png")).getImage());
        f.setContentPane(cp);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setAlwaysOnTop(true);

        MediaList mediaList = mediaPlayerFactory.newMediaList();

        mediaListPlayer.setMediaList(mediaList);
        mediaListPlayer.setMode(MediaListPlayerMode.DEFAULT);
    }

    public void play(String file)
    {
        try
        {
            String[] options =
            {
            };

            if(!isPlayStarted)
            {
                            this.mediaListPlayer.getMediaList().addMedia(file, options);
                mediaListPlayer.play();
                isPlayStarted = true;
            }
            else
            {
                MediaList mediaList = mediaPlayerFactory.newMediaList();
                mediaList.addMedia(file, options);
                mediaListPlayer.setMediaList(mediaList);
                mediaListPlayer.play();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
