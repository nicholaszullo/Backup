import TreePackage.*;
import java.io.*;
import java.util.*;

public class Assig4 
{
	private BinaryNode<Character> root;
	private int id, totLeaf;
	private Character[] letters;
	private String[] encoding;
	
	
	public Assig4(String file) throws Exception						//FileReader throws FileNotFoundException
	{
		id = 0;														//Track the number of nodes created
		totLeaf = 0;												//Track the number of leaf nodes to create an array with size for only the number needed
		init(new FileReader(file));									//Creates the tree from given input file
		createTable();												//Creates the encoding table from the tree
	}
	
	private void init(FileReader input) throws Exception 			//BufferedReader throws IO exception
	{
		BufferedReader read = new BufferedReader(input);			//Create new reader to read the file
		recInit(read.readLine(), read);
	}
	
	private BinaryNode<Character> recInit(String line, BufferedReader read) throws Exception
	{
		BinaryNode<Character> temp = new BinaryNode<>('\0');		//Add temporary data to the node
		temp.setID(id++);
		if (temp.getID() == 0)										//If it is the first node, make it the root node
			root = temp;
		
		if (line.charAt(0) == 'I'){									//If an interior node
			temp.setLeftChild(recInit(read.readLine(), read));		//Set the left part of the tree 
			temp.setRightChild(recInit(read.readLine(), read));		//Set the right part of the tree
			temp.setType(0);
		} else if (line.charAt(0) == 'L'){							//Base case if a leaf node
			temp.setData(line.charAt(2));							//Overwrite temp data to be real data 
			temp.setType(1);
			totLeaf++;
			return temp;											//Return the leaf node to be set as the left or right of the interior node above it
		}
		return temp;												//If not a leaf node, return the interior node to be set to the interior node above it
		
	}
	
	public Character[] getLetters()
	{
		return letters;												//Return the letters available
	}
	public String[] getEncoding()
	{
		return encoding;											//Return the encoding to be indexed in order with letters[]
	}
	
	public void createTable(){
		letters = new Character[totLeaf];							//Initialize the 2 arrays
		encoding = new String[totLeaf];
		recTable(new StringBuilder(), root, letters, encoding);		//Create the table and fill arrays
		
	}

	private void recTable(StringBuilder code, BinaryNode<Character> curr, Character[] letters, String[] encoding){
		
		if (curr.getData() == '\0'){
			code.append("0");										//Add a 0 to code when branching left
			recTable(code, curr.getLeftChild(), letters, encoding); //i is incremented on the way up recursion
			code.deleteCharAt(code.length()-1);						//Remove 0 after finishing left branch
			
			code.append("1");										//Add a 1 to code when branching right
			recTable(code, curr.getRightChild(), letters, encoding);					
			code.deleteCharAt(code.length()-1);						//Remove the 1 after finishing the right
		} else {
			int ascii = curr.getData(); 							//Ascii value of char, add to array based on ascii index, ASSUMES input of all capital letters, and no letter is skipped in tree
			encoding[ascii-65] = code.toString();					//Add the code for the letter 
			letters[ascii-65] = curr.getData();						//Add the letter that matches the code to the same index as its letter
		}
	
	}
	
	public String[] generateCode(String input)
	{
		return recCode(new StringBuilder(input), new String[input.length()], 0);
	}
	
	private String[] recCode(StringBuilder curr, String[] code, int i)
	{
		if (curr.length() == 0)										//Base case no more letters to encode
			return code;
		
		int ascii = curr.charAt(0);
		if (ascii-65 >= letters.length || ascii-65 < 0)				//Base case the current letter is not supported
		{
			System.out.println("Error! Enter only the supported characters and try again!");
			return code;											//Stop recursion and return back up 
		}
		
		code[i] = (encoding[ascii-65]);								//If supported letter, store its encoding in the array to be returned and printed 
		recCode(curr.deleteCharAt(0), code, (i+1));	
		return code;
	}
	
	public String decode(String input)
	{
		StringBuilder result = new StringBuilder();
		if (recDecode(new StringBuilder(input), result, root, false))
			return result.toString();								//If successfully created return the string
		else 
			return null;											//If not created return null
	}

	private boolean recDecode(StringBuilder input, StringBuilder result, BinaryNode<Character> curr, boolean created)
	{
		try
		{
			if (curr.getType() == 1)								//If a leaf is reached add its data to the result
			{
				result.append(curr.getData());
				created = true;
			}
			else if (input.charAt(0) == '0')						//Go left in tree if a 0
			{
				input.deleteCharAt(0);								//Delete the 0 from input
				created = recDecode(input,result,curr.getLeftChild(),created);
			}
			else if (input.charAt(0) == '1')						//Go right in tree if a 1
			{
				input.deleteCharAt(0);								//Delete the 1 from the input
				created = recDecode(input,result,curr.getRightChild(), created);
			}
			else													//If the current number is not a 0 or 1 it is an error
			{
				System.out.println("Error! only enter supported 0 and 1 sequences!");
				created = false;									//If not successfully created, set to false
				return created;
			}
			if (input.length() != 0)								//After recursion check if finished with the input
			{
				created = recDecode(input,result,root,created);		//Start back at root of tree to find char for next sequence
			}
		}
		catch (Exception StringIndexOutOfBoundsException)			//If the sequence does not match any from table this execption will be thrown
		{
			System.out.println("Error! Not a supported sequence!");
			created = false;										//If not successfully created set to false
		}
		return created;
		
	}
	
}