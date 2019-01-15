import java.io.*;
import java.util.*;

public class Coding
{
	public static void main(String args[]) throws Exception
	{
			BufferedReader athlete2sportsFile = new BufferedReader( new FileReader( "athlete2sports.txt" ) );
			BufferedReader whoPlayedFile = new BufferedReader( new FileReader( "whoPlayed.txt" ) );
			
			// CODE HERE TO PRINT SECTION #1 OUTPUT
			TreeMap<String, String> ath2sport = new TreeMap<String, String>();			
			TreeSet<String> sports = new TreeSet<String>();									
			TreeMap<String, String> sport2ath = new TreeMap<String, String>();	

			
			while (athlete2sportsFile.ready()){
				String temp = athlete2sportsFile.readLine();
				String[] tokens = temp.split("\\s+");		
				
				for (int i = 1; i < tokens.length; i++){
					sports.add(tokens[i]);
					if (!(sport2ath.containsKey(tokens[i]))){
						sport2ath.put(tokens[i], tokens[0]);
					} else{
						String temp1 = tokens[0];
						temp1 += " " + sport2ath.get(tokens[i]);
						sport2ath.put(tokens[i], temp1);
					}
						
						
				}
				
				String transfer = toString(sports);								//converts TreeSet to a string
		
				ath2sport.put(tokens[0], transfer);								
				sports.clear();
			}
			
		for (String name : ath2sport.keySet()){							//Print ath2sport
			System.out.println(name + " " + ath2sport.get(name));
		}
		// CODE HERE TO PRINT SECTION #2 OUTPUT
		System.out.println(); // LEAVE THIS HERE TO SEPARATE SECTIONS
		
		for (String sport : sport2ath.keySet()){
			
			String[] temp = sport2ath.get(sport).split("\\s+");
			Arrays.sort(temp);
			String names = "";
			for (String name : temp)
				names += name + " ";
			
			sport2ath.put(sport,names);
			
		}
		
		for (String sport : sport2ath.keySet()){								//Print the inverse 
			System.out.println(sport + " " + sport2ath.get(sport));
		}
		// CODE HERE TO PRINT SECTION #3 OUTPUT
		System.out.println(); // LEAVE THIS HERE TO SEPARATE SECTIONS
			
			
		TreeSet<String> listOfPairs = new TreeSet<String>();
		TreeMap<String, String> total = new TreeMap<String, String>();
		
		while (whoPlayedFile.ready()){
			String pair = whoPlayedFile.readLine();
			listOfPairs.add(pair);
			String temp5 = "";
			String[] flip = pair.split("\\s");
			String pair1 = "z";
			if (flip.length > 1){
				
				pair1 = flip[1] + " " + flip[0];

			}
			
			for (String name : ath2sport.keySet()){
				if (ath2sport.get(name).contains(pair) || ath2sport.get(name).contains(pair1))
					temp5 += name + " ";
				
			}
			total.put(pair,temp5);
			
		}
		for (String s : total.keySet()){
			System.out.println(s + " " + total.get(s));
		}

		
	} // END MAIN

	// ------------------YOUR METHODS HERE -----------------------------
	
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
		return temp3;	
	}

} // END CLASS
