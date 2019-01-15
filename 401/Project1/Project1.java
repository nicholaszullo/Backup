// Project1.java

import java.io.*; // BufferedReader
import java.util.*; // Scanner to read from a text file

public class Project1
{
	public static void main (String args[]) throws Exception // we NEED this 'throws' clause
	{
		// ALWAYS TEST FIRST TO VERIFY USER PUT REQUIRED CMD ARGS
		if (args.length < 3)
		{
			System.out.println("\nusage: C:\\> java Lab2 <input file name> <lo>  <hi>\n\n"); 
			// i.e. C:\> java Lab2 L2input.txt 1 30
			System.exit(0);
		}
		String infileName = args[0];	// grab args[0] and store into a String var named infileName
		String grabber = args[1];		// grab args[1] and conver to int then store into a var named lo
		int lo = Integer.parseInt(grabber);
		grabber = args[2]; 				// grab args[2] and conver to int then store into a var named hi
		int hi = Integer.parseInt(grabber);
		
		// STEP #1: OPEN THE INPUT FILE AND COMPUTE THE MIN AND MAX. NO OUTPUT STATMENTS ALLOWED
		Scanner infile = new Scanner( new File(infileName) );
		int min,max;
		min=max=infile.nextInt(); // WE ASSUME INPUT FILE HAS AT LEAST ONE VALUE
		
		while ( infile.hasNextInt() )
		{
			int num = infile.nextInt();

			if (num < min)			//If the number is less, make it the min
				min = num;
			else if (num > max)		//If the number is bigger, make it the max
				max = num;
			
		}

		System.out.format("min: %d max: %d\n",min,max); // DO NOT REMOVE OR MODIFY IN ANY WAY


		// STEP #2: DO NOT MODIFY THE REST OF MAIN. USE THIS CODE AS IS 
		// WE ARE TESTING EVERY NUMBER BETWEEN LO AND HI INCLUSIVE FOR
		// BEING PRIME AND/OR BEING PERFECT 
		for ( int i=lo ; i<=hi ; ++i)
		{
			System.out.print( i );
			if ( isPrime(i) ) System.out.print( " prime ");
			if ( isPerfect(i) ) System.out.print( " perfect ");
			System.out.println();
		}
	} // END MAIN
	
	// *************** YOU FILL IN THE METHODS BELOW **********************
	
	// RETURNs true if and only if the number passed in is perfect
	static boolean isPerfect( int n )
	{	
		int sum = 0;
		
		for (int i = 1; i <= n/2; i++){		//Finds the factors of a number
			
				if (n % i ==0)				//If the number is a factor, add it to the sum
					sum += i;
		}
		
		return sum == n;					//If the sum == the number, it is perfect; return true. Else, return false
	
	}
	
	// RETURNs true if and only if the number passed in is prime
	static boolean isPrime( int n )
	{	
		boolean found = false;		
		int i = 3;			//Starts at 3 because 0, 1, 2 are unnecessary to be checked in loop because of if else ladder beforehand
		int prime;
		
		if (n == 2)			//if the number is 2, stop, it is prime. Only needs checked once, so placed outside loop
			return true;
		else if (n==1)
			return false;	//If the number is 1, stop, it is not prime.
		else if (n % 2 == 0)		//If even, it is not prime (except 2)
			return false;
										//Acconts for all numbers greater than 2 
		while (!found && i <= n/2)		//Stops testing if a number divides evenly or all values are tested
		{								//If a factor that divides evenly is found, stop the loop
			
			prime = n % i;		//Checks to see if a number divides evenly
			if (prime == 0)
				found = true;		//SHOULD BE RETURN TRUE; LOOP SHOULD NOT HAVE ANY BOOLEAN, IT IS UNNECESSARY -10
			else 			//Adds 2 to skip testing even numbers 
				i += 2;
			
		}
		
		return !found; 
	}
} // END Project1 CLASS

