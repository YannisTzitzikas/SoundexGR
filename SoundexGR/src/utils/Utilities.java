/**
 * @author: Antrei Kavros (refactoring and additions by Yannis Tzitzikas)
 */

package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import SoundexGR.SoundexGRExtra;
import SoundexGR.SoundexGRSimple;
import stemmerWrapper.StemmerWrapper;

import java.io.FileWriter;

import java.security.SecureRandom;
import java.util.TreeMap;

public class Utilities {

    LinkedHashSet<String> names = new LinkedHashSet<>();  //  a set of names (read by the file)
    LinkedHashSet<String> errNames = new LinkedHashSet<>(); // the error names to be produced and written in a file
    TreeMap<String, String> wcPair = new TreeMap<>(); //WrongCorrect pair
    TreeMap<String, String> cwPair = new TreeMap<>(); //CorrectWrong pair

    SecureRandom rand = new SecureRandom();

    /**
     * reads the filename and fills the hashset "names"
     * @param path
     * @throws FileNotFoundException
     * @throws IOException
     */
        
    public void readFile(String path) throws FileNotFoundException, IOException {
    	    	
        FileReader fl = new FileReader(path);
        BufferedReader bfr = new BufferedReader(fl);
        String line;
        while ((line = bfr.readLine()) != null) {
            String[] tmp = line.split(",");
            for (String word : tmp) {
                names.add(word.trim());
            }
        }
    }
    
        
    /**
     * reads the filename (the first maxWordNum words) and fills the hashset "names" 
     * @param path
     * @param  maxWordNum  the max number of words to be read
     * @throws FileNotFoundException
     * @throws IOException
     */

    
    public void readFile(String path, int maxWordNum) throws FileNotFoundException, IOException {
    	int wordsRead=0;	
        FileReader fl = new FileReader(path);
        BufferedReader bfr = new BufferedReader(fl);
        String line;
        while ((line = bfr.readLine()) != null) {
            String[] tmp = line.split(",");
            for (String word : tmp) {
            	if (wordsRead<maxWordNum) {
            		names.add(word.trim());
            		wordsRead++;
            	}
            }
        }
        //System.out.println(">>readFile read "+wordsRead+" words, specifically: "+names);
    }
    

    /**
     * clears the names
     */
    public void clear() {
        this.names.clear();
    }

    public void writeRandom(String pairPath, String setPath) throws IOException {
        FileWriter fr = new FileWriter(pairPath);
        FileWriter fr2 = new FileWriter(setPath);

        String tmp;
        
        for (String name : names) {
            tmp = this.randomizr(name.trim());
            fr.write(tmp + "-" + name.trim() + "\n");
            fr2.write(tmp + "\n");
            wcPair.put(tmp, name);
            cwPair.put(name, tmp);
            errNames.add(tmp);
        }
        
        fr.close();
        fr2.close();
    }

    public String randomizr(String word) {

        int min = 945, max = 974;
        char letter;
        int nxtLetter, nxtIndex, nxtOperation;
        StringBuilder sb = new StringBuilder(word);

        nxtLetter = rand.nextInt(max - min + 1) + min;
        nxtIndex = rand.nextInt(word.length());
        nxtOperation = rand.nextInt(3);
        letter = (char) nxtLetter;

        if (nxtOperation != 2) {
            while (word.charAt(nxtIndex) == letter) {
                nxtLetter = rand.nextInt(max - min + 1) + min;
                letter = (char) nxtLetter;
            }
        }

        switch (nxtOperation) {
            case 0: // substitution
                sb.replace(nxtIndex, nxtIndex, "" + letter);
                break;
            case 1: // addition
                sb.insert(nxtIndex, letter);
                break;
            case 2: // deletion
                sb.deleteCharAt(nxtIndex);
                break;
        }
        return sb.toString();
    }
    /**
     * It takes as input a query word and the phonetic alg to be used
     * and returns the words that have the same code
     * It pre-supposes "names" which is the set of names to be checked
     * (those by reading the file)
     * @param query the query code
     * @param type the algorithm to be used (soundex, original, combine)
     * @return an arraylist of strings
     */

    public ArrayList<String> search(String query, String type) {
        ArrayList<String> res = new ArrayList<>();
              
        
        
        if (type.compareTo("exactMatch") == 0) {
            for (String word : names) {               
                if (word.compareTo(query) == 0) {
                    res.add(word);
                }
            }
        }
        else if (type.compareTo("soundex") == 0) {
            for (String word : names) {               
                if (SoundexGRExtra.encode(word).compareTo(SoundexGRExtra.encode(query)) == 0) {
                    res.add(word);
                }
            }
        } else if (type.compareTo("original") == 0) {
            for (String word : names) {
                if (SoundexGRSimple.encode(word).compareTo(SoundexGRSimple.encode(query)) == 0) {
                    res.add(word);
                }

            }
        } else if(type.compareTo("combine")==0){   // yes if at least one of the codes (of Simple or Extra) is the same
            for (String word : names) {
                if (SoundexGRExtra.encode(word).compareTo(SoundexGRExtra.encode(query)) == 0 ||
                        SoundexGRSimple.encode(word).compareTo(SoundexGRSimple.encode(query)) == 0) {
                    res.add(word);
                }

            }
        }  else if (type.compareTo("stemcase") == 0) {  // tests a stemmer over the collection
           	StemmerWrapper stemmer = new StemmerWrapper();	
            for (String word : names) {
            	//System.out.print(word + " vs " + query + ":" + stemmer.getStemOf(word) +"<->"+ stemmer.getStemOf(query));
            	if (stemmer.getStemOf(word).compareTo(stemmer.getStemOf(query)) == 0) {
            		//System.out.println("[Correct]");
                    res.add(word);
                } else {
                	//System.out.println("[Wrong]");
                }
           }
        } else if (type.compareTo("stemAndsoundex") == 0) {  // new ongoing 
           	StemmerWrapper stemmer = new StemmerWrapper();
           	String queryStemmed = stemmer.getStemOf(query);
           	String wordStemmed;
            for (String word : names) {
            	wordStemmed = stemmer.getStemOf(word);
            	if (SoundexGRExtra.encode(wordStemmed).compareTo(SoundexGRExtra.encode(queryStemmed)) == 0) {
                    res.add(word); //add the word before stemming (for computing correctly the metrics)
                }
            }
        }   else if (type.compareTo("fullPhonetic") == 0) {  // full phonetic 
        	for (String word : names) {               
                if (SoundexGRExtra.phoneticTrascription(word).compareTo(SoundexGRExtra.phoneticTrascription(query)) == 0) {
                    res.add(word);
                }
        	} 
        }  
        
        else if (type.compareTo("editDistance1") == 0) {  // editDistance 
        	for (String word : names) { 
        		if (EditDistance.match(word, query, 1)) {
                    res.add(word);
                }
        	} 
        }  
        
        else if (type.compareTo("editDistance2") == 0) {  // editDistance 
        	for (String word : names) { 
        		if (EditDistance.match(word, query, 2)) {
                    res.add(word);
                }
        	} 
        }  
        
        else if (type.compareTo("editDistance3") == 0) {  // editDistance 
        	for (String word : names) { 
        		if (EditDistance.match(word, query, 3)) {
                    res.add(word);
                }
        	} 
        }  
        
        else if (type.compareTo("editDistance4") == 0) {  // editDistance 
        	for (String word : names) { 
        		if (EditDistance.match(word, query, 4)) {
                    res.add(word);
                }
        	} 
        }  
        
       
        return res;
    }

    /**
     * It returns true if at least one of the codes (SoundexGRExtra or SoundexGRSimple) is the same
     * @param s1
     * @param s2
     * @return
     */
    public static boolean compareCombined(String s1, String s2){
        if (SoundexGRExtra.encode(s1).compareTo(SoundexGRExtra.encode(s2)) == 0 ||
                SoundexGRSimple.encode(s1).compareTo(SoundexGRSimple.encode(s2)) == 0) {
            return true;
        }

        return false;
    }

    public static void prompt() {
            Scanner input = new Scanner(System.in);
            System.out.println("Use 'word1 | word2' to compare two words\nUse just a word to view its code\nEnter a query:");

            String query = input.nextLine();
            while (query.compareTo("--exit") != 0) {
                if(query.contains("|")){
                    String[] data = query.split("[\\|\\s]+");
                    if(data.length != 2){
                        System.out.println("Specify two words, e.g ευθεία | εφθεία");
                    }
                    System.out.println("<--------RESULTS-------->\n");
                    System.out.println("Soundex Extra for " + data[0] + " : " + SoundexGRExtra.encode(data[0].trim()));
                    System.out.println("Soundex Extra for " + data[1] + " : " + SoundexGRExtra.encode(data[1].trim()));

                    System.out.println("Soundex Simple for " + data[0] + " : " + SoundexGRSimple.encode(data[1].trim()));
                    System.out.println("Soundex Simple for " + data[1] + " : " + SoundexGRSimple.encode(data[0].trim()));

                    System.out.println("Comparison with combine returns : " + (Utilities.compareCombined(data[0], data[1]) ? "Equal": "Not Equal") );
                } else {
                    System.out.println("<--------RESULTS-------->\n");
                    System.out.println("Soundex Extra: " + SoundexGRExtra.encode(query));
                    System.out.println("Soundex Simple: " + SoundexGRSimple.encode(query));
                }
                System.out.println("\nEnter another query or --exit to exit");
                query = input.nextLine();
            }
    }
}
