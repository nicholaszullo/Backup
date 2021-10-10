#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <pthread.h>

#define		NROW	1028
#define		NCOL	NROW

#define 	NUM_THREADS 32

#define TEST_RESULTS


//Matrix A
int matrixA  b[NROW][NCOL];
//Matrix B
int matrixB  [NROW][NCOL];
//Matrix C
int matrixC [NROW][NCOL];

//Temp array
int tempMatrix [NROW][NCOL];

//Output Array C
int outputMatrix [NROW][NCOL];

struct timeval startTime;
struct timeval finishTime;
double timeIntervalLength;

void verifyMatrixSum();

void *thread_calculate(void* rank) {
	long my_rank = (long) rank;
	int i, j, k;
	int my_m = NROW/NUM_THREADS;
	int my_start = my_rank*my_m;
	int my_end = (my_rank+1)*my_m;
 
    if (my_rank +1 == NUM_THREADS){
        my_end += NROW % NUM_THREADS;
    }
	for (i = my_start; i < my_end; i++){
		for (j = 0; j < NCOL; j++){
			int sum = 0;
			for (k = 0; k < NROW; k++){
				sum += matrixA[i][k] * matrixB[k][j];
			}
			tempMatrix[i][j] = sum;

		}
		for (j = 0; j < NCOL; j++){
			int sum = 0;
			for (k = 0; k<NROW; k++){
				sum += tempMatrix[i][k] * matrixC[k][j];	
			}
			outputMatrix[i][j] = sum;
		}
	}
	
}

int main(int argc, char* argv[])
{
    int i,j,k;
    // Matrix initialization. Just filling with arbitrary numbers.
    for(i=0;i<NROW;i++)
    {
        for(j=0;j<NCOL;j++)
        {
            matrixA[i][j]= (i + j)/128;
            matrixB[i][j]= (j + j)/128;
            matrixC[i][j]= (i + j)/128;
            tempMatrix[i][j] = 0;
            outputMatrix[i][j]= 0;
        }
    }

    //Get the start time
    gettimeofday(&startTime, NULL); /* START TIME */
	//Declare Threads
	pthread_t threads[NUM_THREADS];
	int rc;
	long t;
	void *status;

	//Create Threads
	for (t = 0; t < NUM_THREADS; t++){
		rc = pthread_create(&threads[t], NULL, thread_calculate, (void *)t);
		if (rc){
			printf("ERROR MAKING THREAD %d CODE %d\n", t, rc);
			exit(-1);
		}
	}

	//Finish threads
	for (t = 0; t < NUM_THREADS; t++){
		rc = pthread_join(threads[t], &status);
	}

    //Get the end time
    gettimeofday(&finishTime, NULL);  /* END TIME */

    //Calculate the interval length
    timeIntervalLength = (double)(finishTime.tv_sec-startTime.tv_sec) * 1000000
                         + (double)(finishTime.tv_usec-startTime.tv_usec);
    timeIntervalLength=timeIntervalLength/1000;

    #ifdef TEST_RESULTS
    //[Verifying the matrix summation]
    verifyMatrixSum();
    #endif

    //Print the interval length
    printf("Interval length: %g msec.\n", timeIntervalLength);

    return 0;
}


// Helper function to verify if the sum from parallel and serial versions match
void verifyMatrixSum() {
    int i, j;

    double totalSum;
    totalSum=0;
    //
    for(i=0;i<NROW;i++){
        for(j=0;j<NCOL;j++)
        {
            totalSum+=(double)outputMatrix[i][j];
        }
    }
    printf("\nTotal Sum = %g\n",totalSum);
}
