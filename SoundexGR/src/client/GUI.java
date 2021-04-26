/**
 * 
 */
package client;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import SoundexGR.SoundexGRExtra;
import SoundexGR.SoundexGRSimple;
import evaluation.DictionaryBasedMeasurements;
import evaluation.DictionaryMatcher;

//import filesMgmt.FileReadingUtils;



/**
 * AppController: The controller of the graphical add
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */

class AppController implements ActionListener {

	/**
	 * The method where all GUI actions are sent
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		
		// Clear BUTTON
		if (event.getSource()==Dashboard.clearB ) {
			//System.out.println("Clear Pressed");
			if (Dashboard.textInputArea.getText().equals(""))
			   Dashboard.textInputArea.setText(GUI.exampleInputString);
			else 
				 Dashboard.textInputArea.setText("");
		}
		
		// Set Output as Input Clear BUTTON
		if (event.getSource()==Dashboard.swapB ) {
			Dashboard.textInputArea.setText(Dashboard.textOutputArea.getText());
		}
				
		// Code Length
		if (event.getSource()==Dashboard.codeLenghtsC ) {
			int lenBefore =  Dashboard.getAppSoundexCodeLen();
			String selected = (String)Dashboard.codeLenghtsC.getSelectedItem();
			int selectedInt= new Integer(selected);
			Dashboard.setAppSoundexCodeLen(selectedInt);
			System.out.printf("Code length changed from %d to %d.", lenBefore,Dashboard.getAppSoundexCodeLen());
		}
		
		// SOUNDEX EXTRA
		if (event.getSource()==Dashboard.soundexB ) {
			//System.out.println("SoundexGR Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" " + 
						SoundexGRExtra.encode(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
		
		
		// SOUNDEX Naive
		if (event.getSource()==Dashboard.soundexNaiveB) {
			//System.out.println("SoundexGR Naive Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" " + 
						SoundexGRSimple.encode(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
		
		// Phonetic
		if (event.getSource()==Dashboard.pnoneticB) {
			//System.out.println("Phonetic Transcription  Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" " + 
						SoundexGRExtra.phoneticTrascription(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
		
		//applyAllB
		if (event.getSource()==Dashboard.applyAllB) {
			//System.out.println("Apply all pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String strFormat = "%14s ->  %s   %s  %s";
			
			String outputStr= String.format(strFormat, "word", "SGR", "SGRNv", "Phonetic");				
			for (String token: tokens) {	
				String output = String.format(strFormat, 
							token, 
							SoundexGRExtra.encode(token),
							SoundexGRSimple.encode(token),
							SoundexGRExtra.phoneticTrascription(token)
							);
				outputStr = outputStr.concat("\n" + output);
			}
			Dashboard.textOutputArea.setText(outputStr);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
		
		//produceErrosB
		if (event.getSource()==Dashboard.produceErrosB) {
			//System.out.println("Misspellings");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			
			String output="";
			for (String token: tokens) {	
				output += token + ":";
				for (String errorStr: DictionaryBasedMeasurements.returnVariations(token)) {
					output+= " " + errorStr;
					//System.out.println(output);
				}
				output+="\n";
			}
			Dashboard.textOutputArea.setText(output);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
			
		// dictionarylookup
		if (event.getSource()==Dashboard.dictionaryLookupB) {
			//System.out.println("Disctionary Lookup");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String output="";
			for (String token: tokens) {	
				//output += token + ":";
				output+=DictionaryMatcher.getMatchings(token, Dashboard.getAppSoundexCodeLen()) + "\n";
			}
			Dashboard.textOutputArea.setText(output);
			Dashboard.textOutputArea.setCaretPosition(0);
		}
		
	} // actionPerformed
}

class Dashboard  extends JFrame {
	AppController apCtlr; 
	
	static JTextArea textInputArea;
    static JTextArea textOutputArea;
    
    static Dimension textAreaDimension = new Dimension(50, 10);
	
    
    static JButton soundexB;
    static JButton soundexNaiveB;
    static JButton pnoneticB;
    static JButton applyAllB;
    static JButton dictionaryLookupB;
    static JButton produceErrosB;
    
    static JLabel  codeLengthB;
    static JComboBox codeLenghtsC;
    
    
    static JButton clearB;
    static JButton swapB;
    
    static Font appTextfont =    new Font("monospaced", Font.BOLD, 18);  //  Font.PLAIN Font.BOLD
    static Font appButtonfont =    new Font("serif", Font.PLAIN, 18);
    
    private static int   appSoundexCodeLen = 6 ; 
    
    /**
	 * @return the appSoundexCodeLen
	 */
	static int getAppSoundexCodeLen() {
		return appSoundexCodeLen;
	}
	/**
	 * @param appSoundexCodeLen the appSoundexCodeLen to set
	 */
	static void setAppSoundexCodeLen(int newLen) {
		Dashboard.appSoundexCodeLen = newLen;
		SoundexGRExtra.LengthEncoding=newLen;
		SoundexGRSimple.LengthEncoding=newLen;
		loadOrRefreshDictionary();
	}
     
	static void loadOrRefreshDictionary() {
		//Loading the dictionary (one getMatching initiates its loading)
		new Thread( () -> 	{DictionaryMatcher.getMatchings("αυγόβββ", Dashboard.getAppSoundexCodeLen());}).start();
		System.out.println("Dictionary loaded/refreshed.");
	}
    
    /**
     * Constructor. Takes an input the object that will act as actionlistener
     * @param ac
     */
	Dashboard(AppController ac) {
		apCtlr=ac; // controller
		setAppSoundexCodeLen(7);   // it also loads/refreshes the dictionary
		//loadOrRefreshDictionary(); // not needed
		
		
		// ICON 
		try {
			ImageIcon icon;
			String path = "/icon.png";  // works "/icon.png"
			System.out.println(getClass().getResource(path)); // "/"
			java.net.URL imgURL = getClass().getResource(path);	
			if (imgURL != null) {
				System.out.println(">>>"+imgURL);
				icon = new ImageIcon(imgURL, "Eikonidio");
				this.setIconImage(icon.getImage());
			} else {
				System.err.println("Couldn't find the icon file: " + path);
			}
		} catch (Exception e ) {System.out.println(e);}
		
		//Create and set up the Menu
        //AppMenu appMenu = new AppMenu(this);
        //this.setJMenuBar(appMenu.createMenuBar());
       
		// GUI PART: size, layout, title
		setBounds(80,80,1200,550);  //x,  y,  width,  height)
		setLayout(new GridLayout(0,2,5,5)); // rows, columns, int hgap, int vgap)
		//setLayout(new FlowLayout()); // rows, columns, int hgap, int vgap)
		
		//this.getContentPane().setBackground(ColorMgr.colorBackground);
		//this.getContentPane().setBackground(Color.BLUE);
		setVisible(true);
		setTitle(GUI.appName);
		
		// GUI PART:  calling the methods that create buttons each in one separate panel
		
		//PANEL FOR ALL USER INPUT (TEXT AREA AND "TOOLBARS")
		JPanel generalInputPanel = new JPanel(new GridLayout(0,1,5,1)); // rows, columns, int hgap, int vgap)
		generalInputPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "User Input & Tool Bars"));
		add(generalInputPanel);
		
		createInput(generalInputPanel); 
		createPhonemicOperators(generalInputPanel);
		createMatchingOperators(generalInputPanel);
		createGeneralOperators(generalInputPanel);
		createOutput(null);
		
		setVisible(true);
		
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
	}
	/**
	 * Create gui elements for the input
	 */
	void createInput(JPanel parentPanel) {
		JPanel userInputPanel = new JPanel(new GridLayout(1,1,5,1)); // rows, columns, int hgap, int vgap)
		userInputPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Input"));
		// A: INPUT TEXT AREA
		//JTextArea 
		textInputArea = new JTextArea(
			    //"Welcome! Write whatever you want here."
				GUI.exampleInputString
			);
		//textInputArea.setFont(new Font("Courier", NORMAL, 22));  // Font.ITALIC    Courier Serif
		textInputArea.setFont(appTextfont);
		
		textInputArea.setLineWrap(true);
		textInputArea.setWrapStyleWord(true);
		
		JScrollPane areaScrollPane = new JScrollPane(textInputArea);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(textAreaDimension);
		
		userInputPanel.add(areaScrollPane); 
		
		if (parentPanel==null) { // if no parent panel
			this.add(userInputPanel); // adds to Frame
		} else {
			parentPanel.add(userInputPanel);
		}
	}
	
	
	/**
	 * Create gui elements for the output
	 */
	void createOutput(JPanel parentPanel) {
		JPanel outputPanel = new JPanel(new GridLayout(1,1,5,5)); // rows, columns, int hgap, int vgap)
		outputPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Output"));
		
		// A: OUTPUT TEXT AREA
		//JTextArea 
		textOutputArea = new JTextArea(
			    "output"
			);
		//textOutputArea.setFont(new Font("Courier", NORMAL, 22));  //
		textOutputArea.setFont(appTextfont);
		textOutputArea.setLineWrap(true);
		textOutputArea.setWrapStyleWord(true);
	
		
		JScrollPane areaScrollPane = new JScrollPane(textOutputArea);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(textAreaDimension);
		
		outputPanel.add(areaScrollPane); 
		//add(outputPanel); // adds to Frame
		if (parentPanel==null) { // if no parent panel
			this.add(outputPanel); // adds to Frame
		} else {
			parentPanel.add(outputPanel);
		}
		
	} // create output
	
	
	
	/**
	 * Creates the buttons for the general operators
	 */
	
	
	void createGeneralOperators(JPanel parentPanel) {
		//JPanel generalOperatorPanel = new JPanel(new GridLayout(1,0,5,5)); // rows, columns, int hgap, int vgap)
		JPanel generalOperatorPanel = new JPanel(new FlowLayout()); // rows, columns, int hgap, int vgap)
		generalOperatorPanel.setBackground(ColorMgr.colorBackground);
		
		generalOperatorPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "General Operations"));
		
		
		clearB = 			new JButton("Clear"); //clear
		swapB = 			new JButton("Set Output as Input"); //clear
		
		clearB.setBackground(Color.LIGHT_GRAY);
		swapB.setBackground(Color.LIGHT_GRAY);
		
		// Array with all buttons
		JButton[] allButtons = {
				clearB,
				swapB
	    };
				
		// tooltips
		clearB.setToolTipText("Clears the input area");
		swapB.setToolTipText("Set Output as Input");
		
		// Add Buttons to Panel and sets action listeners
		for (JButton b: allButtons) {
			generalOperatorPanel.add(b); // adds button to the panel
			b.setFont(appButtonfont);
			b.addActionListener(this.apCtlr); // sets the action listener
			b.setBackground(Color.white);
		}
		// adds the panel to Frame
		
		//operatorPanel.setSize(200, 100); // lala
		//add(generalOperatorPanel); 
		

		if (parentPanel==null) { // if no parent panel
			this.add(generalOperatorPanel); // adds to Frame
		} else {
			parentPanel.add(generalOperatorPanel);
		}
		
	}			
	
	
	
	void createMatchingOperators(JPanel parentPanel) {
		//JPanel matchingOperatorPanel = new JPanel(new GridLayout(1,0,5,5)); // rows, columns, int hgap, int vgap)
		JPanel matchingOperatorPanel = new JPanel(new FlowLayout());
		//JPanel matchingOperatorPanel = new FlowLayout();
		matchingOperatorPanel.setBackground(ColorMgr.colorBackground);
		
		matchingOperatorPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Misspelling and Matching Operations"));
		
		dictionaryLookupB=		new JButton("Dictionary lookup");		
		produceErrosB=		new JButton("Produce Misspellings");	
				
		// Array with all buttons
		JButton[] allButtons = {
				produceErrosB,
				dictionaryLookupB,
	    };
				
	
	    //tooltips
	    dictionaryLookupB.setToolTipText("Lookup the word(s) in the dictionary (it it does not exist it returns approximate matches)");
		produceErrosB.setToolTipText("Produces various misspellings of the given words");
		
		
		// Add Buttons to Panel and sets action listeners
		for (JButton b: allButtons) {
			matchingOperatorPanel.add(b); // adds button to the panel
			b.addActionListener(this.apCtlr); // sets the action listener
			b.setFont(appButtonfont);
			b.setBackground(ColorMgr.colorButtonMatch);
		}
		// adds the panel to Frame
		
		//operatorPanel.setSize(200, 100); // lala
		//add(matchingOperatorPanel); 

		if (parentPanel==null) { // if no parent panel
			this.add(matchingOperatorPanel); // adds to Frame
		} else {
			parentPanel.add(matchingOperatorPanel);
		}
	}	

	
	/**
	 * Creates the buttons for the phonemic operators
	 */
	void createPhonemicOperators(JPanel parentPanel) {
		//JPanel operatorPanel = new JPanel(new GridLayout(1,0,5,5)); // rows, columns, int hgap, int vgap)
		JPanel operatorPanel = new JPanel(new FlowLayout()); // rows, columns, int hgap, int vgap)
		operatorPanel.setBackground(ColorMgr.colorBackground);
		
		operatorPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Phonemic Operations"));
		
		
		codeLengthB=			new JLabel("Code length:");
		soundexB = 			new JButton("SoundexGR"); //soundex
		soundexNaiveB = 	new JButton("SoundexGRNaive"); //soundexNaive
	    pnoneticB = 		new JButton("Phonemic"); //phonetic
	    applyAllB = 		new JButton("APPLY ALL"); //apply all
	      
	    codeLenghtsC = new JComboBox(new String[]{"3","4","5","6","7","8","9","10","11","12","13","14","15","16"});
	    codeLenghtsC.setSelectedIndex(4); //lola  7 
	    codeLenghtsC.addActionListener(this.apCtlr);
	 	    
	    // Array with all buttons
	    JButton[] allButtons = {
				//codeLengthB,
				soundexB,
				soundexNaiveB,
				pnoneticB,
				applyAllB,
	    };
		
		// tooltips
		soundexB.setToolTipText("Applies SoundexGR to each input word");
		soundexNaiveB.setToolTipText("Applies SoundexGRNaive to each input word");
		pnoneticB.setToolTipText("Applies Phonetic Transcription to each input word");
		applyAllB.setToolTipText("Applies all to each input word");
			    
		// Add Buttons to Panel and sets action listeners
		for (JButton b: allButtons) {
			operatorPanel.add(b); // adds button to the panel
			b.addActionListener(this.apCtlr); // sets the action listener
			b.setFont(appButtonfont);
			b.setBackground(ColorMgr.colorButtonPhone);
		}
		codeLengthB.setFont(appButtonfont);
		codeLenghtsC.setFont(appButtonfont);
		//add the rest
		operatorPanel.add(codeLengthB);
		operatorPanel.add(codeLenghtsC);
		// adds the panel to Frame
		
		//operatorPanel.setSize(200, 100); // lala
		//add(operatorPanel); 

		if (parentPanel==null) { // if no parent panel
			this.add(operatorPanel); // adds to Frame
		} else {
			parentPanel.add(operatorPanel);
		}
	}
				
}

public class GUI {
	static String appName = "SoundexGR v0.2";
	static String exampleInputString =
			"αυγό  αβγό "
			+ "θαύμα θάβμα θαυμαστικό "  
			+ "ξέρω  κσαίρο " 
			+ "αύξων άφξον " 
			+  "εύδοξος εβδοξος "
			+ "έτοιμος έτιμος έτημος έτυμος έτιμως αίτημος "
			+ "μήνυμα μύνοιμα"
			 ;
	static String aboutMsg = "About ... ";
			//FileReadingUtils.readFileAsString("README.txt");
	
	public static void main(String[] args) {
		System.out.println(appName);
		AppController ac = new AppController(); // controller
		Dashboard d = new Dashboard(ac);  // gui taking the controller
	}
}


class ColorMgr {
	static Color  colorBackground = Color.white;
	static Color  colorButtonPhone = new Color(204,255,255);
	static Color  colorButtonMatch = new Color(255,255,204);
	
}
