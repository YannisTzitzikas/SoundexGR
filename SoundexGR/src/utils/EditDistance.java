/**
 * 
 */
package utils;

//A Space efficient Dynamic Programming
//based Java program to find minimum
//number operations to convert str1 to str2
// Adapted from code given in https://www.geeksforgeeks.org/edit-distance-dp-5/

import java.util.*;
public class EditDistance
{
/**
 * Returns the edit distance between the two strings
 * @param str1
 * @param str2
 * @return
 */
public static int EditDistDP(String str1, String str2)
{
 int len1 = str1.length();
 int len2 = str2.length();

 // Create a DP array to memoize result
 // of previous computations
 int [][]DP = new int[2][len1 + 1];


 // Base condition when second String
 // is empty then we remove all characters
 for (int i = 0; i <= len1; i++)
     DP[0][i] = i;

 // Start filling the DP
 // This loop run for every
 // character in second String
 for (int i = 1; i <= len2; i++)
 {
    
     // This loop compares the char from
     // second String with first String
     // characters
     for (int j = 0; j <= len1; j++)
     {
        
         // if first String is empty then
         // we have to perform add character
         // operation to get second String
         if (j == 0)
             DP[i % 2][j] = i;

         // if character from both String
         // is same then we do not perform any
         // operation . here i % 2 is for bound
         // the row number.
         else if (str1.charAt(j - 1) == str2.charAt(i - 1)) {
             DP[i % 2][j] = DP[(i - 1) % 2][j - 1];
         }

         // if character from both String is
         // not same then we take the minimum
         // from three specified operation
         else {
             DP[i % 2][j] = 1 + Math.min(DP[(i - 1) % 2][j],
                                    Math.min(DP[i % 2][j - 1],
                                        DP[(i - 1) % 2][j - 1]));
         }
     }
 }

 // after complete fill the DP array
 // if the len2 is even then we end
 // up in the 0th row else we end up
 // in the 1th row so we take len2 % 2
 // to get row
 //System.out.print(DP[len2 % 2][len1] +"\n");
 
 return DP[len2 % 2][len1];
 
}

/**
 * returns true if the edit distance of s1 and s2 is less than k
 * @param s1
 * @param s2
 * @param k
 */
static boolean match(String s1, String s2, int k ) {
	
	int ed = EditDistDP(s1, s2);
	return (ed <= k);

	
}

//Driver program
public static void main(String[] args)
{
 String str1 = "food";
 String str2 = "money";
 System.out.println(EditDistDP(str1, str2));
 
 System.out.println(match(str1,str2,1));
 System.out.println(match(str1,str2,2));
 System.out.println(match(str1,str2,3));
 System.out.println(match(str1,str2,4));
 System.out.println(match(str1,str2,5));
 
 
 
 System.out.println(EditDistDP("αβγό","αυγό"));
}
}

//This code is contributed by aashish1995