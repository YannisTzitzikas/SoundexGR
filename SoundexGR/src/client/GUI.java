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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import SoundexGR.SoundexGRExtra;
import SoundexGR.SoundexGRSimple;

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
		
		// SOUNDEX EXTRA
		if (event.getSource()==Dashboard.soundexB ) {
			System.out.println("SoundexGR Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" | " + 
						SoundexGRExtra.encode(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
		}
		
		
		// SOUNDEX Naive
		if (event.getSource()==Dashboard.soundexNaiveB) {
			System.out.println("SoundexGR Naive Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" | " + 
						SoundexGRSimple.encode(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
		}
		
		// Phonetic
		if (event.getSource()==Dashboard.pnoneticB) {
			System.out.println("Phonetic Transcription  Pressed");
			ArrayList<String> tokens = 	Tokenizer.getTokens(
							Dashboard.textInputArea.getText()
			);
			String outputStr="";
			for (String token: tokens) {	
				
				outputStr = outputStr.concat(" | " + 
						SoundexGRExtra.phoneticTrascription(token)
						)  ;
			}
			Dashboard.textOutputArea.setText(outputStr);
		}
		
		//applyAllB
		if (event.getSource()==Dashboard.applyAllB) {
			System.out.println("Apply all pressed");
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
		}
	} // actionPerformed
}

class Dashboard  extends JFrame {
	AppController apCtlr; 
	
	static JTextArea textInputArea;
    static JTextArea textOutputArea;
	
    static JButton clearB;
    static JButton soundexB;
    static JButton soundexNaiveB;
    static JButton pnoneticB;
    static JButton applyAllB;
    
    
    
    
    /**
     * Constructor. Takes an input the object that will act as actionlistener
     * @param ac
     */
	Dashboard(AppController ac) {
		apCtlr=ac; // controller
		
		// ICON 
		ImageIcon icon;
		String path = "Image.png";
		java.net.URL imgURL = getClass().getResource(path);	
		if (imgURL != null) {
			icon = new ImageIcon(imgURL, "Eikonidio");
			this.setIconImage(icon.getImage());
		} else {
			System.err.println("Couldn't find file: " + path);
		}
		
		//Create and set up the Menu
        //AppMenu appMenu = new AppMenu(this);
        //this.setJMenuBar(appMenu.createMenuBar());
        
		
		// GUI PART: size, layout, title
		setBounds(100,100,900,850);  //x,  y,  width,  height)
		setLayout(new GridLayout(0,1,5,5)); // rows, columns, int hgap, int vgap)
		setVisible(true);
		setTitle(GUI.appName);
		
		// GUI PART:  calling the methods that create buttons each in one separate panel
		createInput();
		createOperators();
		createOutput();
		
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
	void createInput() {
		
		JPanel inputPanel = new JPanel(new GridLayout(1,1,5,1)); // rows, columns, int hgap, int vgap)
		inputPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Input"));
		
		// A: INPUT TEXT AREA
		//JTextArea 
		textInputArea = new JTextArea(
			    //"Welcome! Write whatever you want here."
				GUI.exampleInputString
			);
		//textInputArea.setFont(new Font("Courier", NORMAL, 22));  // Font.ITALIC    Courier Serif
		textInputArea.setFont(new Font("monospaced", Font.BOLD, 22));
		
		textInputArea.setLineWrap(true);
		textInputArea.setWrapStyleWord(true);
		
		JScrollPane areaScrollPane = new JScrollPane(textInputArea);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(50, 30));
		
		inputPanel.add(areaScrollPane); 
		
		add(inputPanel); // adds to Frame
	}
	
	
	/**
	 * Create gui elements for the output
	 */
	void createOutput() {
		JPanel outputPanel = new JPanel(new GridLayout(1,1,5,5)); // rows, columns, int hgap, int vgap)
		outputPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "Output"));
		
		// A: OUTPUT TEXT AREA
		//JTextArea 
		textOutputArea = new JTextArea(
			    "output"
			);
		//textOutputArea.setFont(new Font("Courier", NORMAL, 22));  //
		textOutputArea.setFont(new Font("monospaced", Font.BOLD, 22));
		textOutputArea.setLineWrap(true);
		textOutputArea.setWrapStyleWord(true);
		
		JScrollPane areaScrollPane = new JScrollPane(textOutputArea);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(50, 30));
		
		outputPanel.add(areaScrollPane); 
		add(outputPanel); // adds to Frame
	} // create output
	
	
	/**
	 * Creates the buttons for the operators
	 */
	void createOperators() {
		JPanel operatorPanel = new JPanel(new GridLayout(1,0,5,5)); // rows, columns, int hgap, int vgap)
		operatorPanel.setBorder(BorderFactory.createTitledBorder(
		        BorderFactory.createEtchedBorder(), "General Operations"));
		
		soundexB = 			new JButton("SoundexGR"); //soundex
		soundexNaiveB = 	new JButton("SoundexGRNaive"); //soundexNaive
	    pnoneticB = 		new JButton("Phonetic"); //phonetic
	    applyAllB = 		new JButton("APPLY ALL"); //apply all
	    clearB = 			new JButton("Clear"); //clear
		
		// Array with all buttons
		JButton[] allButtons = {
				soundexB,
				soundexNaiveB,
				pnoneticB,
				applyAllB,
				clearB,
	    };
		
		// tooltips
		clearB.setToolTipText("Clears the input area");
		soundexB.setToolTipText("Aplies SoundexGR to each token");
		soundexNaiveB.setToolTipText("Aplies SoundexGRNaive to each token");
		pnoneticB.setToolTipText("Aplies Phonetic Transcription to each token");
		applyAllB.setToolTipText("Aplies all");
	    
		// Add Buttons to Panel and sets action listeners
		for (JButton b: allButtons) {
			operatorPanel.add(b); // adds button to the panel
			b.addActionListener(this.apCtlr); // sets the action listener
		}
		// adds the panel to Frame
		
		//operatorPanel.setSize(200, 100); // lala
		add(operatorPanel); 
	}			
}

public class GUI {
	static String appName = "SoundexGR v0.1";
	static String exampleInputString =
			"αυγό  αβγό "
			+ "θαύμα θάβμα θαυμαστικό "  
			+ "ξέρω  κσαίρο " 
			+ "αύξων άφξον " 
			+  "εύδοξος εβδοξος "
			+ "έτοιμος έτιμος έτημος έτυμος έτιμως αίτημος "
			 ;
	static String aboutMsg = "About ... ";
			//FileReadingUtils.readFileAsString("README.txt");
	
	public static void main(String[] args) {
		System.out.println(appName);
		AppController ac = new AppController(); // controller
		Dashboard d = new Dashboard(ac);  // gui taking the controller
	}
}

