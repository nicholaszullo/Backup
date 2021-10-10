#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <sys/time.h>
#include <pthread.h>

#define		NSTEPS	2000
#define		P_START	0
#define		P_END	5

#define 	NUM_THREADS 16


struct timeval startTime;
struct timeval finishTime;
double timeIntervalLength;

double polynomial(double x);
double area;
pthread_mutex_t area_mux = PTHREAD_MUTEX_INITIALIZER;

void *thread_calculate(void* rank){
	long my_rank = (long) rank;
	int i;

	int my_m = NSTEPS/NUM_THREADS;
	int my_start = my_rank*my_m;
	int my_end = (my_rank+1)*my_m;
	if (my_rank+1 == NUM_THREADS){
        my_end += NSTEPS % NUM_THREADS;
    }

	double step_size;
    double my_area;
    double p_current = P_START;
    double f_result_low, f_result_high;

    // Calculating intermediary step sizes
    step_size = (double)(P_END - P_START) / NSTEPS;
    
    //Initial step position
    p_current = P_START;
    my_area=0.0;
    for (i = my_start; i < my_end; i++){
        p_current = i * step_size;

        f_result_low = polynomial(p_current);
        f_result_high = polynomial(p_current + step_size);

        my_area += (f_result_low + f_result_high) * step_size / 2;
    }
    pthread_mutex_lock(&area_mux);
    area += my_area;
    pthread_mutex_unlock(&area_mux);

}

int main() {

    //Get the start time
    gettimeofday(&startTime, NULL);
	//Declare Threads
	pthread_t threads[NUM_THREADS];
	int rc;
	long t;
	void *status;
	area = 0;
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
    gettimeofday(&finishTime, NULL);  /* after time */

    //Calculate the interval length
    timeIntervalLength = (double)(finishTime.tv_sec-startTime.tv_sec) * 1000000
                         + (double)(finishTime.tv_usec-startTime.tv_usec);
    timeIntervalLength=timeIntervalLength/1000;

    //Print the interval length
    printf("Interval length: %g msec.\n", timeIntervalLength);

    printf("Result: %f \n",area);

    return 0;
}

// Calculates x->y values of a fixed polynomial
// Currently is https://www.desmos.com/calculator/swxvru1xxn
double polynomial(double x){

    // x^2
    double numerator = pow(x, 2);
    //(-4x^3+2x^4)
    double temp_poly = -4 * pow(x, 3) + 2 * pow(x, 4);

    //(-4x^3+2x^4)^2
    double temp_poly_2 = pow(temp_poly, 4);

    // x^3 + 2x^2 * (-4x^3+2x^4)^2
    double temp_poly_3 = pow(x, 3) + 2 * pow(x, 2) * temp_poly_2;

    // root square of (x^3 + 2x^2 * (-4x^3+2x^4)^2)
    double denominator = sqrt(temp_poly_3);

    double y = 0;
    if (denominator != 0)
        y = numerator / denominator;

    return y;
}