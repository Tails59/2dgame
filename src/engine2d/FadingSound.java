package engine2d;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class FadingSound extends Thread {
	String filename; // The name of the file to play
	boolean finished; // A flag showing that the thread has finished

	private Clip clip;

	/**
	 * Create a new Sound
	 * 
	 * @param fname
	 * @param startPaused
	 * @param loop
	 * @param af          [FilteredAudio] A filtered audio stream
	 */
	public FadingSound(String fname) {
		filename = fname;
		finished = false;
		// this.echo = echoEffect;
	}

	/**
	 * run will play the actual sound but you should not call it directly. You need
	 * to call the 'start' method of your sound object (inherited from Thread, you
	 * do not need to declare your own). 'run' will eventually be called by 'start'
	 * when it has been scheduled by the process scheduler.
	 */
	public void run() {
		try {
			File file = new File(filename);
			AudioInputStream stream = AudioSystem.getAudioInputStream(file);
			AudioFormat format = stream.getFormat();

			FadeFilterStream filter = new FadeFilterStream(stream, true);
			AudioInputStream ais = new AudioInputStream(filter, format, stream.getFrameLength());

			DataLine.Info info = new DataLine.Info(Clip.class, format);

			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);
			clip.start();

			Thread.sleep(100);
			while (clip.isRunning()) {
				Thread.sleep(100);
			}
			clip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finished = true;
	}

	private class FadeFilterStream extends FilterInputStream {
		boolean fadeIn;
		
		FadeFilterStream(InputStream in, boolean fadeIn) {
			super(in);
			this.fadeIn = fadeIn;
		}

		public short getSample(byte[] buffer, int position) {
			return (short) (((buffer[position + 1] & 0xff) << 8) | (buffer[position] & 0xff));
		}

		public void setSample(byte[] buffer, int position, short sample) {
			buffer[position] = (byte) (sample & 0xFF);
			buffer[position + 1] = (byte) ((sample >> 8) & 0xFF);
		}

		public int read(byte[] sample, int offset, int length) throws IOException {
			int bytesRead = super.read(sample, offset, length);
			float change = 2.5f * (1.0f / (float) bytesRead);

			float volume = 0.8f;
			short amp = 0;

			for (int p = 0; p < bytesRead; p = p + 2) {
				amp = getSample(sample, p);
				amp = (short) ((float) amp * volume);
				setSample(sample, p, amp);
				
				if (volume <= 0.1f) {
					fadeIn = false;
				} else if (volume >= 0.8f) {
					fadeIn = true;
				}
				
				if (fadeIn) {
					volume = volume + change;
				} else {
					volume = volume - change;
				}
			}
			return length;

		}
	}
}
