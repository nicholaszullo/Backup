#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <sys/unistd.h>
#include <mpi.h>

#define XMAX 2.0
#define XMIN -2.0
#define YMAX 2.0
#define YMIN -2.0
#define N 10000                //number of divisions for the grid
#define ITER 50                //number of iterations for each point

#define TEST_RESULTS

#define 	NUM_THREADS 8


struct timeval startTime;
struct timeval finishTime;

//The printing is only for fun :)
void printMandelBrot();

void verifyMatrixSum();

double timeIntervalLength;

int main() {
    int i, j, count;

    double x, y;            //(x,y) point on the complex plane
    double x0, y0, tempx;
    double dx, dy;

    //increments in the real and imaginary directions
    dx = (XMAX - XMIN) / N;
    dy = (YMAX - YMIN) / N;
	int *pixels = malloc(N * N *sizeof(int));
	int my_rank;
	int size;
    //Get the start time
    gettimeofday(&startTime, NULL); /* START TIME */

	MPI_Init(NULL, NULL);

	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

	MPI_Comm_size(MPI_COMM_WORLD, &size);

	int chunk = N / size;
	int i_start = chunk*my_rank;
	if (my_rank +1 == size){
		chunk += N % size;
	}
	chunk *= N;

	int *my_pixels = calloc(chunk,sizeof(int));
	double *my_count = calloc(1, sizeof(double));
    //calculations for mandelbrot
    for (i = 1; i <= chunk/N; i++) {
		for (j = 1; j <= N; j++) {
            // c_real
            x0 = XMAX -
                 (dx * (i+i_start));  //scaled i coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.00, 2.00))
            // c_imaginary
            y0 = YMAX -
                 (dy * (j));  //scaled j coordinate of pixel (scaled to lie in the Mandelbrot X scale (-2.00, 2.00))

            // z_real
            x = 0;

            // z_imaginary
            y = 0;
            count = 0;

            while ((x * x + y * y < 4) && (count < ITER)) {
                tempx = (x * x) - (y * y) + x0;
                y = (2 * x * y) + y0;
                x = tempx;
                count++;
            }
            my_pixels[((i - 1) * N) + j - 1] = count;
			*my_count += (double) count;
        }
    }
	double totalSum = 0;
	MPI_Allreduce(my_count, &totalSum, 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);

	double avg = ((double) totalSum)/(N*N);
	for (i = 0; i < chunk/N; i++) {
		for (j = 0; j < N; j++) {
			my_pixels[(i*N) + j]=my_pixels[(i*N)+j]/avg;
		}
	}

	int *counts = calloc(size, sizeof(int));
	int *disp = calloc(size, sizeof(int));
	for (i = 0; i < size; i++){
		if (i +1 == size){
			counts[i] = ((N / size) + (N % size)) * N;
		} else {
			counts[i] = chunk;
		}
		disp[i] = chunk*i;
	}

	MPI_Gatherv(my_pixels, chunk, MPI_INT, pixels, counts, disp, MPI_INT, 0, MPI_COMM_WORLD);
	if (my_rank == 0){
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
		    //printMandelBrot(pixels);
		#endif
	}
    MPI_Finalize();
	return 0;
}

// Helper function to verify if the sum from parallel and serial versions match
void verifyMatrixSum(int* pixels) {
    int i, j;

    double totalSum;
    totalSum = 0;
    //
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            totalSum += (double) pixels[(i*N) + j];
        }
    }

    printf("\nTotal Sum = %g\n", totalSum);
}

void printMandelBrot(int* pixels) {
    int i, j;
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            printf("%d ", pixels[(i*N) + j]);
        }
        printf("\n");
    }
}

