import java.util.*;
import java.io.*;

public class Drugs
{
	public static void main( String[] args ) throws Exception
	{
		BufferedReader cat2drugFile	= new BufferedReader(new FileReader("foodDrug2Category.txt"));
		BufferedReader patient2drugFile = new BufferedReader(new FileReader("patient2FoodDrug.txt"));
		BufferedReader dontMixFile = new BufferedReader(new FileReader("dontMix.txt"));
	
		TreeMap<String, TreeSet<String>> cat2drug = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, TreeSet<String>> pat2drug = new TreeMap<String, TreeSet<String>>();
		TreeMap<String, String> dontMix = new TreeMap<String, String>();
		
		while (cat2drugFile.ready()){
			TreeSet<String> drugs = new TreeSet<String>();		//Inside while loop so the TreeSet gets cleared for each new line so each key only has its own elements
			String temp = cat2drugFile.readLine();
			String[] tokens = temp.split(",");			//Split on the comma in the file
			for (int i = 1; i < tokens.length; i++)
				drugs.add(tokens[i]);
			cat2drug.put(tokens[0], drugs);				//Adds the type in aplhabetical order with a list of drugs/foods of that type following it in alphabetical order
		}
		printSet(cat2drug);
		
		while (patient2drugFile.ready()){						//Same as first loop but with the second file
			TreeSet<String> drugs = new TreeSet<String>();		//Can have same name as before bc not in scope of main
			String temp = patient2drugFile.readLine();
			String[] tokens = temp.split(",");			
			for (int i = 1; i < tokens.length; i++)
				drugs.add(tokens[i]);
			pat2drug.put(tokens[0], drugs);				
		}
		
		printSet(pat2drug);
		
		while (dontMixFile.ready()){						//Process the dontMix 
			String temp = dontMixFile.readLine();
			String[] tokens = temp.split(",");
			dontMix.put(tokens[0], tokens[1]);				//only two per line so this is fine
		}	
		
		
		
		TreeSet<String> mixers = new TreeSet<String>();
		
		for (String name : pat2drug.keySet()){
			TreeSet<String> patCats = new TreeSet<String>();	
			for (String drug : pat2drug.get(name)){				//Loads a TreeSet of categorys of drug/food a patient is using
				for (String cat : cat2drug.keySet()){
					if (cat2drug.get(cat).contains(drug))
						patCats.add(cat);
				}
			}
			
			for (String mix1 : dontMix.keySet()){				//Checks to see if 2 things that dont mix are in the cats that a patient is taking
				if (patCats.contains(mix1)){
					if (patCats.contains(dontMix.get(mix1)))
						mixers.add(name);
					
				}
			}
	
		}
		
		System.out.println();					//Print the set of people who are mixing
		for (String name : mixers)
			System.out.println(name);
			
		
		
	} // END MAIN
	
	private static void printSet(TreeMap<String, TreeSet<String>> set){
	
		for (String key : set.keySet()){
			System.out.print(key +" ");
			for (String item : set.get(key))
				System.out.print(item + " ");
			System.out.println();
	
		}
	}
		
} // END CLASS