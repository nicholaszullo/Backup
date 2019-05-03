import TreePackage.*;
import java.io.*;
import java.util.*;

public class Assig4Driver
{
	
	public static void main(String[] args) throws Exception
	{
		Assig4 huffman = new Assig4(args[0]);						//Make a new file out of the input, assumes valid input
		int selection = 0;
		Scanner scan = new Scanner(System.in);
		
		do 
		{
			System.out.println("Please choose from the following:");
			System.out.println("1) Encode a text string\n2) Decode a Huffman string\n3) Quit");
			selection = scan.nextInt();
			if (selection == 1)										//Encode a string
			{
				System.out.println("Enter a String from the following characters:");
				Character[] letters = huffman.getLetters();			//Display available letters
				for (Character c : letters)
					System.out.print(c);
				System.out.println();
				String input = scan.next();							//Get input string from user to encode
				String[] code = huffman.generateCode(input.toUpperCase());	//Input must be uppercase for ascii encoding. generate the code for the string
				if (code[code.length-1] != null)					//If all letters have been decoded, print the code created
				{
					for (String s : code)							//Display the code line by line
						System.out.println(s);
				}
			} 
			else if (selection == 2)								//Decode a string
			{
				System.out.println("Here is the encoding table:");
				Character[] letters = huffman.getLetters();
				String[] encoding = huffman.getEncoding();
				for (int i = 0; i < letters.length; i++)			//Display the encoding table
					System.out.println(letters[i]+": "+encoding[i]);
				
				System.out.println("Please enter a Huffman string (one line, no spaces)");
				String input = scan.next();							//Get string from user
				String ans = huffman.decode(input);
				if (ans != null)									//If a string is created, print it 
					System.out.println("Text String:\n" + ans);		//Output resulting huffman code
				
				
			} else if (selection == 3)								//Exit the loop condition
				System.out.print("Goodbye");
			else if (selection != 3)								//A number other than 1 2 3 is entered is not valid
				System.out.println("Not a valid input! Only enter 1 2 or 3"); 

		} while (selection != 3);									//If 3 is selected stop the loop
	
	
	}
	
}