/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evaluation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import SoundexGR.SoundexGRExtra;
import SoundexGR.SoundexGRSimple;
import utils.MeasurementsWriter;
//import java.util.stream.Collectors;
import utils.Utilities;
//import org.apache.commons.text.similarity.LevenshteinDistance;
/**
 *
 * @author: Antrei Kavros (additions by Yannis Tzitzikas)
 */
 
public class BulkCheck {
    	
	static MeasurementsWriter mw=null; // for writing evaluation measurements in a file

    /**
     * It takes as input a response (res) and the set of correct answers (exp)
     * and computes the precision
     * @param exp
     * @param res
     * @return the precision
     */
    public float getPrecision(LinkedHashSet<String> exp, ArrayList<String> res) {
        int counter = 0;
        for (String word : exp) {
            if (res.contains(word.trim())) {
                counter++;
            }
        }
        return res.size() == 0 ? 0 : counter / (float) res.size();
    }

    /**
     * It takes as input a response (res) and the set of correct answers (exp)
     * and computes the recall
     * @param exp
     * @param res
     * @return
     */
    public float getRecall(LinkedHashSet<String> exp, ArrayList<String> res) {
        int counter = 0;
        for (String word : exp) {
            if (res.contains(word.trim())) {
                counter++;
            }
        }
        return counter / (float) exp.size();
    }

    /**
     * It computes precision/recall/f-measure
     * @param utils
     * @param path  the file with the eval collection
     * @param type  the matching (soundex) algorith to be applied
     * @param fileToWrite  the file to write (currently it just creates the file, it does not store anything there)
     * @param maxWordNum max number of words to consider, if 0 no limit is applied 
     * @throws FileNotFoundException
     * @throws IOException
     * NOTE: the maxWordNum should be considered also in the reading of the file (i.e. in method check)
     */
    public void check(Utilities utils, String path, String type, String fileToWrite, int maxWordNum) throws FileNotFoundException, IOException {
    	boolean bounded = maxWordNum !=0;  // true if the max num of words should be applied
    	
    	FileReader fl = new FileReader(path);
        BufferedReader bfr = new BufferedReader(fl);
        String line;

        FileWriter fr = new FileWriter(fileToWrite); // opens the file to write  (currently does not write anything)
        float total_pre = 0; // initialization of total precision
        float total_rec = 0; // initialization of total recall
        int counter = 0; // counts the number of buckets (i.e. the number of lines in the file)

        int numOfWords = 0; // ytz: 2021 for counting the words
        
        // reading the eval collection
        long start = System.nanoTime();
        while ((line = bfr.readLine()) != null) {
            String[] tmp2 = line.split(","); // reading the tokens of a line
               
            if (!bounded) { // no bound on number of words
            	LinkedHashSet<String> tmp = new LinkedHashSet<>(Arrays.asList(tmp2)); // adding them to a hashset
	            ArrayList<String> res = utils.search(tmp2[0].trim(), type); // ...
	            total_pre += getPrecision(tmp, res); // adding the precision
	            total_rec += getRecall(tmp, res); // adding the recall
	            counter++;
	            numOfWords+=tmp2.length;
        	} else { // bounded number of word
        		if (numOfWords<maxWordNum)  { //
        			LinkedHashSet<String> tmp = new LinkedHashSet<>();
        			for (String s: tmp2) {
        				if (numOfWords<maxWordNum) {
        					tmp.add(s);
        					numOfWords++;
        				}
        			}
        			ArrayList<String> res = utils.search(tmp2[0].trim(), type); // ...
    	            total_pre += getPrecision(tmp, res); // adding the precision
    	            total_rec += getRecall(tmp, res); // adding the recall
    	            counter++;
            	} else { // have read the max words
            		
            	}
        	}
        }

        long end = System.nanoTime();
        long total = end - start;

        double elapsedTime = (double) total / 1000*1000*1000;
        float avgPrecision = total_pre / counter;  	// computing the avg precision
        float avgRecall = total_rec / counter; 		// computing the avg recall
        float avgFmeasure = 2 * (total_pre / counter) * (total_rec / counter) / ((total_pre / counter) + (total_rec / counter));
        
        if (SoundexGRExtra.LengthEncoding!=SoundexGRSimple.LengthEncoding) { // the config is not correct for experiments
        	throw new RuntimeException("SoundexGRExtra.LengthEncoding!=SoundexGRSimple.LengthEncoding");
        }
        
        mw.write(SoundexGRExtra.LengthEncoding + ","); // writing to file
        mw.write(avgPrecision+",");
        mw.write(avgRecall+",");
        mw.write(avgFmeasure+"\n");
        
        /*
        System.out.println("\tElapsed time     : " + elapsedTime);
        System.out.println("\tAverage Precision: " + avgPrecision); 
        System.out.println("\tAverage Recall   : " + avgRecall); 
        System.out.println("\tF-Measure        : " + avgFmeasure);
        System.out.println("\tNum of words checked: " + numOfWords);
        */
        System.out.format("\tPre:%.3f Rec:%.3f F1:%.3f NWords:%d",avgPrecision, avgRecall,avgFmeasure,numOfWords);
                
        fr.close();  // closes the output file
    }

    /** 
     * (OLD VERSION - NOT IN USE)
     * Performs the experiments:  Comparative Evaluation of various Soundex-like algorithms
     * (the old one)
     */
    public static void performExperimentsOLDVERSION() {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();

        try {
        	/*
        	 * Comparative Evaluation of various Soundex-like algorithms
        	 */
            System.out.println("------Additions------");
            utils.readFile("Resources/names/additions.txt");
            
                
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "soundex", "Resources/names/soundex/adds_result.txt",0);
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "original", "Resources/names/original/adds_result.txt",0);
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "combine", "Resources/names/combine/adds_result.txt",0);
            System.out.println("------Additions End------\n\n");
            utils.clear();

            System.out.println("------Substitutions------");
            utils.readFile("Resources/names/subs.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "soundex", "Resources/names/soundex/subs_result.txt",0);
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "original", "Resources/names/original/subs_result.txt",0);
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "combine", "Resources/names/combine/subs_result.txt",0);
            System.out.println("------Substitutions End------\n\n");
            utils.clear();

            System.out.println("------Deletions------");
            utils.readFile("Resources/names/deletions.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "soundex", "Resources/names/soundex/dels_result.txt",0);
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "original", "Resources/names/original/dels_result.txt",0);
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "combine", "Resources/names/combine/dels_result.txt",0);
            System.out.println("------Deletions End------\n\n");
            utils.clear();
            
            System.out.println("-------Same Sounded-----");
            utils.readFile("Resources/names/same_sounded.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "soundex", "Resources/names/soundex/sames.txt",0);
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "original", "Resources/names/original/sames.txt",0);
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "combine", "Resources/names/combine/sames.txt",0);
            System.out.println("-------Same End-------\n\n");
            utils.clear();
            
            

        } catch (IOException ex) {
            Logger.getLogger(BulkCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

    	
    }
    
    /**
     * 
     */
    
    public static void performExperimentsForDatasetSizes() {
    	//MeasurementsWriter initialization and header
    	mw = new MeasurementsWriter("Resources/measurements/phoneticMeasurements.csv");
    	mw.write("# datasetName, datasetSize, codeMethod, CodeSize, Precision, Recall, FScore\n");
    	
    	// PARAMS of the experiments to run
    	// Dataset sizes
    	int dSizeMin=200; //100
    	int dSizeMax=2000; //3000
    	int dSizeStep=200;  //4000
    	
    	// Code sizes
    	int codeSizeMin=2; //100
    	int codeSizeMax=12; //3000
    	
    	
    	for (int ds=dSizeMin; ds<=dSizeMax; ds+=dSizeStep) { // datasetsizes
    		
    		for (int codeSize=codeSizeMin; codeSize<=codeSizeMax; codeSize++) {  // code sizes
        		
        		// setting the length encoding 
        		SoundexGRExtra.LengthEncoding = codeSize;
        		SoundexGRSimple.LengthEncoding = codeSize;
        		
    			performExperiments(ds);  // performs the experiments
    		}
    	}
    	
    	// closing the measurements file
    	mw.close();
    	System.out.println("COMLETION.");
    }
    
    
    /** 
     * Refactored code: Performs all the experiments
     * @param maxWordNum  max number of words from the dataset to be considered (use 0 for no limit in the number of words to be considered)
     */
    public static void performExperiments(int maxWordNum) {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();
        
        //MeasurementsWriter initialization and header
    	if (mw==null) {
    		String filename = "Resources/measurements/currentMeasurements.csv";
    		System.out.println("Creating a new file: "+ filename);
	        mw = new MeasurementsWriter(filename);
	    	mw.write("# datasetName, datasetSize, codeMethod, CodeSize, Precision, Recall, FScore\n");
    	}	
        
        String DatasetFiles[]		= {
        	"Resources/names/additions.txt", 		// additions
        	"Resources/names/subs.txt", 			// subtitutions
        	"Resources/names/deletions.txt", 		// deletions			
        	"Resources/names/same_sounded.txt"  // same sounded
        	//"Resources/names/newcollection.txt"  // test purposes  
        };  // evaluation collections
        
        String OptionsToEvaluate[] 	= { 
        		"exactMatch",
        		"soundex", 
        		"original", 
        		"combine", 
        		"stemAndsoundex", 
        		"fullPhonetic"

        };  // all supported options
        //String OptionsToEvaluate[] 	= { "soundex"};  
        String outputFilePrefix 	=  "Resources/names/results"	;   // prefixes of files for writing

        try {
            for (String datasetFile: DatasetFiles) { // for each dataset file
            	if (maxWordNum==0)  
            		utils.readFile(datasetFile) ;
            	else   	
            		utils.readFile(datasetFile,maxWordNum); // reads the dataset file (up to maxWordNum), 0: no limit
            	System.out.print("["+datasetFile+"]: ");
            	            	
            	for (String optionToEvaluate: OptionsToEvaluate ) { // for each code generation option
            		System.out.println("\tTesting *" + optionToEvaluate + "* " + " codeLength=" + SoundexGRExtra.LengthEncoding +" maxwords="+maxWordNum);
            		mw.write(datasetFile+","+maxWordNum+","+optionToEvaluate+","); // writing to measurement file
            		
            		String outputFileName = 
            				outputFilePrefix + "/output-" +
            				datasetFile.substring(datasetFile.lastIndexOf('/')+1); // the prefix + the last part of the dataset filename
            		
            		//System.out.println(">>>>>"+outputFileName);
            		bulkCheckRun.check(utils, datasetFile, optionToEvaluate, outputFileName, maxWordNum);
            		System.out.println(""); //-------------------------------------------------");
            	}
            	utils.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(BulkCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        mw.close(); // new to test
    }

    
        
    /** 
     * Comparing the performance of Stemming
     */
    public static void performExperimentsWithStemmer() {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();
        
        System.out.println("Evaluating the peformance of *stemming*");
        String DatasetFiles[]		= {
            	"Resources/names/additions.txt", 		// additions
            	"Resources/names/subs.txt", 			// subtitutions
            	"Resources/names/deletions.txt", 		// deletions			
            	"Resources/names/same_sounded.txt" 		// same sounded
            };  // evaluation collections
        
        String OptionsToEvaluate[] 	= { "stemcase"};
        String outputFile     		=  "Resources/names/results/sames-stemmer.txt"	;   // file for writing

        try {
            for (String datasetFile: DatasetFiles) { // for each dataset file
            	utils.readFile(datasetFile); // reads the dataset file
            	System.out.println("["+datasetFile+"]");
            	for (String optionToEvaluate: OptionsToEvaluate ) { // for each code generation option
            		System.out.println("Results  over " + datasetFile + " with the option *" + optionToEvaluate + "*:");
            		bulkCheckRun.check(utils, datasetFile, optionToEvaluate, outputFile,0);
            		System.out.println("-------------------------------------------------");
            	}
            	utils.clear();
            }    
        } catch (IOException ex) {
        	System.out.println(ex);
            Logger.getLogger(BulkCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
    	
    	performExperiments(0); // the refactored code for the experiments Yannis Tzitzikas
    	//performExperimentsWithStemmer();  // evaluation of a Greek stemmer
    	//performExperimentsForDatasetSizes();
        }
}
