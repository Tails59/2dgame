package engine2d;

import java.io.*;
import javax.sound.sampled.*;

public class Sound extends Thread {

	String filename;		// The name of the file to play
	boolean finished;		// A flag showing that the thread has finished
	
	private Clip clip;
	private boolean loop;
	
	/**
	 * Create a new Sound
	 * @param fname
	 * @param startPaused
	 * @param loop
	 * @param af [FilteredAudio] A filtered audio stream
	 */
	public Sound(String fname, boolean loop) {
		filename = fname;
		finished = false;
		this.loop = loop;
		//this.echo = echoEffect;
	}

	/**
	 * run will play the actual sound but you should not call it directly.
	 * You need to call the 'start' method of your sound object (inherited
	 * from Thread, you do not need to declare your own). 'run' will
	 * eventually be called by 'start' when it has been scheduled by
	 * the process scheduler.
	 */
	public void run() {
		try {
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat	format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
			
			if(this.loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
			
			Thread.sleep(100);
			while (clip.isRunning()) { Thread.sleep(100); }
			clip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finished = true;
	}
}
