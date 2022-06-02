package Audio;

import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

@SuppressWarnings("serial")
public class AudioPlayer implements Serializable
{
	private transient AudioInputStream ais;
	private transient Clip clip;
	
	
	/**
     * Constructs a new {@code AudioPlayer}
     * @param     s path to audio
     */
	public AudioPlayer(String s)
	{
		try {
			ais = AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream(s)
			);
			AudioFormat baseFormat =  ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate() ,
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream( decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	/**
     * Start play audio
     * @see #setFramePosition(int frames)
     * @see #start()
     */
	public void play()
	{
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	/**
     * stop playing audio
     * @see #isRunning()
     * @see #stop()
     */
	private void stop()
	{
		if(clip.isRunning()) clip.stop();
	}
	
	/**
     * disable audio
     * @see #close()
     */
	public void close()
	{
		stop();
		clip.close();
	}
}
