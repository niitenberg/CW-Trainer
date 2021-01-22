package eu.wildweed;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import eu.wildweed.NoiseGenerator;


//								//
//	© Andrus Niitenberg 2015	//
//								//


class QueueManager {
	
	private boolean removeDuplicates, randomOrder, addNoise, endlessLoop, repeatPattern;
	private ArrayList<String> listWithDuplicates;
	private ArrayList<String> listWithoutDuplicates;
	private ArrayList<String> wordlist;
	private volatile boolean stopWorking, isWorking;
	private int userSelectedIndex = -1;

	
	void removeDuplicates(boolean remove) {
			
			if(remove)
				wordlist = listWithoutDuplicates;
			else
				wordlist = listWithDuplicates;
			
			
			CWTrainerApp.getGUI().getWordList().setListData(
				wordlist.toArray(new String[wordlist.size()]));
	}
	

	void randomOrder(boolean value) {
		randomOrder =  value;
	}
	
	void addNoise(boolean value) {
		addNoise =  value;
	}
	
	void endlessLoop(boolean value) {
		endlessLoop =  value;
	}
	
	void repeatPattern(boolean value) {
		repeatPattern =  value;
	}
		
	void setList(ArrayList<String> list) {
		listWithDuplicates = new ArrayList<String>(list);
		listWithoutDuplicates = new ArrayList<String>(new LinkedHashSet<String>(list));
		
		if(CWTrainerApp.getGUI().getCheck1().isSelected())
			wordlist = listWithoutDuplicates;
		else
			wordlist = listWithDuplicates;

		
		CWTrainerApp.getGUI().getWordList().setListData(
			wordlist.toArray(new String[wordlist.size()]));
	}
	
	void stop() {
		
		//We cannot use Thread.interrupt() here because interruption gets
		//lost somewhere deeper than CWPlayer.play() methods
		stopWorking = true;

	}
	
	
	void work() {

		new Thread(new Runnable() {
		
			public void run() {		
				
				isWorking = true;

				//Check if user has selected the "add noise" option
				if(CWTrainerApp.getGUI().getCheck3().isSelected())
					NoiseGenerator.play();		
				
				do {
					
					for(int i = 0; i < wordlist.size(); i++) {
						
						while(randomOrder && !stopWorking) {
						
							//Play user-selected word in random order mode
							if(userSelectedIndex != -1) {
								commencePlay(userSelectedIndex);
								userSelectedIndex = -1;
							}		
							else
								commencePlay((int) (Math.random() * wordlist.size()));		
						} 
							
			
						if(!stopWorking) {
							
							if(userSelectedIndex != -1) {
								i = userSelectedIndex;
								userSelectedIndex = -1;
							} 	
							
							commencePlay(i);			

						}
						else {
								stopping();
								return;
							}
									
					} //End loop
							
				} while(endlessLoop); //End loop
				
				//We need to change the state of the Start/Stop button
				CWTrainerApp.getGUI().getStartButton().doClick();
				stopping();			
						
			} //End method

		}).start(); //End thread
		
	} //End method
	
	void commencePlay(int index) {
		
		CWTrainerApp.getGUI().getWordList().ensureIndexIsVisible(index);
		CWTrainerApp.getGUI().getWordList().setSelectedIndex(index);
	
		CWPlayer.play(wordlist.get(index));
		
		
		//Playing a long word at slow speed can take a lot of time, therefore
		//we need to check constantly for user-initiated Stop to return as quickly as possible
		
		if(repeatPattern) {
			try {
				if(stopWorking)
					return;
				
				Thread.sleep(2500);
				
				if(stopWorking)
					return;
				
				CWPlayer.play(wordlist.get(index));
				
				if(stopWorking)
					return;
				
				CWPlayer.play(wordlist.get(index));
				
				if(stopWorking)
					return;
				
				CWPlayer.play(wordlist.get(index));
				
				if(stopWorking)
					return;
				
				Thread.sleep(1000);
				
				if(stopWorking)
					return;
				
				CWPlayer.play(wordlist.get(index));
				
				if(stopWorking)
					return;
				
				Thread.sleep(3500);
				
			} catch(InterruptedException ex) {/*Nothing needs to be done here*/}
		}		
	} //End method
	
	void stopping() {
		
		stopWorking = false;
		isWorking = false;
		
		CWTrainerApp.getGUI().getStartButton().setEnabled(true);
		CWTrainerApp.getGUI().getCheck1().setEnabled(true);
		CWTrainerApp.getGUI().getTextArea().setEnabled(true);
		CWTrainerApp.getGUI().getLoadButton().setEnabled(true);
	
		if(addNoise)
			NoiseGenerator.stop();
	}
	
	boolean isWorking() {
		return isWorking;
	}
	
	void setNextIndex(int index) {
		userSelectedIndex = index;
	}
	
} //End class