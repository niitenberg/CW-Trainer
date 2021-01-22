package eu.wildweed;
import javax.swing.UIManager;
import eu.wildweed.GUI;
import eu.wildweed.DrawPanel;
import eu.wildweed.CWPlayer;
import java.util.Locale;
import java.util.ResourceBundle;


//								//
//	© Andrus Niitenberg 2015	//
//								//


public class CWTrainerApp {
	
	static final int defaultSpeed = 15;
	static final int defaultVolume = 50;
	
	private static GUI gui;
	private static QueueManager qmanager;
	private static Locale currentLocale;
	
	public static void main(String[] args) {
			
		CWPlayer.init();
		CWPlayer.setVolume(defaultVolume / 100.0);
		
		NoiseGenerator.init();
		NoiseGenerator.setVolume(defaultVolume / 300.0);
		
		qmanager = new QueueManager();
		
		gui = new GUI();
		gui.build();
			
		CWPlayer.setSpeed(25);
		
		//Say hello
		CWPlayer.play(
			ResourceBundle.getBundle(
			"eu/wildweed/res/labels", currentLocale).getString("hello"));
		
		CWPlayer.setSpeed(defaultSpeed);

	} //End main()
	
	static Locale getCurrentLocale() {
		return currentLocale;
	}
	
	static void setCurrentLocale(Locale locale) {
		currentLocale = locale;
	}
	
	static GUI getGUI() {
		return gui;
	}
	
	static QueueManager getQueueManager() {
		return qmanager;
	}
		
} //End class

