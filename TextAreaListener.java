package eu.wildweed;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


class TextAreaListener implements KeyListener {
	
	public void keyTyped(KeyEvent ev) {}
	public void keyPressed(KeyEvent ev) {}
	
	public void keyReleased(KeyEvent ev) {
			
		CWPlayer.play(ev.getKeyChar());
			
	}

}