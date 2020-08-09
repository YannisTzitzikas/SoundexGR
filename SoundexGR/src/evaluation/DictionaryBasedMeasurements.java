/**
 * 
 */
package evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import SoundexGR.SoundexGRExtra;
import mitos.stemmer.Stemmer;
import stemmerWrapper.StemmerWrapper;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 *
 */
public class DictionaryBasedMeasurements {
	
	
	/** Prints the contents of a map String->Integer
	 * 
	 * @param map
	 */
	private static void printMap( Map<String, Integer> map) {
		System.out.println("Code: Frequency");
		
		for (String code:map.keySet()) {
        	System.out.printf("%s: %d\n", code, map.get(code));
        }
		
	}
	
	/**
	 * It prints an inverted index (a map of words to sets of words)
	 * @param ii : the map
	 * @param verbose  : if false only the size of the posting list of each term is shown, if true all elements are shown too
	 */
	private static void printInvertedIndex(Map<String, HashSet<String>> ii, boolean verbose) {
	    for (String code:  ii.keySet()) {
	    	System.out.printf("%s,%d\n", code, ii.get(code).size());
	    	if (verbose) {
	    		for (String w: ii.get(code)) {
	    			System.out.println("\t"+w);
	    		}
	    	}
	    }
	}
	
	/**
	 * It performs a few measurements over a dictionary (about the distribution of codes)
	 * @param path  the file path of the dictionary
	 */
	public static void performMeasurements(String path) {
			int counter = 0;  // word counter
			int totalCharNum = 0;
			int wordMinSize = Integer.MAX_VALUE;
			int wordMaxSize = 0;
			int curWordSize = 0;
	        String line;
	        int codesCharSize = 0;
	        long startTime=0, endTime=0, totalTime = 0;
	        String wordEncoded=""; 
	        
	        int option2test = 1 ; // 1: CoundexGRExtra, 2: stemmer
	        
	        
	        //extra for codes' analytics (it measures the number of words assigned the same code)
	        Map<String, Integer> codesAndCounts = new HashMap<>(); // for code analytics
	        StemmerWrapper stemmer= new StemmerWrapper (); // for the case we want to perform analogous measurements using a stemmer	        
	        
	        // DETAILED for INSPECTION
	        Map<String, HashSet<String>> codesAndWords = new HashMap<>(); // for code analytics     
	        
	        
			try {
				FileReader fl = new FileReader(path);
		        BufferedReader bfr = new BufferedReader(fl);	
		        startTime = System.nanoTime();
		        while ((line = bfr.readLine()) != null) {
		        	counter++; // counting words
		        	curWordSize = line.length(); // size of current word
		        	totalCharNum+=curWordSize;  // adding chars of current word
		        	if (curWordSize <wordMinSize ) wordMinSize=curWordSize;  // for min/max word sizes
		        	if (curWordSize >wordMaxSize ) wordMaxSize =curWordSize;
		        	
		        	if (option2test==1) {
		        		wordEncoded = SoundexGRExtra.encode(line); // for testing SoundexGRExtra
		        	} else if  	(option2test==2) {
		        		wordEncoded = stemmer.getStemOf(line); 		// for testing a stemmer
		        	} 
		        	
		        	
		        	codesCharSize+=wordEncoded.length();
		        	
		        	//System.out.printf("%10s --> %s\n", line, wordInSoundex); // for debugging only
		        	
		        	//extra for analytics (counting how many words go to the same code/stem
		        	 Integer codefreq = codesAndCounts.get(wordEncoded);
		        	 codesAndCounts.put(wordEncoded, (codefreq == null) ? 1 : codefreq + 1);
		        	 
		        	 //DETAILED ANALYSIS
		        	 HashSet wordsWithThatCode = codesAndWords.get(wordEncoded);
		        	 if (wordsWithThatCode==null) { // the code is not in the map
		        		 wordsWithThatCode = new HashSet();
		        		 wordsWithThatCode.add(line);
		        		 codesAndWords.put(wordEncoded,wordsWithThatCode);
		        	 } else {
		        	 wordsWithThatCode.add(line);
		        	 }
		        }
	        } catch (Exception e) {
	        	System.out.println(e);
	        }
			
	        endTime = System.nanoTime();
	        totalTime = endTime - startTime;

	        
	        System.out.println("\tTEST OPTION      : " +  ( (option2test==1) ? "SoundexGRExtra": "Stemmer"));
	        	        
	        System.out.println("\tElapsed time     : " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS)+ " secs");
	        System.out.println("\tElapsed time     : " + TimeUnit.MILLISECONDS.convert(totalTime, TimeUnit.NANOSECONDS)+ " msecs");
	        System.out.println("Number of words read      : " + counter);
	        System.out.println("Total number of chars read: " + totalCharNum);
	        System.out.println("Avg word size             : " + (totalCharNum+0.0)/counter);
	        System.out.println("Min word size             : " + wordMinSize);
	        System.out.println("Max word size             : " + wordMaxSize);
	        System.out.println("Average code size         : " + (codesCharSize+0.0)/counter);
	        
	        // Analytics for the codes
	        System.out.println("Number of Distinct codes  : " + codesAndCounts.keySet().size());
	        int minCount=Integer.MAX_VALUE, maxCount=0;
	        for (String code:codesAndCounts.keySet()) {
	        	int codeCount = codesAndCounts.get(code);
	        	if (codeCount <minCount ) {
	        			minCount=codeCount;
	        		}
	        	if (codeCount >maxCount ) {
	        			maxCount =codeCount;
				}
	        }
	        
	        System.out.println("Min number of words of a code: " + minCount);
	        System.out.println("Max number of words of a code: " + maxCount);
	        //printMap(codesAndCounts);
	        
	        //Detailed
	        printInvertedIndex(codesAndWords,false);
	}
	
	public static void main(String[] args) {
		System.out.println("Soundex/DictionaryBasedMeasurements.");
		//performMeasurements("Resources/dictionaries/EN-winedt/test.dic"); // for debugging purposes
		performMeasurements("Resources/dictionaries/EN-winedt/gr.dic");  // for measurements over the dictionary
		
	}

}
