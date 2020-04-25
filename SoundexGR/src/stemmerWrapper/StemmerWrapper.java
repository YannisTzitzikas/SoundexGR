/**
 * 
 */
package stemmerWrapper;

import mitos.stemmer.Stemmer;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */
public class StemmerWrapper {
	
	public String getStemOf(String w) {
		return Stemmer.Stem(w);
	}
	
	public StemmerWrapper () {
		Stemmer.Initialize();
	}
	
	
	public static void main(String[] lala) {
		
		System.out.println("**Greek Stemmer (from mitos) **");
		String[] testCases = {
				"αυτοκίνητο", "αυτοκινήτων", 
				"καρπούζι", "καρπούζια", "καρπουζιών",
				"καρέκλα","καρέκλας","καρέκλες","καρεκλών",
				"υπολογισμός", "υπολογίσιμος", "υπολογίζω",
				"πράττω", "πρακτικός", "πράξη", "πράγμα",
				"αναδιάταξη", "αναδιατάσσω", "αναδιέταξα", 
				"κριτήριο", "κριτήρια", "κρητηριων",
				"προβολέας", "προβολείς",
				"αμινοξύ", "αμινοξέα",
				"πάω", "πηγαίνω", "πηγαίνεις", "πήγαμε",
				"Αθήνα", "Αθηναίος", "Αθηναία", "Αθηναϊκό",
				"Ηράκλειο", "Ηρακλείου", 
				"Γιάννης", "Γιάννη",
				"Μαρία", "Μαριών",
				"Μύρων", "Μύρωνας", "Μύρωνα"
		};
		Stemmer.Initialize(); // not needed every time (only once)
		for (String w: testCases)  {
			System.out.printf("%12s --> %s\n", w, Stemmer.Stem(w));
		}
	}

}
