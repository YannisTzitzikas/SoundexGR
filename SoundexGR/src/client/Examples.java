/**
 * 
 */
package client;

import SoundexGR.SoundexGRExtra;
import SoundexGR.SoundexGRSimple;


/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */

/*
 * This class provides some examples of SoundexGR
 */
public class Examples {
	
	/**
	 * Replaces some special characters for being readable by latex
	 * @param w
	 * @return
	 */
	static public String toLatex(String w) {
		return w.replace("$","\\$")
				.replace("@","$@$")
				.replace("&","\\&")
				.replace("^","$\\wedge$")
				  ;
		}
	
	public static void main(String[] lala) {
		System.out.println("**SoundexGR Examples**");
		String[] testCases = {
				"Θάλασσα","θάλλασα","θάλασα", "θαλασών",
				"μήνυμα","μύνημα","μίνιμα","μοίνειμα",
				"έτοιμος", "έτιμος", "έτημος", "έτυμος", "έτιμως", "αίτημος",
				"αυγό","αβγό","αυγολάκια","αβγά", "αυγά",
				"τζατζίκι", "τσατζίκι","τσατσίκι",
				"μπαίνω","μπένω",
				"ξέρω","κσαίρο",
				"αύξων","άφξον",
				"εύδοξος","εβδοξος",
				"κορονοιός", "κοροναιός",
				"οβελίας", "ωβελύας","οβελίσκος",
				"Βαγγέλης","Βαγκέλης","Βαγκαίλης",
				"Γιάννης", "Γιάνης", "Γιάνννης",
				"αναδιατάσσω", "αναδιέταξα",	
				"θαύμα", "θάβμα", "θαυμαστικό"
		};
		
		System.out.println("\n       word  --> SGRSimp | SGRExtra"); // for including it in latex 
		for (String w: testCases)  {
			System.out.printf("%12s --> %6s  |  %s \n", w, SoundexGRSimple.encode(w), SoundexGRExtra.encode(w));
		}
		
		
		System.out.println("\n       word  --> SGRSimp | SGRExtra (for latex)"); // for including it in latex 
		for (String w: testCases)  {
			System.out.printf("\\GreekToEnglish{%s}  & $\\rightarrow$ &\\GreekToEnglish{%s} &\\GreekToEnglish{%s}\\\\\n", w, 
						toLatex(SoundexGRSimple.encode(w)), 
						toLatex(SoundexGRExtra.encode(w)));			
		}	
		
	}
}
