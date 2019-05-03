/*	Nick Zullo CS 445 Spring 2019
*
*	Provides info if the teller is available or not and the customer stored in the teller
*	Not that necessary, program worked without this class but had to add it after rubric was posted and said I needed it
*
*/
public class Teller
{
	int id; 					//Teller ID number
	boolean open;				//If teller is open for next customer
	Customer currCust;
	
	public Teller(int num)
	{
		id = num;
		open = true;
	}

	//Check if the teller is able to serve next customer
	public boolean checkAvail(){
		return open;
	}
	
	//Change the teller from open to closed or closed to open
	private void setAvail(boolean newCondition){
		open = newCondition;
	}
	
	//Add a customer to the teller
	public void addCust(Customer cust){
		currCust = cust;
		setAvail(false);
	}
	
	//Remove the customer from the teller
	public void removeCust(){
		currCust = null;
		setAvail(true);
		
	}
	
	//Return the customer currently at the teller
	public Customer getCust(){
		return currCust;
	}


}	