import java.util.*;
import java.io.*;
/*
*
*		VERSION 1.0
*			
*
*/
public class Jumbles
{
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		
		BufferedReader dictFile = new BufferedReader(new FileReader(args[0]));
		BufferedReader jumbFile = new BufferedReader(new FileReader(args[1]));
		
		TreeMap<String, String> total = new TreeMap<String, String>();
		TreeMap<String, String> jumbdict = new TreeMap<String, String>();
		TreeMap<String, String> jumblesMap = new TreeMap<String, String>();
		
		while (dictFile.ready()){
			String word = dictFile.readLine();
			String sorted = sortLetters(word);
			if (jumbdict.containsKey(sorted))					//If a dupe sorted word exists, add the dict word to the same key as the other dicts 
				word += " " + jumbdict.get(sorted);
				
			String[] sorter = word.split("\\s+");				//Sorts the dict words on the line to match output
			Arrays.sort(sorter);
			word = toString(sorter);
			
			jumbdict.put(sorted, word);
			
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
			} else 
				total.put(jumblesMap.get(jumble), "");			//Need this line to match output, no dict words matching jumble given
		}
		
		for (String jumble : total.keySet())						//Print the jumble and matching dict words
			System.out.println(jumble +" " + total.get(jumble));

			
		long end = System.currentTimeMillis()-start;
		System.out.println("Run time is: " + end);
	}
	
	//Takes a string and sorts its letters into to alphabetical order
	private static String sortLetters(String word){
		
		char[] arr = word.toCharArray();
		Arrays.sort(arr);
		
		return new String(arr);
	
	}
	
	//A toString method that returns a String version of a TreeSet in sorted order with no brackets or commas
	private static String toString(TreeSet<String> set){
		String temp = set.toString();
		temp = temp.substring(1, temp.length()-1);		//Remove Brackets
		String[] temp2 = temp.split("\\s+");
		String temp3 ="";
		for (int i = 0; i < temp2.length; i++){
			if (temp2[i].charAt(temp2[i].length()-1) == ',')
				temp2[i] = temp2[i].substring(0, temp2[i].length()-1); 		//Remove commas
			temp3 += temp2[i] + " ";
		}
		return temp3;								//Return the TreeSet<String> as a string with no brackets or commas
	}
	
	//Converts a String[] to a String with spaces between words
	private static String toString(String[] s){
		String total = "";
		for (String str : s)
			total += str + " ";
		return total;
		
	}	
	
}