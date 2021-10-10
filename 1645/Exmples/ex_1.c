#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>


#define NUM_THREADS     5

void *PrintHello(void *threadid)
{
   long tid;
   tid = (long)threadid;
   printf("Hello World from thread #%ld\n", tid);
   
   // EXIT THREAD
   pthread_exit(NULL);
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
   
		printf("In main: creating thread %ld\n", t);
   
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
   
   

   /* Last thing that main() should do -- WAIT FOR ALL THREADS TO FINISH*/
   pthread_exit(NULL);
}