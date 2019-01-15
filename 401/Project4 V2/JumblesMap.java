import java.util.*;
import java.io.*;
/*
*		VERSION 2.0
*		Converted from using and sorting an array when reading in file to using TreeSets
*		Reduced runtime by ~300 ms on my laptop
*
*/
public class JumblesMap
{
	public static void main(String[] args) throws Exception
	{
		
		BufferedReader dictFile = new BufferedReader(new FileReader(args[0]));
		BufferedReader jumbFile = new BufferedReader(new FileReader(args[1]));
		
		TreeMap<String, TreeSet<String>> total = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, TreeSet<String>> jumbdict = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, String> jumblesMap = new TreeMap<String, String>();
		
		
		while (dictFile.ready()){	
			TreeSet<String> line = new TreeSet<String>();				
			String word = dictFile.readLine();
			String sorted = sortLetters(word);				//Sorts the letters in the word in alphabetical order
			
			if (jumbdict.containsKey(sorted)){				//If the key is already in the set, add word to that line
				jumbdict.get(sorted).add(word);
			} else {
				line.add(word);							//Create a new line in jumbdict for that key
				jumbdict.put(sorted,line);
			}
		}
		while (jumbFile.ready()){
			String jumble = jumbFile.readLine();
			String sorted = sortLetters(jumble);
			if (jumblesMap.containsKey(sorted))						//If a dupe sorted word exists, add the jumble word to the same key as the other jumble 
				jumble += " " + jumblesMap.get(sorted);
			jumblesMap.put(sorted, jumble);
			
		}
		
		for (String jumble : jumblesMap.keySet()){				//If a jumble is found in jumblesMap that matches to a dict word's jumble, 
			if (jumbdict.containsKey(jumble)){					//add it to total found
				total.put(jumblesMap.get(jumble), jumbdict.get(jumble));
				
			} else {
				total.put(jumblesMap.get(jumble), new TreeSet<String>());			//Need this line to match output, no dict words matching jumble given
				
			}
		}
	
		for (String jumble : total.keySet())						//Print the jumble and matching dict words
			System.out.println(jumble +" " + toString(total.get(jumble)));
		

	}
	
	//Takes a string and sorts its letters into to alphabetical order
	private static String sortLetters(String word){
		
		char[] arr = word.toCharArray();
		Arrays.sort(arr);
		
		return new String(arr);
	
	}
	
	//A toString method that returns a String version of a TreeSet in sorted order with no brackets or commas
	private static String toString(TreeSet<String> set){
		if (set.isEmpty())
			return "";
	
		String temp = set.toString();
		temp = temp.substring(1, temp.length()-1);		//Remove Brackets
		String[] temp2 = temp.split("\\s+");
		String temp3 ="";
		for (int i = 0; i < temp2.length; i++){
			if (temp2[i].charAt(temp2[i].length()-1) == ',')				//Checks each word to see if it ends with a comma
				temp2[i] = temp2[i].substring(0, temp2[i].length()-1); 		//Remove commas
			temp3 += temp2[i] + " ";
		}
		return temp3;								//Return the TreeSet<String> as a string with no brackets or commas
	}
	
	
}