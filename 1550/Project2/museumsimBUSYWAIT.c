#include "sem.h"
#include "unistd.h"
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <sys/time.h>
#include <sys/mman.h>
/*
 *		COMPILE WITH -m32 TO RUN ON QEMU otherwise binary file error 
 * 		./museumsim -m 10 -k 2 -pv 50 -dv 1 -sv 1 - pg 50 -pg 1 -sg 1
 */

struct timeval starttime;

typedef struct shared_mem{
	int visitors;
	int guides;
	int numInMuseum;
	int visitorsWaiting;
	int guidesWaiting;
	int guidesArriving;
	int visitorsArriving;
	struct cs1550_sem* exit;
	struct cs1550_sem* maxtwo;						//2 guides max
	struct cs1550_sem* maxten;						//10 visitors max
}shared_mem; 

shared_mem* shared;
int number;											//The ID of the visitor or tour guide

void down(struct cs1550_sem *sem) {
  syscall(__NR_cs1550_down, sem);
}

void up(struct cs1550_sem *sem) {
  syscall(__NR_cs1550_up, sem);
}

//Called immediately after tourguideArrives returns
void openMuseum(){
	struct timeval curr;
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
	gettimeofday(&curr, NULL);
	printf("Tour guide %d opens the museum for tours at time %d.\n", number, (int)(curr.tv_sec - starttime.tv_sec));
	fflush(stdout);


}

void tourMuseum(){
	//Must sleep for 2 seconds while touring
	//A visitor inside must not block another visitor
	struct timeval curr;

	if (shared->maxten->value <=0 && (shared->visitorsArriving - (shared->guidesArriving*10)) >= 0 && shared->guidesWaiting == 0)								//If no guides waiting or arriving, and max has been reached dont enter deadlock
		return;
	
	shared->visitorsWaiting++;						//Will wait if not enter
	down(shared->maxten);							//Enter museum
	shared->numInMuseum++;							//Enter museum
	shared->visitorsWaiting--;						//In museum so not waiting


	gettimeofday(&curr, NULL);  
	printf("Visitor %d tours the museum at time %d\n", number, (int)(curr.tv_sec - starttime.tv_sec) );
	fflush(stdout);
	sleep(2);
}

void tourguideLeaves(){
	//Cannot leave until all visitors leave
	struct timeval curr;
	struct timespec time;
	time.tv_sec = 0;
	time.tv_nsec = 100000000L;
	
	if (number == 0 || (shared->numInMuseum == 0 && (shared->visitorsArriving > 0 || shared->visitorsWaiting > 0))){							//If just opened museum, wait for some visitors to enter before trying to leave
	//	nanosleep(time);
		down(shared->exit);
	}
	while(shared->numInMuseum > 0 ){					//Check if there is still visitors in museum, wait if so
	//	printf("%d \n", shared->numInMuseum);
	//	fflush(stdout);
		nanosleep(time); 
		//down(shared->exit);
	}
	while (shared->maxten->value > 0 && shared->visitorsArriving > 0){
	//	printf("%d %d \n", shared->maxten->value, shared->visitorsArriving);
	//	fflush(stdout); 
	//	nanosleep(time);
		down(shared->exit);
	}
	up(shared->maxtwo);								//Allow another guide to open tours

	gettimeofday(&curr, NULL);
	printf("Tour guide %d leaves the museum at time %d.\n", number,  (int)(curr.tv_sec - starttime.tv_sec) );
	fflush(stdout); 

}

void visitorLeaves(){
	struct timeval curr;

	while (shared->exit->value < 0)					//Reset exit sem for guide so it can exit when no more visitors
		up(shared->exit);
	
	shared->numInMuseum--;

	gettimeofday(&curr, NULL);
	printf("Visitor %d leaves the museum at time %d\n", number, (int)(curr.tv_sec - starttime.tv_sec));
	fflush(stdout);
}

void visitorArrives(int m, int pv, int dv, int sv){
	//Block if no tour guide arriving 
	//Wait if museum is closed
	//Wait if maximum number of visiors reached 
	int i;
	struct timeval curr;
	srand(sv);										//Needs to be at top and not in else or rand() returns same value
	for (i = 0; i < m; i++){
		int pid = fork();							//Create a visitor process
		if (pid == 0){								//Arriving visitor process
			number = shared->visitors;
			shared->visitors++;						//Start from 0 (place before to start from 1)
			shared->visitorsArriving--;				//Decrease total visitors left to arrive 
			gettimeofday(&curr, NULL);
			printf("Visitor %d arrives at time %d.\n", number, (int)(curr.tv_sec - starttime.tv_sec));
			fflush(stdout);
			tourMuseum(); 
			visitorLeaves();
			exit(0);
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
	//printf("done with visitors\n");
}

void tourguideArrives(int k, int pg, int dg, int sg){
	//Block if no visior arriving
	//Wait if museum is closed and no visitors
	//Wait if 2 tour guides in museum
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
	//printf("done with guides\n");
	//fflush(stdout);
}


void main(int argc, char *argv[]){
	int opt, m, k, pv, dv, sv, pg, dg, sg;
	int temp;
	
	gettimeofday(&starttime, NULL);
	shared = (shared_mem*)mmap(NULL, sizeof(shared_mem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	if ((int)shared == -1){
		printf("UNABLE TO ALLOCATE MEMORY FOR SEMAPHORES\n");
		exit(0);
	}
	shared->guides = 0;
	shared->visitors = 0;
	shared->numInMuseum = 0;
	shared->visitorsWaiting = 0;
	shared->guidesWaiting = 0;
	shared->exit = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	shared->maxtwo = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	shared->maxten = (struct cs1550_sem*)mmap(NULL, sizeof(struct cs1550_sem), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	shared->exit->value = 0;
	shared->maxtwo->value = 2;
	shared->maxten->value = 0;

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
					printf("Here %d\n", temp);
					break;
				}
				break;
			default: 							//'?' is the unspecified argument value of opt
				printf("Unknown argument provided! %c\n", optopt);
				break;

		}
	}
	//printf("%d %d %d %d %d %d %d %d\n", m, k, pv, dv, sv, pg, dg, sg);
	shared->guidesArriving = k;
	shared->visitorsArriving = m;
	printf("The museum is now empty.\n");
	fflush(stdout);
	int pid = fork();
	if (pid == 0){
		visitorArrives(m, pv, dv, sv);
	} else {
		//FORK AGAIN FOR THIS ?
		tourguideArrives(k, pg, dg, sg);
	 	wait(NULL);			//Wait for Visitor arrival process
	}

	munmap(shared->exit, sizeof(struct cs1550_sem));
	munmap(shared->maxten, sizeof(struct cs1550_sem));
	munmap(shared->maxtwo, sizeof(struct cs1550_sem));
	munmap(shared, sizeof(shared_mem));
}