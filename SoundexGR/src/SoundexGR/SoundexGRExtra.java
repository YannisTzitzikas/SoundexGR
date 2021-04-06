package SoundexGR;

/**
 * @author: Antrei Kavros and Yannis Tzitzikas
 */


public class SoundexGRExtra {
	
	public static int LengthEncoding = 4; // the length of the code to be produced (default = 4)
	
    /**
     * @param c, the character to be checked, if is in the "strong" (ηχηρά)
     * category of Greek Letters
     * @return true, if is in the category, false otherwise
     */
    private static boolean isStrong(char c) {
        // check if ηχηρό.
        boolean tmp;
        switch (c) {
            case 'γ':
            case 'β':
            case 'δ':
            case 'α': // χρειάζεται;
            case 'λ':
            case 'μ':
            case 'ν':
            case 'ρ':
            case 'ζ':     // ΥΤΖ (May 5, 2020)
                tmp = true;
                break;
            default:
                tmp = false;
                break;
        }
        return tmp;
    }

    /**
     * @param c, the character to be checked, if is in the "aecho" (αήχα)
     * category of Greek Letters
     * @return true, if is in the category, false otherwise
     */
    private static boolean isAecho(char c) {
        // check if αηχο
        boolean tmp;
        switch (c) {
            case 'π':
            case 'τ':
            case 'κ':
            case 'φ':
            case 'θ':
            case 'σ':
            case 'χ':
            case 'ξ':
                tmp = true;
                break;
            default:
                tmp = false;
                break;
        }
        return tmp;
    }

    /**
     *
     * @param c, the character to be checked, if it is a vowel.
     * @return true if @param is a vowel, false otherwise
     */
    private static boolean isVowel(char c) {
        // check if vowel
        boolean tmp;
        switch (c) {
            case 'ά':
            case 'α':
            case 'ε':
            case 'έ':
            case 'η':
            case 'ή':
            case 'ι':
            case 'ί':
            case 'ϊ':
            case 'ΐ':
            case 'ό':
            case 'ο':
            case 'υ':
            case 'ύ':
            case 'ϋ':
            case 'ΰ':
            case 'ω':
            case 'ώ':
                tmp = true;
                break;
            default:
                tmp = false;
                break;
        }
        return tmp;
    }

    /**
     *
     * @param c, the character to be checked if, it is Greek letters 'A' or 'E
     * @return true if is A or E
     */
    private static boolean isAorE(char c) {
        // check if word is α or e
        boolean tmp;
        switch (c) {
            // ά , έ are included if there is an error to the intonation
            case 'ά':
            case 'α':
            case 'ε':
            case 'έ':
                tmp = true;
                break;
            default:
                tmp = false;
                break;
        }
        return tmp;
    }

    /**
     *
     * @param word, a string to be trimmed from its suffix
     * @return the modified string if applicable, or the @param
     */
    private static String removeLast(String word) {
        //suffix removal
        if (word.length() > 2) {
            char[] tmp = word.toCharArray();

            int counter = 0;
            if (tmp[tmp.length - 1] != 'ν' || tmp[tmp.length - 1] != 'ς' || tmp[tmp.length - 1] != 'σ') {
                tmp[tmp.length - 1] = 'ο';
            }
            int i = tmp.length - 1;
            while (i >= 0 && isVowel(tmp[i])) {
                tmp[i] = 'o';
                counter++;
                i--;
            }
            counter--;
            return new String(tmp);
        } else {
            return word;
        }
    }

    /**
     *
     * @param word, a string to be trimmed from its suffix only letters 'σ', 'ς'
     * and 'ν'
     * @return the modified string if applicable, or the @param
     */
    private static String removeLastStrict(String word) {
        if (word.length() > 2) {
            char[] tmp = word.toCharArray();
            if (tmp[tmp.length - 1] == 'ν' || tmp[tmp.length - 1] == 'ς' || tmp[tmp.length - 1] == 'σ') {
                word = word.substring(0, word.length() - 1);
            }
        }
        return word;
    }

    /**
     *
     * @param word, a string to be modified, based on vowel digram combinations,
     * that when combined, produce a different sound.E.g 'αι' => 'ε'
     * @return the modified @param if applicable, or the original @param
     * otherwise
     */
    private static String groupVowels(String word) {
        // remove group vowels if necessary to single ones
        char[] tmp = word.toCharArray();
        int counter = 0;
        for (int i = 0; i < tmp.length; i++) {
            switch (tmp[i]) {
                case 'ό':
                    tmp[i] = 'ο';
                    break;
                case 'ο':
                    if (i + 1 < tmp.length) {
                        if (tmp[i + 1] == 'ι') {
                            tmp[i] = 'ι';
                            tmp[i + 1] = ' ';
                        }
                        if (tmp[i + 1] == 'ί') {
                            tmp[i] = 'ι';
                            tmp[i + 1] = ' ';
                        }
                        if (tmp[i + 1] == 'υ') {
                            tmp[i] = 'ο';
                            tmp[i + 1] = ' ';
                        }
                        if (tmp[i + 1] == 'ύ') {
                            tmp[i] = 'ο';
                            tmp[i + 1] = ' ';
                        }
                        
                    } else {
                        tmp[i] = 'ο';
                    }
                    break;
                case 'έ':
                    tmp[i] = 'ε';
                    break;
                case 'ε':
                    if (i + 1 < tmp.length) {
                        if (tmp[i + 1] == 'ι') {
                            tmp[i] = 'ι';
                            tmp[i + 1] = ' ';
                        }
                        if (tmp[i + 1] == 'ί') {
                            tmp[i] = 'ι';
                            tmp[i + 1] = ' ';
                        }
                    } else {
                        tmp[i] = 'ε';
                    }
                    break;
                case 'ά':
                    tmp[i] = 'α';
                    break;
                case 'α':
                    if (i + 1 < tmp.length) {
                        if (tmp[i + 1] == 'ι') {
                            tmp[i] = 'ε';
                            tmp[i + 1] = ' ';
                        }
                        if (tmp[i + 1] == 'ί') {
                            tmp[i] = 'ε';
                            tmp[i + 1] = ' ';
                        }
                    } else {
                        tmp[i] = 'α';
                    }
                    break;
                case 'ί':
                case 'ή':
                case 'ύ':
                case 'ι':
                case 'η':
                case 'υ':
                case 'ϋ':
                case 'ΰ':
                case 'ϊ':
                case 'ΐ':
                    tmp[i] = 'ι';
                    break;
                case 'ώ':
                    tmp[i] = 'ο';
                    break;
                case 'ω':
                    tmp[i] = 'ο';
                    break;
            }
        }

        for (int i = 0; i < tmp.length; i++) {

            if (tmp[i] == ' ') {
                if (i + 1 < tmp.length) {
                    tmp[i] = tmp[i + 1];
                    tmp[i + 1] = ' ';
                    counter++;
                }
            }
        }
        return new String(tmp);
    }

    
    
    /**
    * This methods is not used by  Soundex. 
    * It is used only for the production of the phonetic transcription.
    * @param word, a string to be modified, based on vowel digram combinations,
    * that when combined, produce a different sound.E.g 'αι' => 'ε' 
    * 
    * @return the modified @param if applicable, or the original @param
    * otherwise
    */
   private static String groupVowelsPhonetically(String word) {
       // remove group vowels if necessary to single ones
       char[] tmp = word.toCharArray();
       int counter = 0;
       for (int i = 0; i < tmp.length; i++) {
           switch (tmp[i]) {
               case 'ό':
                   tmp[i] = 'ο';
                   break;
               case 'ο':
                   if (i + 1 < tmp.length) {
                       if (tmp[i + 1] == 'ι') {
                           tmp[i] = 'ι';
                           tmp[i + 1] = ' ';
                       }
                       if (tmp[i + 1] == 'ί') {
                           tmp[i] = 'ι';
                           tmp[i + 1] = ' ';
                       }
                       if (tmp[i + 1] == 'υ') {
                           tmp[i] = 'u';                  // this is the difference with the SoundexGR
                           tmp[i + 1] = ' ';
                       }
                       if (tmp[i + 1] == 'ύ') {
                           tmp[i] = 'u';				// this is the difference with the SoundexGR
                           tmp[i + 1] = ' ';
                       }
                       
                   } else {
                       tmp[i] = 'ο';
                   }
                   break;
               case 'έ':
                   tmp[i] = 'ε';
                   break;
               case 'ε':
                   if (i + 1 < tmp.length) {
                       if (tmp[i + 1] == 'ι') {
                           tmp[i] = 'ι';
                           tmp[i + 1] = ' ';
                       }
                       if (tmp[i + 1] == 'ί') {
                           tmp[i] = 'ι';
                           tmp[i + 1] = ' ';
                       }
                   } else {
                       tmp[i] = 'ε';
                   }
                   break;
               case 'ά':
                   tmp[i] = 'α';
                   break;
               case 'α':
                   if (i + 1 < tmp.length) {
                       if (tmp[i + 1] == 'ι') {
                           tmp[i] = 'ε';
                           tmp[i + 1] = ' ';
                       }
                       if (tmp[i + 1] == 'ί') {
                           tmp[i] = 'ε';
                           tmp[i + 1] = ' ';
                       }
                   } else {
                       tmp[i] = 'α';
                   }
                   break;
               case 'ί':
               case 'ή':
               case 'ύ':
               case 'ι':
               case 'η':
               case 'υ':
               case 'ϋ':
               case 'ΰ':
               case 'ϊ':
               case 'ΐ':
                   tmp[i] = 'ι';
                   break;
               case 'ώ':
                   tmp[i] = 'ο';
                   break;
               case 'ω':
                   tmp[i] = 'ο';
                   break;
           }
       }

       for (int i = 0; i < tmp.length; i++) {

           if (tmp[i] == ' ') {
               if (i + 1 < tmp.length) {
                   tmp[i] = tmp[i + 1];
                   tmp[i + 1] = ' ';
                   counter++;
               }
           }
       }
       return new String(tmp);
   }

    /**
     *
     * @param word, a string from which the intonation is to be removed.
     * @return a string without intonation
     */
    private static String removeIntonation(String word){
        char[] tmp = word.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            switch(tmp[i]){
                case 'ά':
                    tmp[i]='α';
                    break;
                case 'έ':
                    tmp[i]='ε';
                    break;
                case 'ό':
                    tmp[i]='ο';
                    break;
                case 'ή':
                    tmp[i]='η';
                    break;
                case 'ί':
                    tmp[i]='ι';
                    break;
                case 'ύ':
                    tmp[i]='υ';
                    break;
                case 'ώ':
                    tmp[i]='ω';
                    break;
                case 'ΐ':
                    tmp[i]='ι';
                    break;
                case 'ϊ':
                    tmp[i]='ι';
                    break;
                case 'ΰ':
                    tmp[i]='υ';
                    break;
                case 'ϋ':
                    tmp[i]='υ';
                    break;
                default:
                    break;
            }
        }
        return new String(tmp);
    }

    /**
     *
     * @param word, a string to be modified, based on vowel digram combinations,
     * that when combined, produce a different sound, that is considered a
     * consonant.E.g 'αυτος' => 'αφτος'
     * @return the modified @param if applicable, the original @param otherwise
     */
    private static String getVowelAsConsonant(String word) {
        // Check if a letter contains αυ , ευ and check if the next letter corresponds to a rule
        char[] tmp = word.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            if ((tmp[i] == 'υ' || tmp[i] == 'ύ') && (i - 1 >= 0 && isAorE(tmp[i - 1]))) {

                if ((i + 1) < tmp.length && isAecho(tmp[i + 1])) {
                    tmp[i] = 'φ';
                } else if ((i + 1) < tmp.length && (i + 1 < tmp.length && (isVowel(tmp[i + 1]) || isStrong(tmp[i + 1])))) {
                    tmp[i] = 'β';
                }
                if (i == tmp.length - 1) {
                    tmp[i] = 'φ';
                }
            }
        }
        return new String(tmp);
    }

    /**
     *
     * @param word, a string to be modified if it contains specific consonant
     * digram combinations
     * @return the modified string
     */
    private static String unwrapConsonantBigrams(String word) {
        // Reason for example of μπ -> b and not -> β , is that vowels as consonant operation follows and may 
        // wrongly change to a consonant
        word = word.replace("μπ", "b");
        word = word.replace("ντ", "d");
        word = word.replace("γκ", "g");
        word = word.replace("γγ", "g");
        word = word.replace("τσ", "c");
        word = word.replace("τζ", "c");
        word = word.replace("πς", "ψ");
        word = word.replace("πσ", "ψ");
        word = word.replace("κς", "ξ");
        word = word.replace("κσ", "ξ");

        return word;
    }

    
    /**
    * 
    * Version for the exact phonetic transcription
    * @param word, a string to be modified if it contains specific consonant
    * digram combinations
    * @return the modified string
    */
   private static String unwrapConsonantBigramsPhonetically(String word) {
       // Reason for example of μπ -> b and not -> β , is that vowels as consonant operation follows and may 
       // wrongly change to a consonant
       word = word.replace("μπ", "b");
       word = word.replace("ντ", "d");
       word = word.replace("γκ", "g");
       word = word.replace("γγ", "g");
       word = word.replace("τσ", "ts"); // different than plain (in soundex: c)
       word = word.replace("τζ", "dz"); // different than plain (in soundex: c)
       word = word.replace("πς", "ψ");
       word = word.replace("πσ", "ψ");
       word = word.replace("κς", "ξ");
       word = word.replace("κσ", "ξ");

       return word;
   }

    /**
     *
     * @param word, word to be encode through this Soundex implementation
     * @return an encoded word
     */
    public static String encode(String word) {

    	
    	
	    //The following function calls could be merged together in to one loop for better performance
        word = word.toLowerCase(); // word to lowercase
        word = unwrapConsonantBigrams(word); // αφαίρεση δίφθογγων - dual letter substitution to single
        //System.out.println(word + " (after unwrapConsonantBigrams)");
        word = getVowelAsConsonant(word); // μετατροπή ευ, αυ σε σύμφωνο , αν ακολουθεί κάποιο άηχο ή ηχηρό γράμμα - substitution of υ vowel to consonant if needed
        //System.out.println(" " + word  + " (after getVoelsAsConsonants)");
        // removeLast and removeLastStrict or almost useless, now that the word is trimmed to just 6 digits
        word = removeLastStrict(word);  // αφαίρεση του suffix της λέξης - suffix removal
        //System.out.println(" " + word  + " (after getLastStrict)");
        word = groupVowels(word); // μετατροπή φωνήεντων πχ αι σε ε - substitute group vowels to single vowel.
        //System.out.println(" " + word  + " (after groupVowels)");
        
        
        word = removeIntonation(word);
        //System.out.println(" " + word + " (after removeIntonation)");
        word = String.join("", word.split(" "));

        char[] givenWord = word.toCharArray();
        char[] res = new char[givenWord.length];

        int i = 1;

        res[0] = word.charAt(0);
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
                case 'ψ':
                case 'c':
                case 'ξ':
                    res[i] = '4';
                    break;
                case 'κ':
                case 'g':
                    res[i] = '5';
                    break;
                case 'λ':
                    res[i] = '6';
                    break;
                case 'μ':
                case 'ν':
                    res[i] = '7';
                    break;
                case 'ρ':
                    res[i] = '8';
                    break;
                case 'α':
                    res[i] = '9';
                    break;
                case 'ε':
                    res[i] = '*';
                    break;
                case 'ο':
                case 'ω':
                    res[i] = '$';
                    break;
                case 'ι':
                    res[i] = '@';
                    break;
                default:

                    res[i] = '0';
                    break;
            }
            i++;
        }
        
        //for (int z=0; z<res.length;z++)
        //	System.out.print(res[z]);
        //System.out.println(" (after encode)");
        
        String finalResult = ""; // Remove duplicates
        finalResult += res[0];
        for (i = 1; i < res.length; i++) {
            if (res[i] != '0') {
                if (res[i] != res[i - 1]) {
                    finalResult += res[i];
                }
            }

        }
        
        //for (int z=0; z<res.length;z++)
        //	System.out.print(res[z]);
        //System.out.println(" (after remove duplicates)");
        
        //finalResult += "00000000"; 
        finalResult += "00000000000000000000"; // needed only in the case the lenth of the code is big
        return finalResult.substring(0, LengthEncoding); // 4 letter length encoding
    }
    

    
    
    /**
    * It is a variation of the SoundexGR for returning a kind of phonetic transcription of the entire word
    * @param word, word to be encode 
    * @return phonetic transcription of the word
    */
   public static String phoneticTrascription(String word) {
       word = word.toLowerCase(); // word to lowercase
       //word = unwrapConsonantBigrams(word); // αφαίρεση δίφθογγων - dual letter substitution to single
       word = unwrapConsonantBigramsPhonetically(word); // NEW 
      //System.out.println(word + " (after unwrapConsonantBigrams Phonetically)");
       word = getVowelAsConsonant(word); // μετατροπή ευ, αυ σε σύμφωνο , αν ακολουθεί κάποιο άηχο ή ηχηρό γράμμα - substitution of υ vowel to consonant if needed
       //System.out.println(" " + word  + " (after getVoelsAsConsonants)");
       // removeLast and removeLastStrict or almost useless, now that the word is trimmed to just 6 digits
       //word = removeLastStrict(word);  // αφαίρεση του suffix της λέξης - suffix removal
       //System.out.println(" " + word  + " (after getLastStrict)");
       //word = groupVowels(word); // μετατροπή φωνήεντων πχ αι σε ε - substitute group vowels to single vowel.
       word = groupVowelsPhonetically(word); // NEW
      //System.out.println(" " + word  + " (after groupVowels phonetically)");
       //word = removeIntonation(word);
       //System.out.println(" " + word + " (after removeIntonation)");
       
       word = String.join("", word.split(" "));
     
       char[] givenWord = word.toCharArray();
       char[] res = new char[givenWord.length];

       int i = 0;
       givenWord = word.substring(i).toCharArray();
       
       for (char c : givenWord) {
           switch (c) {
           	   case  'b': res[i]='b'; break;
               case  'd': res[i]='d'; break;
               case  'c': res[i]='c'; break;
               case  'g': res[i]='g'; break;
               case  'u':   res[i]='u'; break; // different with soundexGR (for capturing U)
           	   //---------------
  
           	   case 'α':   res[i]='a'; break;
               case 'β':   res[i]='v'; break; // different
               case 'γ':   res[i]='γ'; break;
               case 'δ':   res[i]='δ'; break;
               case 'ε':   res[i]='e'; break; // different
               case 'ζ':   res[i]='ζ'; break;
               case 'η':   res[i]='i'; break; // different
               case 'θ':   res[i]='θ'; break;
               case 'ι':   res[i]='i'; break;
               case 'κ':   res[i]='k'; break; // different
               case 'λ':   res[i]='l'; break; // different
               case 'μ':   res[i]='m'; break; // different
               case 'ν':   res[i]='n'; break; // different
               case 'ξ':   res[i]='ξ'; break; 
               case 'ο':   res[i]='ο'; break;
               case 'π':   res[i]='p'; break; // different
               case 'ρ':   res[i]='r'; break; // different
               case 'σ':   res[i]='s'; break; // different
               case 'ς':   res[i]='s'; break; // different
               case 'τ':   res[i]='t'; break; // different
               case 'υ':   res[i]='U'; break;
               case 'φ':   res[i]='φ'; break;
               case 'χ':   res[i]='χ'; break;
               case 'ψ':   res[i]='ψ'; break;
               case 'ω':   res[i]='ο'; break;
             
               default:
                   //res[i] = '0';
                   res[i] = c; // NEW
                   break;
           }
           i++;
       }
       return new String(res);
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
    		System.out.printf("%11s -> %s %s \n", word, encode(word), phoneticTrascription(word));
    	}
    	
    }
}
