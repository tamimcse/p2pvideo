/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.medialist.MediaList;
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
public class VLCMediaPlayer
{
 public static void play(){
     try
     {
        NativeLibrary.addSearchPath("libvlc", "C:/Program Files (x86)/VideoLAN/VLC");
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

        Canvas canvas = new Canvas();
        canvas.setBackground(Color.black);
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);

        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        mediaPlayer.setVideoSurface(videoSurface);

        MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();

        mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
            @Override
            public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                System.out.println("nextItem()");
            }
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

        MediaList mediaList = mediaPlayerFactory.newMediaList();
        String[] options = {};
        mediaList.addMedia("D:\\Newfolder\\files\\output0.mpg", options);
        mediaList.addMedia("D:\\Newfolder\\files\\output1.mpg", options);
        mediaList.addMedia("D:\\Newfolder\\files\\output2.mpg", options);
        mediaList.addMedia("D:\\Newfolder\\files\\output3.mpg", options);
        mediaList.addMedia("D:\\Newfolder\\files\\output4.mpg", options);
        
        mediaListPlayer.setMediaList(mediaList);
        mediaListPlayer.setMode(MediaListPlayerMode.DEFAULT);

        mediaListPlayer.play();

        // This looping is just for purposes of demonstration, ordinarily you would
        // not do this of course
//        for(;;) {
//            Thread.sleep(500);
//            mediaPlayer.setChapter(3);
//
//            Thread.sleep(5000);
//            mediaListPlayer.playNext();
//        }
//
//            mediaList.release();
//            mediaListPlayer.release();
//            mediaPlayer.release();
//            mediaPlayerFactory.release();
     }
     catch(Exception ex)
     {
         ex.printStackTrace();
     }
    }   
}
