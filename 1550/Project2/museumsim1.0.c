#include "sem.h"
#include "unistd.h"
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <sys/time.h>
#include <sys/mman.h>

typedef struct shared_mem{
	int visitors;									//The total arrived visitors
	int guides;										//The total arrived guides
	int numInMuseum;								//The current number of visitors in the museum
	int visitorsWaiting;							//The current number of visitors waiting outside museum
	int guidesWaiting;								//The current number of guides waiting for visitors to give a tour
	int guidesArriving;								//The total guides yet to arrive
	int visitorsArriving;							//The total visitors yet to arrive
	struct cs1550_sem* exit;						//Guide cant leave with visitors
	struct cs1550_sem* maxtwo;						//2 guides max
	struct cs1550_sem* maxten;						//10 visitors per guide max
}shared_mem; 

shared_mem* shared;									//Global declaration of shared struct for use in all methods
struct timeval starttime;							//Global declarion of starttime struct for use in all methods
int number;											//The ID of the visitor or tour guide. Set on the process level and unique to each process

void down(struct cs1550_sem *sem) {					//Wrapper for down syscall
  syscall(__NR_cs1550_down, sem);
}

void up(struct cs1550_sem *sem) {					//Wrapper for up syscall
  syscall(__NR_cs1550_up, sem);
}

/**
 * 		A tour guide opens the museum if there will be visitors to give a tour. 
 * 		Only 2 guides may open the museum at a time
 * 		If tour guide is not needed to process visitors, leave 
 */
void openMuseum(){
	struct timeval curr;							//Stores the current time
	int i;

	shared->guidesWaiting++;
	down(shared->maxtwo);							//Only 2 guides at a time can open musuem 
	shared->guidesWaiting--;

	if (shared->visitorsWaiting == 0 && shared->visitorsArriving == 0){			//If no visitors waiting and no more arriving, dont open 
		return;
	}

	for (i = 0; i < 10; i++){						//Allow 10 visitors in museum for this guide
		up(shared->maxten);
	}

	gettimeofday(&curr, NULL);						//Fill values of struct with current time
	printf("Tour guide %d opens the museum for tours at time %d.\n", number, (int)(curr.tv_sec - starttime.tv_sec));
	fflush(stdout);

}

/**
 * 		A visitor tours the museum for 2 seconds
 * 		Only 20 visitors can tour the museum at time
 * 		A visitor is waiting if more guides will come at a later time
 * 		If no guides will come, leave the museum instantly and do not deadlock 
 */
void tourMuseum(){
	struct timeval curr;							//Stores the current time

	//NOTE: This will cause visitors 0-9 to leave and 10-19 to tour if -m 20 and -k 1. Is that okay?
	if (shared->maxten->value <=0 && (shared->visitorsArriving - (shared->guidesArriving*10)) >= 0 && shared->guidesWaiting == 0)		
		return;										//If no guides waiting or arriving, and max has been reached dont enter deadlock												
	
	shared->visitorsWaiting++;						//Will wait if not enter
	down(shared->maxten);							//Enter museum
	shared->numInMuseum++;							//Enter museum
	shared->visitorsWaiting--;						//In museum so not waiting

	gettimeofday(&curr, NULL);  
	printf("Visitor %d tours the museum at time %d\n", number, (int)(curr.tv_sec - starttime.tv_sec) );
	fflush(stdout);
	sleep(2);
}

/**
 * 		A tour guide cannot leave until the museum is empty
 * 		A tour guide should not leave while museum is empty if there are more visitors to process (up to 10 per tour guide!)
 * 		A tour guide should release another tour guide when it leaves 
 */
void tourguideLeaves(){
	struct timeval curr;

	if (number == 0 || (shared->numInMuseum == 0 && (shared->visitorsArriving > 0 || shared->visitorsWaiting > 0))){		//If just opened museum, wait for some visitors to enter before trying to leave
		down(shared->exit);
	}
	if (shared->numInMuseum > 0 ){					//Check if there is still visitors in museum, wait if so
		down(shared->exit);
	}
	if (shared->maxten->value > 0 && shared->visitorsArriving > 0){		//Check if more visitors will arrive, wait if so
		down(shared->exit);
	}
	up(shared->maxtwo);								//Allow another guide to open tours

	gettimeofday(&curr, NULL);
	printf("Tour guide %d leaves the museum at time %d.\n", number,  (int)(curr.tv_sec - starttime.tv_sec) );
	fflush(stdout); 

}

/**
 * 		A visitor leaving only triggers something if it is the last visitor in the museum
 * 		Release tour guides to leave if it is the last visitor
 */
void visitorLeaves(){
	struct timeval curr;
	
	shared->numInMuseum--;							//Leaving museum

	gettimeofday(&curr, NULL);
	printf("Visitor %d leaves the museum at time %d\n", number, (int)(curr.tv_sec - starttime.tv_sec));
	fflush(stdout);
	
	if (shared->numInMuseum == 0){					//Release all tour guides waiting for empty museum
		printf("The museum is now empty.\n");		//Print whenever the museum is empty
		fflush(stdout);
		while (shared->exit->value < 0)
			up(shared->exit);
	}

}
/**
 *		This method is the hub for the visitor processes
 *		Forks m times, once for each arriving visitor
 * 		The parent process must wait m times for each created process to prevent zombies
 */
void visitorArrives(int m, int pv, int dv, int sv){
	int i;
	struct timeval curr;
	srand(sv);										//Needs to be at top and not in else{} or rand() returns same value
	for (i = 0; i < m; i++){
		int pid = fork();							//Create a visitor process
		if (pid == 0){								//Arriving visitor process
			number = shared->visitors;
			shared->visitors++;						//Start from 0 (place before to start from 1)
			shared->visitorsArriving--;				//Decrease total visitors left to arrive 

			gettimeofday(&curr, NULL);
			printf("Visitor %d arrives at time %d.\n", number, (int)(curr.tv_sec - starttime.tv_sec));
			fflush(stdout);

			tourMuseum(); 							//Try to enter the museum
			visitorLeaves();						//Leave after touring 

			exit(0);								//Do not run the wait for loop 
		} else {									//Parent
			int value = rand() % 100 + 1;
			if (value > pv) {						//100-pv chance of sleeping, pv chance of new visitor
				sleep(dv);
			}
		}
	}

	for (i = 0; i < m; i++){
		wait(NULL);									//Wait for all created visitors
	}	
}

/**
 * 		Hub for tour guide processes
 * 		The exactly same as visitor arrival, just changing the arguments and calls openMuseum and tourguideLeaves instead
 */
void tourguideArrives(int k, int pg, int dg, int sg){
	int i;
	struct timeval curr;
	srand(sg);
 	for (i = 0; i < k; i++){
		int pid = fork();
		if (pid == 0){
			number = shared->guides;
			shared->guides++;
			shared->guidesArriving--;

			gettimeofday(&curr, NULL);
			printf("Tour guide %d arrives at time %d.\n", number, (int)(curr.tv_sec - starttime.tv_sec));
			fflush(stdout);

			openMuseum(); 
			tourguideLeaves();

			exit(0);
		} else {
			int value = rand() % 100 + 1;
			if (value > pg) {						//100-pv chance of sleeping, pv chance of new visitor
				sleep(dg);
			}
		}
 	}
 	for (i = 0; i < k; i++){  
 		wait(NULL);									//Wait for all guide processes
 	}
}


void main(int argc, char *argv[]){
	int opt, m, k, pv, dv, sv, pg, dg, sg;
	int temp;	
	gettimeofday(&starttime, NULL);

	shared = (shared_mem*)mmap(NULL, sizeof(shared_mem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);		//Allocated shared memory space 
	if ((int)shared == -1){
		printf("UNABLE TO ALLOCATE MEMORY FOR SEMAPHORES\n");
		fflush(stdout);
		exit(0);
	}

	shared->guides = 0; 							//Initialize members of shared struct
	shared->visitors = 0;
	shared->numInMuseum = 0;
	shared->visitorsWaiting = 0;
	shared->guidesWaiting = 0;
	shared->exit = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);			//Allocate memory for shared structs (segfault if dont, use malloc? will malloc be shared memory stil?)
	shared->maxtwo = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	shared->maxten = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	shared->exit->value = 0;						//Semaphore exit starts at 0
	shared->maxtwo->value = 2;						//2 guides allowed to start
	shared->maxten->value = 0;						//0 visitors allowed in museum to start (no tour guides yet)

	//Complex way of handling input to accept in any order and allowing more than one character to follow a - (ex. -pv)
	struct option long_options[]={							//The name of argument, macro for 1, flag to set (0 if no flag), value to set flag ('h' if shorter version and no flag)
		{"pv", required_argument, &temp, 1},
		{"dv", required_argument, &temp, 2},
		{"sv", required_argument, &temp, 3},
		{"pg", required_argument, &temp, 4},
		{"dg", required_argument, &temp, 5},
		{"sg", required_argument, &temp, 6},
		{0, 0, 0, 0}
	};
	int option_index = 0;
	while ((opt = getopt_long_only(argc, argv, "m:k:", long_options, &option_index)) != -1){		//getopt returns an integer representation of the argument, -1 when no more arguments
		switch (opt){
			case 'm':							//The number of visitors
				m = atoi(optarg);				//optarg is a char* of argument following m, atoi converts string to integer
				break;
			case 'k':							//The number of tour guides
				k = atoi(optarg);
				break;
			case 0:								//A more than 1 letter flag will be read as a 0, and temp is set to value in 4th column 
				switch (temp){
				case 1:							//the probability of a vistor immediately following another visitor
					pv = atoi(optarg);
					break;
				case 2:							//The delay when a visitor does not immediately follow a visitor
					dv = atoi(optarg);
					break;
				case 3:							//random seed for visitor arrival
					sv = atoi(optarg);
					break;
				case 4:							//Probability of tour guide immediately following another tour guide
					pg = atoi(optarg);
					break;
				case 5:							//The delay when a tour guide does not immediately follow a tour guide
					dg = atoi(optarg);
					break;
				case 6:							//Random seed for tour guide arrival
					sg = atoi(optarg);
					break;
				default:
					printf("Unknown long argument provided! %d\n", temp);
					break;
				}
				break;
			default: 							//'?' is the unspecified argument value of opt
				printf("Unknown argument provided! %c\n", optopt);
				break;

		}
	}

	shared->guidesArriving = k;					//Initialize number of guides to arrive
	shared->visitorsArriving = m;				//Number of visitors yet to arrive
	printf("The museum is now empty.\n");		//Start with an empty museum
	fflush(stdout);

	int pid = fork();							//Begin creating processes
	if (pid == 0){								//Visitor arrival is child
		visitorArrives(m, pv, dv, sv);
	} else {									//Guide arrival is parent
		tourguideArrives(k, pg, dg, sg);
	 	wait(NULL);								//Wait for Visitor arrival process
	}

	munmap(shared->exit, sizeof(struct cs1550_sem));				//Unallocate memory from mmap
	munmap(shared->maxten, sizeof(struct cs1550_sem));
	munmap(shared->maxtwo, sizeof(struct cs1550_sem));
	munmap(shared, sizeof(shared_mem));
}