
/*	Nick Zullo CS 445 Spring 2019
*
*	Stores information about the customer
*	Time entered, time left, customer ID (order generated)
*
*
*/

public class Customer
{
	int id, tellerID, queueLoc;									
	double arriveTime, transactionTime, serviceWaitTimeStart, serviceWaitTimeEnd, serviceWaitTime, serviceBeginTime, serviceEndTime, serviceTime, totTime;		//Various info values about each customer
	
	
	public Customer(int num, double arrival){
		id = num;
		arriveTime = arrival;
		transactionTime = 0;				//Stores all the stats about a customer, no need for setters and getters just update directly from SimBank
		serviceWaitTimeStart = 0;
		serviceWaitTimeEnd = 0;
		serviceWaitTime = 0;
		serviceBeginTime = 0;
		serviceEndTime = 0;
		serviceTime = 0;
		totTime = 0;
		tellerID = 0;
		queueLoc = 0;
	}
	
	public int getID(){
		return id;
	}

	//Calculates the statistics for the customer
	public void calcInfo(){
		totTime = (serviceEndTime - arriveTime);
		serviceWaitTime = (serviceWaitTimeEnd - serviceWaitTimeStart);
		serviceTime = (serviceEndTime - serviceBeginTime);				
	}
		
}