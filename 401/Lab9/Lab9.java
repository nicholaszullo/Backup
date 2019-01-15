import java.io.*;
import java.util.*;

public class Lab9
{
	public static void main(String args[]) throws Exception
	{
		BufferedReader input = new BufferedReader(new FileReader(args[0]));		//Switched from scanner to buffered reader to be faster
		HashSet<String> list1 = new HashSet<String>();
		
		while (input.ready()){
			if (!(list1.add(input.readLine()))){		//If the add was unsuccessfl, then that element is a dupe, so print not unique and stop
				System.out.println("NOT UNIQUE");		
				System.exit(0);
			}
		}
		System.out.println("UNIQUE");					//If it got here, the file is unique
		input.close();									//Close the scanner
	}
}