package SoundexGR;

/**
 * @author: Antrei Kavros
 */

public class SoundexGRSimple {
	
	public static int LengthEncoding = 4; // the length of the code to be produced (default = 4)

    public static String encode(String word) {
        word = word.toLowerCase();

        char[] givenWord = word.toCharArray();
        char[] res = new char[givenWord.length];

        int i = 1;
        
        res[0]=word.charAt(0);
        givenWord = word.substring(i).toCharArray();
        for (char c : givenWord) {
            switch (c) {
                case 'β':
                case 'b':
                case 'φ':
                case 'π':

                    res[i] = '1';
                    break;
                case 'γ':
                case 'χ':
                    res[i] = '2';
                    break;
                case 'δ':
                case 'τ':
                case 'd':
                case 'θ':
                    res[i] = '3';
                    break;
                case 'ζ':
                case 'σ':
                case 'ς':
                case 'c':
                case 'ξ':
                case 'ψ':
                    res[i] = '4';
                    break;
                case 'κ':
                case 'g':
                    res[i] = '6';
                    break;
                case 'λ':
                    res[i] = '7';
                    break;
                case 'μ':
                case 'ν':
                    res[i] = '8';
                    break;
                case 'ρ':
                    res[i] = '!';
                    break;
                default:
                    res[i] = '0';
                    break;
            }
            i++;
        }
        String finalResult = "";
        finalResult += res[0];
        for (i = 1; i < res.length; i++) {
            if (res[i] != '0') {
                if (res[i] != res[i - 1]) {
                    finalResult += res[i];
                }
            }

        }
        //finalResult += "00000000";
        finalResult += "00000000000000000000000";
        
        return finalResult.substring(0, LengthEncoding);
    }
    
    
public static void main(String[] args) {
    	
    	String[] examples = {
    			"αυγό",  
    			"αβγό",
    			"εύδοξος",
    			"έβδοξος",
    			"ουουου",
    			"μπουμπούκι",
    			"ούλα",
    			"έμπειρος",  
    			"νούς",  
    			"ευάερος", 
    			"δίαλλειμα", 
    			"διάλυμα",
    			"αυλών", 			
    			"αυγουλάκια",
    			"τζατζίκι",
    			"τσιγκούνης",
    			"τσιγγούνης",
    			"εύδοξος",
    			"Γιάννης",
    			"Γιάνης",
    			"Μοίνοιματα",
    			"προύχοντας"
    	};
    	
    	//System.out.printf("%11s -> %s %s \n", "Word" , "SoundexGR" , "Phonetic Transcription");
    	LengthEncoding =4;
    	for (String word: examples) {
    		System.out.printf("%11s -> %s  \n", word, encode(word));
    	}
    	
    }
    
}
