package eu.wildweed;
import javax.swing.JList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


//								//
//	© Andrus Niitenberg 2015	//
//								//


class WordListListener extends MouseAdapter {
	
	 public void mouseClicked(MouseEvent ev) {
        
		JList wordlist = (JList) ev.getSource();
        
		if (ev.getClickCount() == 1) {
            
			//Single-click: set next word in QueueManager
            int index = wordlist.locationToIndex(ev.getPoint());
			CWTrainerApp.getQueueManager().setNextIndex(index);
			
        } 
		
		else if (ev.getClickCount() == 2) {

			//Double-click: play clicked word
			int index = wordlist.locationToIndex(ev.getPoint());
			final String word = (String) wordlist.getModel().getElementAt(index);
			
			if(!CWTrainerApp.getQueueManager().isWorking() && !CWPlayer.isPlaying) {
				
				CWTrainerApp.getGUI().getStartButton().setEnabled(false);
				CWTrainerApp.getGUI().getTextArea().setEnabled(false);
				
				new Thread(new Runnable() {
					public void run() {
						
						CWPlayer.play(word);
						
						CWTrainerApp.getGUI().getStartButton().setEnabled(true);
						CWTrainerApp.getGUI().getTextArea().setEnabled(true);
					}
				}).start();
				
				
			}
			
		}
		
    } //End method
	
} //End class