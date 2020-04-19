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
 */
public class BulkCheck {
    ArrayList<Integer> score = new ArrayList<>();
    int correct = 0, total = 0;

    public float getPrecision(LinkedHashSet<String> exp, ArrayList<String> res) {
        int counter = 0;
        for (String word : exp) {
            if (res.contains(word.trim())) {
                counter++;
            }
        }

        return res.size() == 0 ? 0 : counter / (float) res.size();

    }

    public float getRecall(LinkedHashSet<String> exp, ArrayList<String> res) {
        int counter = 0;
        for (String word : exp) {
            if (res.contains(word.trim())) {
                counter++;
            }
        }

        return counter / (float) exp.size();
    }

    public void check(Utilities utils, String path, String type, String fileToWrite) throws FileNotFoundException, IOException {
        FileReader fl = new FileReader(path);
        BufferedReader bfr = new BufferedReader(fl);
        String line;

        FileWriter fr = new FileWriter(fileToWrite);

        float total_pre = 0;
        float total_rec = 0;
        int counter = 0;

        long start = System.nanoTime();
        while ((line = bfr.readLine()) != null) {
            String[] tmp2 = line.split(",");
            LinkedHashSet<String> tmp = new LinkedHashSet<>(Arrays.asList(tmp2));

            ArrayList<String> res = utils.search(tmp2[0].trim(), type);

            total_pre += getPrecision(tmp, res);
            total_rec += getRecall(tmp, res);

            counter++;
        }

        long end = System.nanoTime();
        long total = end - start;

        System.out.println("TIME : " + (double) total / 1000000000);

        System.out.println("Average Precision :\t" + total_pre / counter);
        System.out.println("Average Recall :\t" + total_rec / counter);
        System.out.println("F-Measure: \t" + 2 * (total_pre / counter) * (total_rec / counter) / ((total_pre / counter) + (total_rec / counter)));
        fr.close();
    }

    public static void main(String[] args) {
        Utilities utils = new Utilities();
        BulkCheck bulkCheckRun = new BulkCheck();

        try {
            System.out.println("------Additions------");
            utils.readFile("Resources/names/additions.txt");
            
            //System.exit(1);
            
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
}
