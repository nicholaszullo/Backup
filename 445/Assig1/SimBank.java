import java.util.*;
/*	Nick Zullo CS 445 Spring 2019
*
*	SimBank creates a bank open for a given amount of hours, uses a given amount of customers that are serviced for a given time
*	and collects and prints data about that customer's experience in the bank 
*	Stores the queues for the tellers in an ArrayList of LinkedLists
*	Stores the customers in each teller in an Array of the Teller class
*	
*	v3.1
*	Updated to use a Teller class and have Teller[] tellers not Customer[] tellers to comply with rubric
*	Updated the currentCustomersWaiting tally to not include those being serviced only those waiting in line
*	-------NEED TO IMPLEMENT------------------
*
*
*
*/

public class SimBank 
{												
	ArrayList<Queue<Customer>> line; 			//Collection of queues
	int maxCustomers;							//Maximum amount of customers in bank
	int currentCustomersWaiting;				//Current number of customers in bank
	int tellers;								//Number of tellers in bank, 
	int num_queues;								//Number of queues in bank, 1 or K
	double open;								//Number of hours the bank is open
	double arrival_rate;						//Average rate at which customers enter bank, used to generate random numbers
	double transTime;							//Time per transcation
	long seed;									//Seed for random distribution
	int totalCust;								//Total number of customers
	int custServiced;							//Needed so that no two customers can have the same ID after one gets removed from servicedCustomers
	int custWaited;								//Number of customers who waited in a line
	ArrayList<Customer> servicedCustomers;		//Need to track all data for calulations and display at the end
	ArrayList<Customer> left_customers;			//List of customers who left bank
	Teller[] teller;							//List of tellers in bank
	PriorityQueue<SimEvent> fel;				//Future Event List, manages running of time, oldest event pops off queue
	RandDist rand;								//Random object to generate times 
	final int START_TIME;						//Time the simulation started in minutes
	double currentTime;							//Current time in the simulation
	boolean whichBank;							//true for sq bank, false for mq bank
	int[] tellerCustCounter;
	
	public SimBank(int nTeller, boolean singleQueue, double hours, double arrRate, double avgTimePer, int max, long seed1)
	{	
		START_TIME = 0;											//Initialize all variables with given info from constructor
		currentTime = START_TIME;
		whichBank = singleQueue;
		maxCustomers = max;
		currentCustomersWaiting = 0;							//Customers in bank
		custServiced = 0;										//Number of customers who did not leave the bank
		custWaited = 0;
		tellers = nTeller;
		teller = new Teller[nTeller];							//Array with room for all tellers	
		for (int i = 0; i < nTeller; i++){
			teller[i] = new Teller(i);
		}
		
		arrival_rate = arrRate;
		transTime = avgTimePer;
		seed = seed1;
		open = hours*60;										//Time in system recorded in minutes
		num_queues = 0;
		fel = new PriorityQueue<SimEvent>();
		rand = new RandDist(seed);
		servicedCustomers = new ArrayList<Customer>();
		left_customers = new ArrayList<Customer>();
		totalCust = 0;
		line = new ArrayList<Queue<Customer>>();
		
		tellerCustCounter = new int[nTeller];
		
		if (whichBank){											//If the bank has 1 queue, create 1 queue in arraylist of queues
			line.add(new LinkedList<Customer>());
			num_queues++;
		}
		else {
			for (int i = 0; i < tellers; i++){					//If bank has 1 queue per teller, create num teller queues
				line.add(new LinkedList<Customer>());
				num_queues++;
			}
		}
		
	}

	
	//Make a new customer for the bank
	private Customer generateCust(){
		servicedCustomers.add(new Customer(totalCust++, currentTime));
		return servicedCustomers.get(custServiced++);						//Increment number of customers at the end of the method
		
	}
	
	//Add a customer to the bank 
	private void add(Customer cust){
		
		boolean tellerSelected = false;
		for (int i = 0; !tellerSelected && i < teller.length; i++){		//Check if a teller is open, if tellerSelected is not checked every open teller will serve the current customer
			if (teller[i].checkAvail()){
				addToTeller(i, cust);					//Add to open teller
				tellerSelected = true;
				cust.queueLoc = -1;						//The customer did not wait in a queue
			}
		}
		if (!tellerSelected){							//Cust must wait in queue if all tellers full
			whichQueue(cust);
			cust.serviceWaitTimeStart = currentTime;	//Time customer starts waiting in Queue
		}
		
		
	}
	
	
	//Decides which queue to add the customer to
	private void whichQueue(Customer cust){
		
		if (currentCustomersWaiting >= maxCustomers){			//If there are too many customers in the bank, the current customer will leave
			left_customers.add(servicedCustomers.remove(--custServiced));
			return ;									//Break out of method and do not place cust in a queue
		}
		custWaited++;
		if (whichBank){						//Single Queue bank
			line.get(0).add(cust);
			cust.queueLoc = 0;
			currentCustomersWaiting++;	
		} else {							//Multiqueue bank
			int shortestQueue = 0;
			for (int i = 0; i < line.size(); i++){		
				if (line.get(shortestQueue).size() > line.get(i).size()){		//Determine the queue with the fewest number of customers
					shortestQueue = i;
				}
				
			}
			line.get(shortestQueue).add(cust);								//Add customer to shortest queue
			cust.queueLoc = shortestQueue;
			currentCustomersWaiting++;
		}

	}
	
	//Adds a customer to teller # i
	private void addToTeller(int i, Customer cust){
		teller[i].addCust(cust);
		cust.tellerID = i;
		fel.add(new CompletionLocEvent(currentTime + (rand.exponential(1/transTime)), i));			//Add a departure event at how long the cust was being served *60 so in minutes not seconds
		cust.serviceBeginTime = currentTime;
		tellerCustCounter[i]++;
	}
	
	//Remove the customer from the teller it is being served by
	private void removeFromTeller(int i){
		Customer cust = teller[i].getCust();				//Preserve the customer object
		teller[i].removeCust();						//Reset the teller 
		
		if (whichBank){							//If single queue, take next customer from the queue
			if (line.get(0).size() > 0)
				removeFromQueue(i);
		} else {
			if (line.get(i).size() > 0)			//If multi queue, take next customer from the teller queue
				removeFromQueue(i);
		}
	
		cust.serviceEndTime = currentTime;	
	}
	
	//Remove a customer from the queue and add to open teller
	private void removeFromQueue(int i){
		if (whichBank){								//If single queue, access the only queue
			line.get(0).peek().serviceWaitTimeEnd = currentTime;
			addToTeller(i, line.get(0).remove());
		} else {
			line.get(i).peek().serviceWaitTimeEnd = currentTime;
			addToTeller(i, line.get(i).remove());	//Remove from queue i waiting for teller i
		}
		currentCustomersWaiting--;							//Current number of customers waiting in line decreased 
	}
	
	public void runSimulation(){
		fel.add(new ArrivalEvent(rand.exponential(arrival_rate)*60));	//Need to start with a customer for while loop to work
		
		while (fel.size() > 0 && currentTime <= open){				//Simulation runs while bank is open and there are events in queue
			SimEvent currEvent = fel.remove();
			currentTime = currEvent.get_e_time();
			if (currEvent instanceof ArrivalEvent){					//If the event is an arival event, add a customer to the bank
				add(generateCust());
				fel.add(new ArrivalEvent(currentTime + rand.exponential(arrival_rate)*60));		//Keep the loop of new customers being generated going until the bank closes
	
			} else if (currEvent instanceof CompletionLocEvent){	//Remove a cust from its teller if service is complete
				removeFromTeller(((CompletionLocEvent)currEvent).getLoc());				
			}
			
			
		}
		
		while (fel.size() > 0){							//Customers still in the bank after close can be removed from tellers and queues
			SimEvent currEvent = fel.remove();
			currentTime = currEvent.get_e_time();
			if (currEvent instanceof CompletionLocEvent){	
				removeFromTeller(((CompletionLocEvent)currEvent).getLoc());					
			}
		}
		
	}
	
	
	//Print results of simulation in a formatted table 
	//Note: table doesnt format properly if cmd window is too small in length
	public void showResults(){
		ArrayList<Double> avgWaitTot = new ArrayList<Double>();			//Needed to access later to calculate standard deviation
		double maxWait = 0;
		double totWait = 0;
		double totWaitQueue = 0;
		double totServTime = 0;
		double totTotTime = 0;
		
		
		System.out.printf("\n%-15s%-15s%-15s%-12s%-12s%-15s%-17s%-15s%-15s\n", "Customer ID", "Arrival Time" , "Service Time", "Queue Loc", "Teller Loc", "Time Serv Beg", "Time Cust Waits", "Time Serv Ends", "Total Time");
		for (Customer cust : servicedCustomers){			//Customers who stayed in bank
			cust.calcInfo();
			totWait += cust.serviceWaitTime;
			if (cust.serviceWaitTime > 0){
				totWaitQueue += cust.serviceWaitTime;
				avgWaitTot.add(cust.serviceWaitTime);
			}
			if (cust.serviceWaitTime > maxWait)
				maxWait = cust.serviceWaitTime;
			totServTime += cust.serviceTime;
			totTotTime += cust.totTime;
			System.out.printf("%-15d%-15.2f%-15.2f%-12d%-12d%-15.2f%-17.2f%-15.2f%-15.2f\n" , cust.id, cust.arriveTime, cust.serviceTime, cust.queueLoc, cust.tellerID, cust.serviceBeginTime, cust.serviceWaitTime, cust.serviceEndTime, cust.totTime);
		}
		
		System.out.println("\nCustomers who did not stay: ");
		System.out.printf("%-15s%-15s\n" , "Customer ID", "Arrival Time");
		for (Customer cust : left_customers){
			System.out.printf("%-15d%-15.2f\n", cust.id, cust.arriveTime);
		}
		
		System.out.println("\nNumber of queues: " + num_queues);
		System.out.println("Max allowed to wait: " + maxCustomers);
		System.out.println("Customer arrival rate (per hour): " + arrival_rate);
		System.out.println("Customer service time (ave min): " + transTime);
		System.out.println("Number of customers arrived: " + totalCust);
		System.out.println("Number of customers serviced: " + servicedCustomers.size());
		System.out.println("Number of customers turned away: " + left_customers.size());
		System.out.println("Number of customers who waited: " + custWaited);		
		System.out.println("Avg. wait time total: " + totWait/servicedCustomers.size());
		System.out.println("Maximum wait time: " + maxWait);
	
		double avgWait = totWait/servicedCustomers.size();
		double standardDev = 0;
		for (double waitTime : avgWaitTot){							
			standardDev += Math.pow((waitTime-avgWait),2);
			
		}
		standardDev /= avgWaitTot.size();
		standardDev = Math.sqrt(standardDev);
		System.out.println("Standard Deviation of wait time: " + standardDev);
		System.out.println("Avg. service time of customers: " + totServTime/servicedCustomers.size());
		System.out.println("Avg. wait of customers who waited in a queue: " + totWaitQueue/custWaited);
		System.out.println("Avg. total time in bank: " + totTotTime/servicedCustomers.size());
		for (int i = 0; i < tellerCustCounter.length; i++){
			System.out.println("Teller " + i + " serviced " + tellerCustCounter[i] + " customers");
		}
	}
		
}