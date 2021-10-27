#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>


#define NUM_THREADS	5


typedef struct{
	int id;
	int nThreads;
	}ARGS;
	
ARGS args[NUM_THREADS];


void* f(void* v){

	ARGS* arg = (ARGS*)v;
	printf("I am %d, of %d\n", arg->id, arg->nThreads);
	
	
	  // EXIT THREAD
   pthread_exit(NULL);

}

int main(){


	// DECLARE THREADS
	pthread_t tid[NUM_THREADS];
	int i;
	void *status;
	
	
	// CREATE THREADS
	for(i = 0; i < NUM_THREADS; i++)
	{
		args[i].id = i;
		args[i].nThreads = NUM_THREADS;
		
		pthread_create(&tid[i], NULL, f, &args[i]);
	}
	
	
	// JOIN THREADS
	for(i = 0; i < NUM_THREADS; i++)
	{
		pthread_join(tid[i], &status);
	}


	 /* Last thing that main() should do -- WAIT FOR ALL THREADS TO FINISH*/
   pthread_exit(NULL);
	
return 0;
}