import java.util.*;
import java.io.*;

public class Lab4
{
	
	public static void main(String[] args) throws Exception
	{
		if (args.length < 1){
			System.out.println("Error, must give an input file of Strings. ie java Lab4 jumbles.txt");
			System.exit(0);
		}
		
		BufferedReader infile = new BufferedReader (new FileReader(args[0]));		//Initalize input method and array
		ArrayList<String> jumble = new ArrayList<String>();
		String sorted, jumbled, combined;											//Declare variables outside of loop
		
		while(infile.ready()){					//Takes the file, adds the word and its sorted form to an ArrayList
			
			jumbled = infile.readLine();
			sorted = sortLetters(jumbled);
			combined = jumbled + " " + sorted;
			jumble.add(combined);
			
		}
		
		Collections.sort(jumble);				//Sort the whole array into alphabetical order
		
		for (String s : jumble)				//Print the array
			System.out.println(s);
		
		
	}
	
	//Takes a string and sorts its letters into to alphabetical order
	private static String sortLetters(String word){
		
		char[] arr = word.toCharArray();
		Arrays.sort(arr);
		
		return new String(arr);
	
	}
	
	
	
}