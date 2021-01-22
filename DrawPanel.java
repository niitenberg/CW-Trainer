package eu.wildweed;
import javax.swing.JPanel;
import java.awt.*;
import java.text.AttributedString;
import java.awt.font.TextAttribute;
import java.awt.FontMetrics;


//								//
//	© Andrus Niitenberg 2015	//
//								//


public class DrawPanel extends JPanel implements CWListener {
		
	
	private String word = new String();
	private AttributedString theWord;
	private Font font = new Font("SansSerif", Font.PLAIN, 36);
	FontMetrics fm = getFontMetrics(font);
	
	private int toCenterX, toCenterY; 
	

	public void paintComponent(Graphics g) {
		
		//To clear the painting surface
		super.paintComponent(g);
		
		toCenterY = (int) (getHeight() / 2.0 + (fm.getHeight() / 2.0));
		
		if(theWord != null)
			g.drawString(theWord.getIterator(), toCenterX, toCenterY);

	} //End method
	
	
	public void cwStartEvent(String word, int index) {
		
		theWord = new AttributedString(word);
		toCenterX = (int) (this.getWidth() / 2.0 - (fm.stringWidth(word) / 2.0));
		
	
		theWord.addAttribute(TextAttribute.FONT, font);
		theWord.addAttribute(TextAttribute.FOREGROUND, Color.gray, 0, word.length());
		
		if(index > 0) {
			
			theWord.addAttribute(TextAttribute.FOREGROUND, Color.gray, 0, index + 1);
			
			if(index < (word.length() - 1))
				theWord.addAttribute(TextAttribute.FOREGROUND, Color.gray, index + 1, word.length());
		}
			
		theWord.addAttribute(TextAttribute.FOREGROUND, Color.black, index, index + 1);	
				
		this.repaint();
		
	} //End method
	
	
	public void cwStopEvent() {
		
		theWord = null;
		this.repaint();	
	}

			
} //End class