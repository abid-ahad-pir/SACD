package sopore.net.sacd;

import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class WAVPlayer extends Thread implements LineListener {
     	
	private String threadId;
	private File file;
	static volatile boolean exitNow = false;
    
    /**
     * this flag indicates whether the playback completes or not.
     */
    boolean playCompleted;
    
     
    /**
     * Class Constructor
     * @param id ThreadID.
     * @param file Object of the audio file.
     */
    public WAVPlayer(String id, File file) {
        this.threadId = id;
        this.file = file;
    }
    
    /**
     * Run method since we are using MultiThread Approach for best UI experience, read non-blocked GUI
     */
    public void run() {
        play(file);
    }
    
    /**
     * Class Constructor
     * @param audioFile Object of the audio file.
     */
    private void play(File audioFile) {
        
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); 
            AudioFormat format = audioStream.getFormat(); 
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            audioClip.start();
             
            while (!playCompleted && !exitNow) {
                // wait for the playback completes
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
             
            audioClip.close();
             
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }
     
    /**
     * Listens to the START and STOP events of the audio line.
     */
    @SuppressWarnings("exports")
	@Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.START) {
           // System.out.println("Playback started.");
             
        } else if (type == LineEvent.Type.STOP) {
            playCompleted = true;
            //System.out.println("Playback completed.");
        }
    }
}
