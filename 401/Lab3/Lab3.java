// Lab3.java Starter File

import java.io.*; // BufferedReader
import java.util.*; // Scanner

public class Lab3
{
	public static void main (String args[]) throws Exception // i.e. the input file you put on cmd line is not in directory
	{
		// ALWAYS TEST FIRST TO VERIFY USER PUT REQUIRED INPUT FILE NAME ON THE COMMAND LINE
		if (args.length < 1 )
		{
			System.out.println("\nusage: C:\\> java Lab2 <input filename>\n\n"); // i.e. C:\> java Lab2 input.txt
			System.exit(0);
		}
		BufferedReader infile = new BufferedReader (new FileReader( args[0] )); // we read our text file line by line
		int lineNum=0;
		while( infile.ready() )
		{
			String line = toAlphaLowerCase(infile.readLine());
			if ( isPalindrome( line ) )
				System.out.format("<%s> IS palindrome.\n",line);
			else
				System.out.format("<%s> NOT palindrome.\n",line);
		}
	} // END MAIN
	
	// ******* MODIFY NOTHING ABOVE  THIS   LINE YOU FILL IN THE METHODS BELOW *******	

	//Converts a given string to a string containing only lower case letters and no other characters
	static String toAlphaLowerCase( String s )
	{
		String newWord = ""; 			//Initialize variable outside if (may not be initialized otherwise if string contains no letters)
		for (int i = 0; i < s.length(); i++){
			char tester = s.charAt(i);
			if (Character.isLetter(tester))
				newWord += tester;
			
		}
		return newWord.toLowerCase();
	}
	// RETURNs true if and only if the string passed in is a palindrome
	static boolean isPalindrome( String s )
	{
		int i = 0, k = s.length() - 1;
		while (i < k){
			if (s.charAt(i) == s.charAt(k)){		
				i++;			//Continue to iterate through the loop
				k--;
			} else 				//If 2 letters aren't the same, not a palandrome
				return false;
		}
		return true;			//If it makes it through the loop, it is a palandrome
	}
} // END LAB3 CLASS












