#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>


#define NUM_THREADS     500

int global = 0;

/* Note scope of variable and mutex are the same */
pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;

void *PrintHello(void *threadid)
{
   long tid;
   tid = (long)threadid;
   
   pthread_mutex_lock( &mutex1 );
   global++;
   pthread_mutex_unlock( &mutex1 );
   
   //printf("Hello World from thread #%ld\n", tid);
   
   // EXIT THREAD
 
}

int main (int argc, char *argv[])
{

	// DECLARE THREADS
   pthread_t threads[NUM_THREADS];
   int rc;
   long t;
   void *status;
   
   // CREATE MULTIPLE THREADS
   for(t=0; t<NUM_THREADS; t++){
   
		//printf("In main: creating thread %ld\n", t);
   
		rc = pthread_create(&threads[t], NULL, PrintHello, (void *)t);

		if (rc){
         printf("ERROR; return code from pthread_create() is %d\n", rc);
         exit(-1);
      }
   }
   
   
   // JOIN THREADS
    for(t=0; t<NUM_THREADS; t++) {
        rc = pthread_join(threads[t], &status);
    }
   
   
   
	printf("Global == %d\n", global);
   
   
   /* Last thing that main() should do -- WAIT FOR ALL THREADS TO FINISH*/
 
}