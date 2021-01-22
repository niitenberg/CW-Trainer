package eu.wildweed;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSpinner;

class SpeedSpinnerListener implements ChangeListener {
	
	public void stateChanged(ChangeEvent ev) {
		
		JSpinner spinner = (JSpinner) ev.getSource();
		String string = (String) spinner.getValue();
		
		CWPlayer.setSpeed(Integer.parseInt(string));
	}
	
}