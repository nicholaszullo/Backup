import java.util.*;
import java.io.*;

public class AutoParts
{
	public static void main( String[] args ) throws Exception
	{
		BufferedReader num2quantFile = new BufferedReader( new FileReader( "num2quant.txt" ) );
		BufferedReader num2nameFile = new BufferedReader( new FileReader( "num2name.txt" ) );
		
		TreeMap<Integer, String> num2name = new TreeMap<Integer, String>();
		TreeMap<Integer, Integer> num2quant = new TreeMap<Integer, Integer>();
		
		System.out.println("Map of part # => name:"); // LEAVE THIS IN HERE
		while (num2nameFile.ready()){								//Process the file of num2name sorting by part number
			String temp = num2nameFile.readLine();
			String[] tokens = temp.split("\\s+");
			num2name.put(Integer.parseInt(tokens[0]), tokens[1]);
		}
		for (Integer key : num2name.keySet())						//Print the num2name list in order 
			System.out.println(key +"\t"+num2name.get(key));	
		

		
		while (num2quantFile.ready()){								//Process the num2quantFile and store in order by part number 
			String temp = num2quantFile.readLine();
			String[] tokens = temp.split("\\s+");
			num2quant.put(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
		}
		System.out.println("Join of part# => name => quantity:"); // LEAVE THIS HERE
		for (Integer key : num2name.keySet())
			System.out.println(key +"\t" + num2name.get(key) + "\t" + num2quant.get(key));			//Print values of 'key' from both TreeMaps
		

	} // END MAIN

} // END CLASS