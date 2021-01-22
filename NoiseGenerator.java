package eu.wildweed;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
	
	
abstract class NoiseGenerator {
		
	private static final float SAMPLE_RATE = 22050f;
	private static final int SAMPLE_SIZE = (int) (10 * SAMPLE_RATE);
	private static final byte[] sample = new byte[SAMPLE_SIZE];
		
	private static Clip clip;
	FloatControl gainControl;
		
	static void init() {
			 
		AudioFormat af = new AudioFormat(
		SAMPLE_RATE, // sampleRate
		8,           // sampleSizeInBits
		1,           // channels
		true,        // signed
		false);      // bigEndian
			 
			
		//Generate an array of random numbers
		new Random().nextBytes(sample);
		

		AudioInputStream is = new AudioInputStream(
			new ByteArrayInputStream(sample), af, sample.length);
			
		try {
			clip = AudioSystem.getClip();
			clip.open(is);
		} catch(Exception ex) {return;}
		
	} //End method
		
	
	static void setVolume(double gain) {
		
		//Due to the mechanics of human hearing logarithmic relation is needed
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(gain) * 20); 

		if(clip.isRunning()) {
			clip.stop();
			gainControl.setValue(dB);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else
			gainControl.setValue(dB);
	
	} //End method
	
	
	static void play() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
		
		
	static void stop() {
		if(clip.isRunning())
			clip.stop();
	}
		
} //End class