package eu.wildweed;
import javax.swing.JFileChooser;
import java.io.File;
import java.awt.event.*;
import java.util.ResourceBundle;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;
import java.util.ArrayList;
import java.io.*;


//								//
//	© Andrus Niitenberg 2015	//
//								//


class LoadButtonListener implements ActionListener {
	
	//We will not translate the texts found in FileChooser window because 
	//this would already have the user's preferred language for the system
	
	private BufferedReader reader;
	private InputStreamReader streamReader;
	private File inFile;
	
	public void actionPerformed(ActionEvent ev) {
		
		ResourceBundle labels = ResourceBundle.getBundle("eu/wildweed/res/labels", CWTrainerApp.getCurrentLocale());
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(labels.getString("textonly"), "txt"));

		int returnVal = fileChooser.showOpenDialog(CWTrainerApp.getGUI().getMainWindow());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			inFile = fileChooser.getSelectedFile();	

			try {
				
				InputStream inStream = new FileInputStream(inFile);
				
				try {
					streamReader = new InputStreamReader(inStream, "UTF-8");
				} catch(UnsupportedEncodingException ex) {System.err.println("File read error");}
				
				reader = new BufferedReader(streamReader);
				
			} catch (FileNotFoundException ex) {System.err.println("File open error");}
		
			String line;
			String[] buf;
			ArrayList<String> list = new ArrayList<String>();
		
			try {
	
				while((line = reader.readLine()) != null){
				
					//Remove all non-letter characters
					buf = line.replaceAll("[^\\p{L} ]", "").toLowerCase().split("\\s+");
					
					for(int i = 0; i < buf.length; i++) {
						if(!buf[i].equals(""))
							list.add(buf[i]);
					}

				}
				
				reader.close();
				
			} catch (IOException ex) {/*Nothing needs to be done here*/}
							
			//QueueManager is now ready to work
			CWTrainerApp.getQueueManager().setList(list);
			CWTrainerApp.getGUI().getStartButton().setEnabled(true);
			CWTrainerApp.getGUI().getCheck1().setEnabled(true);
			
		} //End if		
						
	} //End method
	
} //End class