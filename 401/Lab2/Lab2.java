/* Lab2.java  Reads two files into two arrays then checks both arrays for dupes */

import java.io.*;
import java.util.*;

public class Lab2
{
	static final int INITIAL_CAPACITY = 10;
	static final int NOT_FOUND = -1; // indexOfFirstDupe returns this value if no dupes found
	public static void main (String[] args) throws Exception
	{
		// ALWAYS TEST FIRST TO VERIFY USER PUT REQUIRED INPUT FILE NAME ON THE COMMAND LINE
		if (args.length < 1 )
		{
			System.out.println("\nusage: C:\\> java Lab2 <numbers file> <words filename>\n\n"); // i.e. C:\> java Lab2 10Kints.txt 172822words.txt
			System.exit(0);
		}

		String[] wordList = new String[INITIAL_CAPACITY];
		int[] intList  =  new int[INITIAL_CAPACITY];
		int wordCount = 0, intCount=0;

		Scanner intFile = new Scanner( new File(args[0]) );
		BufferedReader wordFile = new BufferedReader( new FileReader(args[1]) );

		// P R O C E S S   I N T   F I L E 
		while ( intFile.hasNextInt() ) // i.e. while there are more ints in the file
		{	if ( intCount == intList.length ) 
				intList = upSizeArr( intList );
			intList[intCount++] = intFile.nextInt();
		} //END WHILE intFile
		intFile.close(); 
		System.out.format( "\n%s loaded into intList array. size=%d, count=%d\n",args[0],intList.length,intCount );
		int dupeIndex = indexOfFirstDupe( intList, intCount );
		if ( dupeIndex == NOT_FOUND )
			System.out.format("No duplicate values found in intList\n");
		else
			System.out.format("First duplicate value in intList found at index %d\n",dupeIndex);


		// P R O C E S S   S T R I N G    F I L E
		while ( wordFile.ready() ) // i.e. while there is another line (word) in the file
		{	if ( wordCount == wordList.length ) 
				wordList = upSizeArr( wordList );
			wordList[wordCount++] = wordFile.readLine();
		} //END WHILE wordFile
		wordFile.close(); 
		System.out.format( "%s loaded into word array. size=%d, count=%d\n",args[1],wordList.length,wordCount );
		dupeIndex = indexOfFirstDupe( wordList, wordCount );
		if ( dupeIndex == NOT_FOUND )
			System.out.format("No duplicate values found in wordList\n");
		else
			System.out.format("First duplicate value in wordList found at index %d\n",dupeIndex);

	} // END MAIN

	//##################################################################################################
	// FYI. Methods that don't say private are by default, private.
		
	static String[] upSizeArr( String[] fullArr )
	{
		String[] newSize = new String[fullArr.length * 2];	//Doubles the size of the array
		for (int i = 0; i < fullArr.length; i++){			//.length-1 not needed because i will stop at length size and not go out of bounds
			newSize[i] = fullArr[i];						//Transfers the contents of the array
		}
		return newSize; 

	}
	
	static int[] upSizeArr( int[] fullArr )
	{
		int[] newSize = new int[fullArr.length * 2];		//Doubles the size of the array
		for (int i = 0; i < fullArr.length; i++){
			newSize[i] = fullArr[i];
		}
		
		return newSize; 

	}

	static int indexOfFirstDupe( int[] arr, int count )
	{		
		Arrays.sort(arr, 0, count);
		
		for (int i = 1; i < count; i++){
			if (arr[i-1] == arr[i])				//Only works for first dupe
				return i;						//If a duplicate is found, stop the loop
		
		}
		return NOT_FOUND;
	}

	static int indexOfFirstDupe( String[] arr, int count )
	{		
		Arrays.sort(arr, 0, count);	
		
		for (int i = 1; i < count; i++){
			if (arr[i-1].equals(arr[i]))		//Only works for first dupe
				return i;						//If a duplicate is found, stop the loop
		
		}
		
		return NOT_FOUND;
	}

} // END CLASS LAB#2
