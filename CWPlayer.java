package eu.wildweed;
import javax.sound.sampled.*;
import java.util.*;

abstract class CWPlayer {
	
	//Standard ratio of Dash/Dot
	static final int WEIGTH = 3;
	
	//Frequency of the tone
	static final int PITCH = 800;
	
	//Samples will have 5 milliseconds wide smooth edges
	static final int EDGE_WIDTH = 5;
	
	//Sample rate
	static float SAMPLE_RATE = 22050f;
	
	//Length of the smallest time unit
	static int dotLength;

	//These will hold the Dot and Dash samples
	static byte[] dotSampleWithPause;
	static byte[] dashSampleWithPause;
	
	//Pause between letters is also a sample because Thread sleeping will 
	//not give good results here
	static byte[] letterPauseSample;
	
	//Audio channel and code alphabet
	static AudioFormat af;
	static SourceDataLine sdl;
	static HashMap<Character, String> codes;
	
	//Morse event listners
	static ArrayList<CWListener> listeners = new ArrayList<CWListener>();
	
	static boolean isPlaying;

	 	
		
	public static void init() {
		
			af = new AudioFormat(
            SAMPLE_RATE, // sampleRate
            8,           // sampleSizeInBits
            1,           // channels
            true,        // signed
            false);      // bigEndian
		
		try {
			sdl = AudioSystem.getSourceDataLine(af);
			sdl.open(af);
			sdl.start();				
		}
		catch(LineUnavailableException ex) {
			System.err.println("Audio channel unavailable:");
			ex.printStackTrace();
			System.exit(1);	
		}
		
		//Build alphabet
		codes = new HashMap<Character, String>();
		codes.put('a', "01");
        codes.put('b', "1000");
		codes.put('c', "1010");
		codes.put('d', "100");
		codes.put('e', "0");
		codes.put('f', "0010");
		codes.put('g', "110");
		codes.put('h', "0000");
		codes.put('i', "00");
		codes.put('j', "0111");
		codes.put('k', "101");
		codes.put('l', "0100");
		codes.put('m', "11");
		codes.put('n', "10");
		codes.put('o', "111");
		codes.put('p', "0110");
		codes.put('q', "1101");
		codes.put('r', "010");
		codes.put('s', "000");
		codes.put('t', "1");
		codes.put('u', "001");
		codes.put('v', "0001");
		codes.put('w', "011");
		codes.put('x', "1001");
		codes.put('y', "1011");
		codes.put('z', "1100");
		codes.put('0', "11111");
		codes.put('1', "01111");
		codes.put('2', "00111");
		codes.put('3', "00011");
		codes.put('4', "00001");
		codes.put('5', "00000");
		codes.put('6', "10000");
		codes.put('7', "11000");
		codes.put('8', "11100");
		codes.put('9', "11110");
		codes.put('.', "010101");
		codes.put(',', "110011");
		codes.put(':', "111000");
		codes.put('?', "001100");
		codes.put('\'', "011110");
		codes.put('-', "100001");
		codes.put('/', "10010");
		codes.put('(', "101101");
		codes.put(')', "101101");
		codes.put(']', "101101");
		codes.put('[', "101101");
		codes.put('"', "010010");
		codes.put('@', "011010");
		codes.put('=', "10001");
		
		
	} //End method	
	
	
	public static void setSpeed(int wpm) {
		
		//There are 50 elements in standard morse word PARIS, in 60 seconds it is
		//1.2 elements per second at 1 Words Per Minute. Dot length at current WPM:
		
		dotLength = (int) (1000 * (1.2 / (double) wpm));
		
		int dotSampleSize = (int) (1.2 / wpm * (double) SAMPLE_RATE);
		int dashSampleSize = dotSampleSize * WEIGTH;

		//Build samples
		dotSampleWithPause = new byte[2 * dotSampleSize];
		dashSampleWithPause = new byte[dashSampleSize + dotSampleSize];
		letterPauseSample = new byte[2 * dotSampleSize];
		
		int samplesInPatternEdge = (int) SAMPLE_RATE * EDGE_WIDTH / 1000;
		int samplesInOneWave = (int) SAMPLE_RATE / PITCH;
		

		for(int i = 0; i < dashSampleSize; i++) {
			
			//Write wave pattern to sample arrays
			if(i < dotSampleSize)
				dotSampleWithPause[i] = (byte) (Math.sin(i / (double) samplesInOneWave * Math.PI * 2.0 ) * 127);
			
			dashSampleWithPause[i] = (byte) (Math.sin(i / (double) samplesInOneWave * Math.PI * 2.0 ) * 127);	
				
		} //End loop
		
		
		//Put soft edges on samples
		for(int i = 0, j = dotSampleSize, k = dashSampleSize;
			i < samplesInPatternEdge; i++, j--, k--) {
			
			double coeff = Math.sin((i / (double) samplesInPatternEdge) * Math.PI / 2);
				
			//Left side
			dotSampleWithPause[i] = (byte) (dotSampleWithPause[i] * coeff);
			dashSampleWithPause[i] = (byte) (dashSampleWithPause[i] * coeff);
				
			//Right side
			dotSampleWithPause[j] = (byte) (dotSampleWithPause[j] * coeff);
			dashSampleWithPause[k] = (byte) (dashSampleWithPause[k] * coeff);
			
		} //End loop
		
		//It is opened in CWPlayer.init()
		sdl.stop();
		sdl.close();
		
		try {
			//Set new buffer size for the audioline
			sdl.open(af, dashSampleWithPause.length); 
			sdl.start();		
		}
		catch(LineUnavailableException ex) {
			System.err.println("Audio channel unavailable:");
			ex.printStackTrace();
			System.exit(1);
		}
		
	} //End method
	
	
	public static void play(char letter) {
		
		//Play single char morse sequence
		String codeSequence = codes.get(Character.toLowerCase(letter));

		//Pause between words
		if(letter == ' ')
			try {
				Thread.sleep(6 * dotLength);			
			} catch(InterruptedException ex) {/*Nothing needs to be done here*/}

		//Unknown letter
		if(codeSequence == null)
			return;
		
		for(int j = 0; j < codeSequence.length(); j++) {
				
			if(codeSequence.charAt(j) == '0')
				sdl.write(dotSampleWithPause, 0, dotSampleWithPause.length);
			else
				sdl.write(dashSampleWithPause, 0, dashSampleWithPause.length);
				
		} //End loop
			
		//Pause between letters
		sdl.write(letterPauseSample, 0, letterPauseSample.length);
		
	} //End method

	
	public static void play(String text) {
		
		isPlaying = true;
		
		for(int i = 0; i < text.length(); i++) {
				
			//Call possible morse event listeners for Start event 
			for(CWListener listener : listeners)
				listener.cwStartEvent(text, i);
			
			//Play single char
			play(text.charAt(i));
					
		} //End loop
		
		//Space between words
		try {
			Thread.sleep(6 * dotLength);			
		} catch(InterruptedException ex) {/*Nothing needs to be done here*/}
		
		sdl.drain();
						
		//Call possible morse event listeners for Stop event
		for(CWListener listener : listeners)
			listener.cwStopEvent();
		
		isPlaying = false;
	
	} //End method 


	static void setVolume(double gain) {
		
		//Due to the mechanics of human hearing logarithmic relation is needed
		FloatControl gainControl = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
		float dB = (float) (Math.log(gain) * 20.0);
		gainControl.setValue(dB);
	}

	
	static void close() {
		
		sdl.stop();
		sdl.flush();
		sdl.close();
	}	
	
	static void addCWListener(CWListener cwl) {
		listeners.add(cwl);
	}
	
	static boolean isPlaying() {
		return isPlaying;
	}

} //End class
	
	
//Morse event interface
interface CWListener {
	
	void cwStartEvent(String currentWord, int charIndex);
	void cwStopEvent();
	
}

	
	