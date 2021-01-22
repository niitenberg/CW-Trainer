package eu.wildweed;
import java.awt.*;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.util.ResourceBundle;
import java.util.Locale;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import eu.wildweed.*;


public class GUI {
	
	private JButton flag1, flag2, loadButton, startButton;
	private JCheckBox check1, check2, check3, check4, check5;
	private JLabel textAreaLabel, volumeLabel, speedLabel;
	private JFrame frame;
	private JTextArea textarea;
	private JList<String> wordlist;
	private JSpinner speed;
	
			
	public void build() {
	
		//Set system look 
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {/*Nothing needs to be done here*/}
		
	
		//West panel
		wordlist = new JList<String>();
		wordlist.setFixedCellWidth(20);
		wordlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		wordlist.addMouseListener(new WordListListener());
	
		JScrollPane scroller1 = new JScrollPane(wordlist);
		scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller1.setAlignmentX(Component.LEFT_ALIGNMENT);
		scroller1.setPreferredSize(new Dimension(120, 0));
		scroller1.setMaximumSize(new Dimension(120, 300));
		
		JPanel westpanel = new JPanel();
		westpanel.setLayout(new BoxLayout(westpanel, BoxLayout.PAGE_AXIS));
		westpanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		westpanel.add(scroller1);
		

		check1 = new JCheckBox();
		check1.setAlignmentX(Component.LEFT_ALIGNMENT);
		check1.setEnabled(false);
		
		check1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if(ev.getStateChange() == ItemEvent.SELECTED)
					CWTrainerApp.getQueueManager().removeDuplicates(true);
				else
					CWTrainerApp.getQueueManager().removeDuplicates(false);
            }
        });
		
		westpanel.add(check1);
		
		
		check2 = new JCheckBox();
		check2.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		check2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if(ev.getStateChange() == ItemEvent.SELECTED)
					CWTrainerApp.getQueueManager().randomOrder(true);
				else
					CWTrainerApp.getQueueManager().randomOrder(false);
            }
        });
		
		westpanel.add(check2);
		
		
		check3 = new JCheckBox();
		check3.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		check3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if(ev.getStateChange() == ItemEvent.SELECTED) {
					
					if(CWTrainerApp.getQueueManager().isWorking())
						NoiseGenerator.play();
					CWTrainerApp.getQueueManager().addNoise(true);
					
				} else {
					NoiseGenerator.stop();
					CWTrainerApp.getQueueManager().addNoise(false);
				}
					
            }
        });
		
		westpanel.add(check3);
		
		
		check4 = new JCheckBox();
		check4.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		check4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if(ev.getStateChange() == ItemEvent.SELECTED)
					CWTrainerApp.getQueueManager().endlessLoop(true);
				else
					CWTrainerApp.getQueueManager().endlessLoop(false);
            }
        });
		
		westpanel.add(check4);
		
		
		check5 = new JCheckBox();
		check5.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		check5.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                if(ev.getStateChange() == ItemEvent.SELECTED)
					CWTrainerApp.getQueueManager().repeatPattern(true);
				else
					CWTrainerApp.getQueueManager().repeatPattern(false);
            }
        });
		
		westpanel.add(check5);

		
		//South panel
		JPanel southpanel = new JPanel();
		
		loadButton = new JButton();
		loadButton.addActionListener(new LoadButtonListener());
		
		startButton = new JButton();
		startButton.setEnabled(false);
		startButton.addActionListener(new StartButtonListener());
		
		southpanel.add(BorderLayout.SOUTH, loadButton);
		southpanel.add(BorderLayout.SOUTH, startButton);
	
		
		//Language flags component panel
		Box east1 = Box.createHorizontalBox();
		
		java.net.URL imageURL_1 = CWTrainerApp.class.getResource("res/ee.png");
		flag1 = new JButton(new ImageIcon(imageURL_1));
		
		flag1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setLabels(new Locale("et", "EE"));
			}
		});
	
		java.net.URL imageURL_2 = CWTrainerApp.class.getResource("res/gb.png");	
		flag2 = new JButton(new ImageIcon(imageURL_2));
		
		flag2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				setLabels(Locale.ENGLISH);
			}
		});
	
		east1.add(Box.createHorizontalGlue());
		east1.add(flag1);
		east1.add(Box.createRigidArea(new Dimension(5, 0)));
		east1.add(flag2);
		
		
		//Drawing component panel
		DrawPanel east2 = new DrawPanel();
		east2.setPreferredSize(new Dimension(240, 90));
		CWPlayer.addCWListener(east2);
		
		
		//Text area component panel
		textarea = new JTextArea();
		textarea.setLineWrap(true);
		textarea.addKeyListener(new TextAreaListener());
		
		JScrollPane scroller2 = new JScrollPane(textarea);
		scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller2.setPreferredSize(new Dimension(150, 80));
		scroller2.setMaximumSize(new Dimension(150, 80));
		
		Box east3 = Box.createHorizontalBox();
		east3.add(Box.createHorizontalGlue());
		east3.add(scroller2);
		
		
		//Spinner component panels
		Box east4 = Box.createHorizontalBox();
		east4.add(Box.createHorizontalGlue());
		volumeLabel = new JLabel();
		east4.add(volumeLabel);
		east4.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//Volume spinner
		JSpinner volume = new JSpinner(new SpinnerNumberModel(
		50,		//Default
		10,		//Min
		100,	//Max
		10));	//Step
		
		volume.setPreferredSize(new Dimension(50, 20));
		volume.setMaximumSize(new Dimension(80, 20));
		volume.addChangeListener(new VolumeSpinnerListener());
		volume.setValue(CWTrainerApp.defaultVolume);
		east4.add(volume);
		
		Box east5 = Box.createHorizontalBox();
		east5.add(Box.createHorizontalGlue());
		speedLabel = new JLabel();
		east5.add(speedLabel);
		east5.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//Speed spinner
		String[] speedlist = {"13", "15", "18", "20", "22", "25", "30", "35", "40", "45", "50", "60"};
		speed = new JSpinner(new SpinnerListModel(speedlist));
		speed.setPreferredSize(new Dimension(50, 20));
		speed.setMaximumSize(new Dimension(80, 20));
		speed.addChangeListener(new SpeedSpinnerListener());
		speed.setValue(new String("" + CWTrainerApp.defaultSpeed));
		east5.add(speed);
		
		
		//Main East panel
		JPanel eastpanel = new JPanel();
		eastpanel.setLayout(new BoxLayout(eastpanel, BoxLayout.PAGE_AXIS));
		eastpanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		eastpanel.add(east1);
		eastpanel.add(east2);
		eastpanel.add(Box.createVerticalGlue());
		textAreaLabel = new JLabel();
		eastpanel.add(textAreaLabel);
		eastpanel.add(east3);
		eastpanel.add(Box.createRigidArea(new Dimension(0, 20)));
		eastpanel.add(east4);
		eastpanel.add(Box.createRigidArea(new Dimension(0,10)));
		eastpanel.add(east5);
		
	
		
		//Frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.getContentPane().add(BorderLayout.WEST, westpanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southpanel);
		frame.getContentPane().add(BorderLayout.EAST, eastpanel);
		
		frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent ev)
            {
				CWPlayer.close();
                ev.getWindow().dispose();
            }
        });
		
			
		//Button labels in current locale. This uses the keyboard input language settings for
		//detecting user's preferred language (defaultLocale method does not give reliable results)		
		setLabels(InputContext.getInstance().getLocale());
		CWTrainerApp.setCurrentLocale(InputContext.getInstance().getLocale());
		
		frame.setVisible(true);
		frame.setResizable(false);
		
	} //End method


	
	void setLabels(Locale locale) {
	
		ResourceBundle labels = ResourceBundle.getBundle("eu/wildweed/res/labels", locale);
		CWTrainerApp.setCurrentLocale(locale);
		
		wordlist.setToolTipText(labels.getString("wordlisthelp"));
	
		check1.setText(labels.getString("removeduplicates"));
		check1.setToolTipText(labels.getString("removeduplicateshelp"));
		
		check2.setText(labels.getString("randomorder"));
		check2.setToolTipText(labels.getString("randomorderhelp"));
		
		check3.setText(labels.getString("addnoise"));
		check3.setToolTipText(labels.getString("addnoisehelp"));
		
		check4.setText(labels.getString("infiniteloop"));
		check4.setToolTipText(labels.getString("infiniteloophelp"));
		
		check5.setText(labels.getString("repeatpattern"));
		check5.setToolTipText(labels.getString("repeatpatternhelp"));
		
		loadButton.setText(labels.getString("load"));
		loadButton.setToolTipText(labels.getString("loadhelp"));
		
		if(CWTrainerApp.getQueueManager().isWorking())
			startButton.setText(labels.getString("stop"));
		else
			startButton.setText(labels.getString("start"));
		
		textAreaLabel.setText(labels.getString("typetext"));
		textarea.setToolTipText(labels.getString("typetexthelp"));
		
		speedLabel.setText(labels.getString("speed"));
		speed.setToolTipText(labels.getString("speedhelp"));
		
		volumeLabel.setText(labels.getString("volume"));
		
		frame.setTitle(labels.getString("title"));
		
	} //End method
	
	JFrame getMainWindow() {
		return frame;
	}
	
	JTextArea getTextArea() {
		return textarea; 
	}
	
	JButton getStartButton() {
		return startButton;
	}
	
	JButton getLoadButton() {
		return loadButton;
	}
	
	JCheckBox getCheck1() {
		return check1;
	}
	
		JCheckBox getCheck3() {
		return check3;
	}
	
	JList<String> getWordList() {
		return wordlist;
	}
	
} //End Class





