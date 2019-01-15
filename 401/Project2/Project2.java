/* Project2.java  Dynamic histogram */

import java.io.*;
import java.util.*;

public class Project2
{
	static final int INITIAL_CAPACITY = 10;
	public static void main (String[] args) throws Exception
	{
		// ALWAYS TEST FIRST TO VERIFY USER PUT REQUIRED INPUT FILE NAME ON THE COMMAND LINE
		if (args.length < 1 )
		{
			System.out.println("\nusage: C:\\> java Project2 <input filename>\n\n"); // i.e. C:\> java Project2 dictionary.txt
			System.exit(0);
		}
		int[] histogram = new int[0]; // histogram[i] == # of words of length n

		/* array of String to store the words from the dictionary. 
			We use BufferedReader (not Scanner). With each word read in, examine it's length and update word length frequency histogram accordingly.
		*/

		String[] wordList = new String[INITIAL_CAPACITY];
		int wordCount = 0;
		BufferedReader infile = new BufferedReader( new FileReader(args[0]) );
		while ( infile.ready() )
		{
			String word = infile.readLine();
			// # # # # # DO NOT WRITE/MODIFY ANYTHING ABOVE THIS LINE # # # # #
			
			if (wordList.length == wordCount)							//If the array is full, make it bigger
				wordList = upSizeArr(wordList);
			wordList[wordCount++] = word;								//Add the word to the list of words

			if (word.length() >= histogram.length)						//If the array is not big enough, make it exactly big enough
				histogram = upSizeHisto(histogram, word.length()+1);	//Needs to be word.length+1 so that on line 37 it does not go out of bounds
			histogram[word.length()]++;									
		
			//  # # # # # DO NOT WRITE/MODIFY ANYTHING BELOW THIS LINE  # # # # #
		} //END WHILE INFILE READY
		infile.close();

		wordList = trimArr( wordList, wordCount );
		System.out.println( "After final trim: wordList length: " + wordList.length + " wordCount: " + wordCount );

		// PRINT WORD LENGTH FREQ HISTOGRAM
		for ( int i = 0; i < histogram.length ; i++ )
			System.out.format("words of length %2d  %d\n", i,histogram[i] );

	} // END main

	static String[] upSizeArr( String[] fullArr )
	{	
	
		String[] newSize = new String[fullArr.length * 2];	//Doubles the size of the array
		for (int i = 0; i < fullArr.length; i++){			
			newSize[i] = fullArr[i];						//Transfers the contents of the array
		}
		return newSize; 

	}
	static String[] trimArr( String[] oldArr, int count )
	{
		
		String[] newSize = new String[count];				//Makes the length of the array exactly equal to the number of items in the array
		for (int i = 0; i < count; i++){					
			newSize[i] = oldArr[i];							//Transfers the contents of the array
		}
		return newSize; 
		
	}

	
	static int[] upSizeHisto( int[] oldArr, int newLength )
	{
		int[] newSize = new int[newLength];
			
		for (int i = 0; i < oldArr.length; i++){
			newSize[i] = oldArr[i];
		}
		return newSize; 

	}
} // END CLASS PROJECT#2