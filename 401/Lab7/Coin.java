/*
	Coin.java THIS IS THE ONLY FILE YOU HAND IN
	THERE IS NO MAIN METHOD IN THIS CLASS!
*/
import java.util.Random;
public class Coin
{
	private int numHeads, numTails;
	private final int TAILS = 0; 			//The value of the coin is tails if it is 0
	private Random rand;					//Declare variables in scope of whole class
	
	public Coin(int seed)
	{
		rand = new Random(seed);		//Seed the random with number given
		reset();						//Set the variables to 0
	}
	
	public Coin(){
		rand = new Random();			//Initialize random object
		reset();
	}
	
	public String flip(){
		int n = rand.nextInt(2);		//Generate a random number from 0 to 1
		if (n == TAILS){					//If tails, add 1 to tails count
			setNumTails(getNumTails()+1);
			return "T";
		}else {							//If not tails, then add to heads
			setNumHeads(getNumHeads()+1);
			return "H";
		}
	}
	
	//Set new value of tails or heads
	private void setNumTails(int n){
		numTails = n;
	}
	private void setNumHeads(int n){
		numHeads = n;
	}
	
	//Returns the number of times the coin has been tails/heads after last reset
	public int getNumTails(){
		return numTails;
	}
	public int getNumHeads(){
		return numHeads;
	}
	
	public void reset(){
		setNumTails(0);
		setNumHeads(0);
	}
} // END COIN CLASS