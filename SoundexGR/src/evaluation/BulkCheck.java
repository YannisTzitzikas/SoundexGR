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
import java.util.stream.Collectors;
import utils.Utilities;
import org.apache.commons.text.similarity.LevenshteinDistance;
/**
 *
 * @author: Antrei Kavros
 */
 
public class BulkCheck {
    ArrayList<Integer> score = new ArrayList<>();
    int correct = 0, total = 0;

    /**
     * It takes as input a response (res) and the set of correct answers (exp)
     * and computes the precision
     * @param exp
     * @param res
     * @return
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
     * 
     * @param utils
     * @param path  the file with the eval collection
     * @param type  the soundex algorith to be applied
     * @param fileToWrite  the file to write (currently it just creates the file, it does not store anything there)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void check(Utilities utils, String path, String type, String fileToWrite) throws FileNotFoundException, IOException {
        FileReader fl = new FileReader(path);
        BufferedReader bfr = new BufferedReader(fl);
        String line;

        FileWriter fr = new FileWriter(fileToWrite); // opens the file to write  (currently does not write anything)
        float total_pre = 0; // initialization of total precision
        float total_rec = 0; // initialization of total precision
        int counter = 0;

        // reading the eval collection
        long start = System.nanoTime();
        while ((line = bfr.readLine()) != null) {
            String[] tmp2 = line.split(","); // reading the tokens of a line
            LinkedHashSet<String> tmp = new LinkedHashSet<>(Arrays.asList(tmp2)); // adding them to a hashset

            ArrayList<String> res = utils.search(tmp2[0].trim(), type); // ...

            total_pre += getPrecision(tmp, res); // adding the precision
            total_rec += getRecall(tmp, res); // adding the recall
            counter++;
        }

        long end = System.nanoTime();
        long total = end - start;

        System.out.println("\tElapsed time     : " + (double) total / 1000000000);

        System.out.println("\tAverage Precision: " + total_pre / counter); // computing the avg precision
        System.out.println("\tAverage Recall   : " + total_rec / counter); // comuting the avg recall
        System.out.println("\tF-Measure        : " + 2 * (total_pre / counter) * (total_rec / counter) / ((total_pre / counter) + (total_rec / counter)));
        fr.close();  // closes the output file
    }

    /** 
     * Performs the experiments:  Comparative Evaluation of various Soundex-like algorithms
     */
    public static void performExperiments() {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();

        try {
        	/*
        	 * Comparative Evaluation of various Soundex-like algorithms
        	 */
            System.out.println("------Additions------");
            utils.readFile("Resources/names/additions.txt");
            
                
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "soundex", "Resources/names/soundex/adds_result.txt");
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "original", "Resources/names/original/adds_result.txt");
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/additions.txt", "combine", "Resources/names/combine/adds_result.txt");
            System.out.println("------Additions End------\n\n");
            utils.clear();

            System.out.println("------Substitutions------");
            utils.readFile("Resources/names/subs.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "soundex", "Resources/names/soundex/subs_result.txt");
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "original", "Resources/names/original/subs_result.txt");
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/subs.txt", "combine", "Resources/names/combine/subs_result.txt");
            System.out.println("------Substitutions End------\n\n");
            utils.clear();

            System.out.println("------Deletions------");
            utils.readFile("Resources/names/deletions.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "soundex", "Resources/names/soundex/dels_result.txt");
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "original", "Resources/names/original/dels_result.txt");
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/deletions.txt", "combine", "Resources/names/combine/dels_result.txt");
            System.out.println("------Deletions End------\n\n");
            utils.clear();
            
            System.out.println("-------Same-------");
            utils.readFile("Resources/names/same_sounded.txt");
            System.out.println("-Soundex");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "soundex", "Resources/names/soundex/sames.txt");
            System.out.println("-Original");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "original", "Resources/names/original/sames.txt");
            System.out.println("-Combine");
            bulkCheckRun.check(utils, "Resources/names/same_sounded.txt", "combine", "Resources/names/combine/sames.txt");
            System.out.println("-------Same End-------\n\n");
            utils.clear();
            
            

        } catch (IOException ex) {
            Logger.getLogger(BulkCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

    	
    }
    
    /** 
     * Ongoing: C
     */
    public static void performExperimentsNew() {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();
        
        System.out.println("EVALUTION (NEW)");
        
        String DatasetFiles[]		= {
        	"Resources/names/additions.txt", 		// additions
        	"Resources/names/subs.txt", 			// subtitutions
        	"Resources/names/deletions.txt", 		// deletions			
        	"Resources/names/same_sounded.txt" 		// same sounded
        };  // evaluation collections
        
        String OptionsToEvaluate[] 	= { "soundex", "original", "combine" };
        String outputFilePrefix 	=  "Resources/names/results"	;   // prefixes of files for writting

        try {
            for (String datasetFile: DatasetFiles) { // for each dataset file
            	utils.readFile(datasetFile); // reads the dataset file
            	System.out.println("["+datasetFile+"]");
            	for (String optionToEvaluate: OptionsToEvaluate ) { // for each code generation option
            		System.out.println("\tResults of testing over " + datasetFile + " the option *" + optionToEvaluate + "*:");
            		
            		String outputFileName = 
            				outputFilePrefix + "/output-" +
            				datasetFile.substring(datasetFile.lastIndexOf('/')+1); // the prefix + the last part of the dataset filename
            		
            		//System.out.println(">>>>>"+outputFileName);
            		bulkCheckRun.check(utils, datasetFile, optionToEvaluate, outputFileName);
            		System.out.println("-------------------------------------------------");
            	}
            	utils.clear();
            }
            
        } catch (IOException ex) {
        	System.out.print(ex);
            Logger.getLogger(BulkCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /** 
     * Ongoing: Comparison with Soundex
     */
    public static void performExperimentsWithStemmer() {
    	Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();
        
        System.out.println("ONGOING: Comparison with Stemming");
        
        String DatasetFiles[]		= { "Resources/names/same_sounded.txt"};  // evaluation collections
        String OptionsToEvaluate[] 	= { "soundex", "original", "combine" };
        String outputFile     		=  "Resources/names/results/sames-new.txt"	;   // file for writting

        try {
            for (String datasetFile: DatasetFiles) { // for each dataset file
            	utils.readFile(datasetFile); // reads the dataset file
            	System.out.println("["+datasetFile+"]");
            	for (String optionToEvaluate: OptionsToEvaluate ) { // for each code generation option
            		System.out.println("Results  over " + datasetFile + " with the option *" + optionToEvaluate + "*:");
            		bulkCheckRun.check(utils, datasetFile, optionToEvaluate, outputFile);
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
    	//performExperiments(); // the original experiments
    	performExperimentsNew(); // the refactored one by Yannis Tzitzikas
    	//performExperimentsWithStemmer();  // Ongoing: Experiments with Soundex
        }
}
