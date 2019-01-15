import java.util.*;
import java.io.*;

public class Potus
{
	public static void main( String[] args )  throws Exception
	{
		BufferedReader infile1 = new BufferedReader( new FileReader("state2Presidents.txt") );
		BufferedReader infile2 = new BufferedReader( new FileReader("allPresidents.txt") );
		BufferedReader infile3 = new BufferedReader( new FileReader("allStates.txt") );

		
		TreeMap<String, String> state2Presidents = new TreeMap<String, String>();			//State mapped to a pres
		TreeSet<String> presidents = new TreeSet<String>();									//Sorts the list of pres as they come in from infile1
		TreeMap<String, String> pres2state = new TreeMap<String, String>();					//Pres mapped to a state
			
		while (infile1.ready()){
			String temp = infile1.readLine();
			String[] tokens = temp.split("\\s+");								//Split the line into state as [0] and presidents as the rest
			String state = tokens[0];
			for (int i = 1; i < tokens.length; i++){								//add presidents in sorted order
				presidents.add(tokens[i]);
				pres2state.put(tokens[i], state);
			}
			String transfer = toString(presidents);								//converts TreeSet to a string
	
			state2Presidents.put(state, transfer);								//Cant just add TreeSet to Map because it will fill with all presidents
			presidents.clear();
		}
	
		for (String state : state2Presidents.keySet()){							//Print state2president
			System.out.println(state + " " + state2Presidents.get(state));
		}
		System.out.println(); 
		
		for (String pres : pres2state.keySet()){								//Print the inverse president2state
			System.out.println(pres + " " + pres2state.get(pres));
		}


		System.out.println(); 
		
		TreeSet<String> allPres = new TreeSet<String>();				//Read the file of all presidents
		while(infile2.ready()){
			allPres.add(infile2.readLine());
		}
		
		for (String pres : allPres){									//Check and see if the pres has a state 
			if (!(pres2state.keySet().contains(pres)))
				System.out.println(pres);
		}
			
		System.out.println(); 
		
		TreeSet<String> allStates = new TreeSet<String>();
		while (infile3.ready()){									//Read the file of all states
			allStates.add(infile3.readLine());
		}
		for (String state : allStates){								//Check if the state has a president
			if (!(state2Presidents.keySet().contains(state)))		
				System.out.println(state);
		}
		

	} // END MAIN
	
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
	
}	// END POTUS CLASS