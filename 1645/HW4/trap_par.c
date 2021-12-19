#include <stdio.h>
#include <math.h>
#include <time.h>
#include <sys/time.h>
#include <mpi.h>

#define		NSTEPS	2000
#define		P_START	0
#define		P_END	5


struct timeval startTime;
struct timeval finishTime;
double timeIntervalLength;

double polynomial(double x);

int main() {
    int i;
    double step_size;
    double area;
    double p_current = P_START;
    double f_result_low, f_result_high;

    // Calculating intermediary step sizes
    step_size = (double)(P_END - P_START) / NSTEPS;

    //Initial step position
    p_current = P_START;
    area=0.0;

    //Get the start time
    gettimeofday(&startTime, NULL);

	MPI_Init(NULL, NULL);

	int my_rank;
	int size;

	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int chunk = NSTEPS / size;
	int start_i = chunk * my_rank;
	if (my_rank +1 == size){
		chunk += NSTEPS % size;
	}
	
	double my_area = 0;

    for(i = 0; i < chunk; i++)
    {
        p_current = (i + start_i) * step_size;

        f_result_low = polynomial(p_current);
        f_result_high = polynomial(p_current + step_size);

        my_area += (f_result_low + f_result_high) * step_size / 2;
    }

	MPI_Reduce(&my_area, &area, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
	if (my_rank == 0){

		//Get the end time
		gettimeofday(&finishTime, NULL);  /* after time */

		//Calculate the interval length
		timeIntervalLength = (double)(finishTime.tv_sec-startTime.tv_sec) * 1000000
							+ (double)(finishTime.tv_usec-startTime.tv_usec);
		timeIntervalLength=timeIntervalLength/1000;

		//Print the interval length
		printf("Interval length: %g msec.\n", timeIntervalLength);

		printf("Result: %f \n",area);

	}
	
	MPI_Finalize();
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