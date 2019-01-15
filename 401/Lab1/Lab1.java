// Lab1.java STARTER FILE

import java.io.*; // I/O
import java.util.*; // Scanner class

public class Lab1
{
	public static void main( String args[] ) 
	{
		final double MILES_PER_MARATHON = 26.21875; // i.e 26 miles 285 yards
		final double SEC_PER_MIN = 60.0;			//	ADDED TWO FINAL VARIABLES TO USE IN CONVERSIONS 
		final double SEC_PER_HOUR = 3600.0;

		Scanner kbd = new Scanner (System.in);

		// THE FOLLOWING THREE VARIABLES ARE PRINTED OUT AT THE END OF THE PROGRAM
		double aveMPH=0, aveMinsPerMile=0, aveSecsPerMile=0;

		// YOUR JOB IS TO DO WHAT YOU HAVE TO TO TO PUT THE CORRECT VALUES INTO THEM
		// BEFORE THEY GET PRINTED OUT AT THE BOTTOM

		System.out.print("Enter marathon time in hrs minutes seconds: "); // i.e. 3 49 37
		// DO NOT WRITE OR MODIFY ANYTHING ABOVE THIS LINE
		
		int hours, mins, secs;
		hours = kbd.nextInt();
		mins = kbd.nextInt();		//Store numbers for calculations
		secs = kbd.nextInt();
		
		//CHANGED FROM mins/60.0 and secs/60.0/60.0 TO USING FINAL VARIABLES LIKE STATED IN FEEDBACK
		double totalTimeHours = hours + (mins/SEC_PER_MIN) + (secs/SEC_PER_HOUR);			//Converts time into hours with a decimal from hours minutes seconds
																				//NEEDED TO MAKE EACH CONVERSION RATE A FINAL VARIABLE -15
		aveMPH = MILES_PER_MARATHON / totalTimeHours;
		aveMinsPerMile = (60.0/aveMPH);							//Contains the minutes per mile but with a decimal (0-99) of how many seconds left over
		aveSecsPerMile =  aveMinsPerMile % 1 * 60;				//Converts seconds from 0-99 to 0-60
		aveMinsPerMile = (int)aveMinsPerMile;					//Truncates the decimal on the minute variable

		// DO NOT WRITE OR MODIFY ANYTHING BELOW THIS LINE. JUST LET MY CODE PRINT THE VALUES YOU PUT INTO THE 3 VARS
		System.out.println();
		System.out.format("Average MPH was: %.2f mph\n", aveMPH  );
		System.out.format("Average mile split was %.0f mins %.1f seconds per mile", aveMinsPerMile, aveSecsPerMile );
		System.out.println();

	} // END MAIN METHOD
} // EOF