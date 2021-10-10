#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <sys/unistd.h>
#include <pthread.h>

#define XMAX 2.0
#define XMIN -2.0
#define YMAX 2.0
#define YMIN -2.0
#define N 10000                //number of divisions for the grid
#define ITER 50                //number of iterations for each point

#define TEST_RESULTS

#define 	NUM_THREADS 256


int pixels[N][N];
double sums;

struct timeval startTime;
struct timeval finishTime;

pthread_mutex_t sum_mux = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t avg_bar; 

//The printing is only for fun :)
void printMandelBrot();

void verifyMatrixSum();

void *thread_calculate(void* rank){
	int i, j, count;

    double x, y;            //(x,y) point on the complex plane
    double x0, y0, tempx;
    double dx, dy;

    //increments in the real and imaginary directions
    dx = (XMAX - XMIN) / N;
    dy = (YMAX - YMIN) / N;

	long my_rank = (long) rank;
	int my_m = N/NUM_THREADS;
	int my_start = my_rank*my_m;
	int my_end = (my_rank+1)*my_m;

	if (my_rank+1 == NUM_THREADS){
		my_end += N%NUM_THREADS;
	}

	double my_count = 0;

	for (i = my_start+1; i <= my_end; i++){
		for (j = 1; j <= N; j++){
			x0 = XMAX - (dx * i);
			y0 = YMAX - (dy*j);
			x = 0;
			y = 0;
			count = 0;

			while((x * x + y * y < 4) && (count < ITER)) {
                tempx = (x * x) - (y * y) + x0;
                y = (2 * x * y) + y0;
                x = tempx;
                count++;
            }
			my_count += (double) count;
			pixels[i-1][j-1] = count;
		}
	}	
	pthread_mutex_lock(&sum_mux);
	sums += my_count;
	pthread_mutex_unlock(&sum_mux);
	pthread_barrier_wait(&avg_bar);
	
	double avg = sums/(N*N);
	for (i = my_start; i < my_end; i++){
		for (j = 0; j < N; j++){
			pixels[i][j] = pixels[i][j] / avg;
		}

	}
}


double timeIntervalLength;

int main() {

    //Get the start time
    gettimeofday(&startTime, NULL); /* START TIME */
    //Declare Threads
	pthread_t threads[NUM_THREADS];
	int rc;
	long t;
	void *status;
	pthread_barrier_init(&avg_bar, NULL, NUM_THREADS);
	sums = 0;
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
    timeIntervalLength = (double) (finishTime.tv_sec - startTime.tv_sec) * 1000000
                         + (double) (finishTime.tv_usec - startTime.tv_usec);
    timeIntervalLength = timeIntervalLength / 1000;
    //Print the interval length
    printf("Interval length: %g msec.\n", timeIntervalLength);


#ifdef TEST_RESULTS
    verifyMatrixSum(pixels);
//    printMandelBrot(pixels);
#endif
    return 0;
}

// Helper function to verify if the sum from parallel and serial versions match
void verifyMatrixSum() {
    int i, j;

    double totalSum;
    totalSum = 0;
    
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            totalSum += (double) pixels[i][j];
        }
    }

    printf("\nTotal Sum = %g\n", totalSum);
}

void printMandelBrot() {
    int i, j;
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            printf("%d ", pixels[i][j]);
        }
        printf("\n");
    }
}

