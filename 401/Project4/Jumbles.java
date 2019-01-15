/*
*
*			VERSION 2
*
*/ 
import java.util.*;
import java.io.*;

public class Jumbles
{
	
	public static void main(String[] args) throws Exception
	{
		
		if (args.length < 2){
			System.out.println("Error, must give an input file of Strings. ie java Lab4 dictonary.txt jumbles.txt");
			System.exit(0);
		}
		
		BufferedReader dictFile = new BufferedReader (new FileReader(args[0]));		//Initalize input methods and arrays
		BufferedReader jumbleFile = new BufferedReader (new FileReader(args[1]));
		ArrayList<String> dictonary = new ArrayList<String>();
		ArrayList<String> jumble = new ArrayList<String>();
		
		String sorted, normal, combined;											//Variables needed to add to dictonary array
		
		while(dictFile.ready()){					//Takes the file, adds the word and its sorted form to an ArrayList
			
			normal = dictFile.readLine();
			sorted = sortLetters(normal);
			
			combined = sorted + " " + normal;
			dictonary.add(combined);
			
		}
		Collections.sort(dictonary);				//Sort into alphabetical order
		
		ArrayList<String> dWords = new ArrayList<String>();
		ArrayList<String> dSorted = new ArrayList<String>();
		
		
		for (String s : dictonary){					//Split dictonary into 2 arrays, sorted letters (in order) and words (not sorted)
			String[] split = s.split("\\s+");
			combined = split[1];
			
			if (!dSorted.isEmpty() && split[0].equals(dSorted.get(dSorted.size()-1))) {			//If empty, doesnt go out of bounds
				combined += " " + dWords.get(dWords.size()-1);				//add word to same string with other words that matches its alphabetical string
				
				String[] sorter = combined.split("\\s+");					//In order to match output online, dictonary words need to appear on line in alphabetical order, so sort them
				Arrays.sort(sorter);	
				combined = toString(sorter);
				
				dWords.add(combined);													//Add the last string + new word
				dWords.remove(dWords.size()-2);											//Delete old last string		
			} else{
				dSorted.add(split[0]);
				dWords.add(split[1]);
			}
		}
		
		
		String jumbled;									//Basically same use as 'normal' earlier but with a relevant name
		
		while(jumbleFile.ready()){						//reads in jumble file
			jumbled = jumbleFile.readLine();
			sorted = sortLetters(jumbled);
			
			int i = binarySearch(dSorted, sorted);		//dSorted must have all sorts from dict at same index with spaces between to add like this
			if (i != -1)								//i = index with same alphabetical string from dict as the jumble word read in
				jumbled += " " + dWords.get(i);			//Adds jumble word and all dict words for output
			
			jumble.add(jumbled);
		}
		
		Collections.sort(jumble);
		for (String s : jumble)				//Print the final array with for each loop
			System.out.println(s);
		
	}
	
	
	
	//Takes a string and sorts its letters into to alphabetical order
	private static String sortLetters(String word){
		
		char[] arr = word.toCharArray();
		Arrays.sort(arr);
		
		return new String(arr);
	
	}
	
	//Returns the index that the key = its location in words, -1 otherwise
	private static int binarySearch(ArrayList<String> words, String key){
		int lo = 0, mid = 0, hi = words.size();	
		
		while ( lo <= hi )
		{
			mid = lo + (hi-lo)/2;

			if (words.get(mid).compareTo(key) == 0)				
				return mid;
			else if (words.get(mid).compareTo(key) > 0)			
				hi = mid -1;
			else if (words.get(mid).compareTo(key) < 0)
				lo = mid+1;	
			else
				lo = 999;
		}
		return -1;		
	}
	
	//Converts a String[] to a String with spaces between words
	private static String toString(String[] s){
		String total = "";
		for (String str : s)
			total += str + " ";
		return total;
		
	}	
}