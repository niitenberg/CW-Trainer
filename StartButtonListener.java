package eu.wildweed;
import java.awt.event.*;
import javax.swing.JButton;
import java.util.ResourceBundle;


class StartButtonListener implements ActionListener {

	private JButton source;
	private boolean isStopButton;

	public void actionPerformed(ActionEvent ev) {
		
		ResourceBundle labels = ResourceBundle.getBundle("eu/wildweed/res/labels", CWTrainerApp.getCurrentLocale());
		source = (JButton) ev.getSource();
		
		if(!isStopButton) {
			
			//Start playing
			CWTrainerApp.getGUI().getTextArea().setEnabled(false);
			CWTrainerApp.getGUI().getCheck1().setEnabled(false);
			CWTrainerApp.getGUI().getLoadButton().setEnabled(false);
			
			source.setText(labels.getString("stop"));
			CWTrainerApp.getQueueManager().work();
	
			isStopButton = true;
			
		} 
		else {
						
				//Stop playing
				CWTrainerApp.getQueueManager().stop();
					
				source.setText(labels.getString("start"));
				source.setEnabled(false);
				
				isStopButton = false;
			}

		
	} //End method
		
} //End class