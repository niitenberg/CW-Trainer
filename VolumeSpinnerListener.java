package eu.wildweed;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSpinner;
import javax.sound.sampled.FloatControl;

class VolumeSpinnerListener implements ChangeListener {
	
	public void stateChanged(ChangeEvent ev) {
		
		JSpinner spinner = (JSpinner) ev.getSource();
		double gain = ((int) spinner.getValue()) / 100.0;
		
		CWPlayer.setVolume(gain);
		NoiseGenerator.setVolume(gain / 3.0);
	
	}		

}