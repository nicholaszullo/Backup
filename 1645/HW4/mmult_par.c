#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <mpi.h>

#define		NROW	1024
#define		NCOL	NROW

#define 	NUM_THREADS 8

#define TEST_RESULTS


struct timeval startTime;
struct timeval finishTime;
double timeIntervalLength;

void verifyMatrixSum();

int main(int argc, char* argv[])
{
    //Matrix A
    int *matrixA = malloc(NROW * NCOL * sizeof(int));
    //Matrix B
    int *matrixB = malloc(NROW * NCOL * sizeof(int));
    //Matrix C
    int *matrixC = malloc(NROW * NCOL * sizeof(int));

    //Temp array
    int *tempMatrix = malloc(NROW * NCOL * sizeof(int));

    //Output Array C
    int *outputMatrix = malloc(NROW * NCOL * sizeof(int));

    int i,j,k;
	int size;
	int my_rank;
    // Matrix initialization. Just filling with arbitrary numbers.
    for(i=0;i<NROW;i++)
    {
        for(j=0;j<NCOL;j++)
        {
            matrixA[(i*NCOL) + j]= (i + j)/128;
            matrixB[(i*NCOL) + j]= (j + j)/128;
            matrixC[(i*NCOL) + j]= (i + j)/128;
            tempMatrix[(i*NCOL) + j] = 0;
            outputMatrix[(i*NCOL) + j]= 0;
        }
    }
//    printf("%d\n", matrixA[2000]);
    //Get the start time
    gettimeofday(&startTime, NULL); /* START TIME */

	MPI_Init(&argc, &argv);

	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
		
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	
    int chunk = NROW / size;
    if (my_rank+1 == size){
        chunk += NROW % size;
    }
    chunk *= NROW;
    int *local_A = malloc(chunk * sizeof(int));
    int *local_B = malloc(NROW*NCOL * sizeof(int));
    int *local_C = malloc(NROW*NCOL * sizeof(int));
    int *local_temp = malloc(chunk * sizeof(int));
    int *local_out = malloc(chunk * sizeof(int));
    int *counts = malloc(size * sizeof(int));
    int *allcounts = malloc(size * sizeof(int));
    int *disp = malloc(size * sizeof(int));
    int *alldisp = malloc(size * sizeof(int));

    for (i = 0; i < size; i++){
        if (i+1 == size){
            counts[i] = ((NROW/size) + (NROW % size)) * NROW;
        } else{
            counts[i] = chunk;
        } 
        disp[i] = chunk*i;  
        allcounts[i] = NROW*NCOL;
        alldisp[i] = 0;
    }

    MPI_Scatterv(matrixA, counts, disp, MPI_INT, local_A, chunk, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(matrixB, allcounts, alldisp, MPI_INT, local_B, NROW*NCOL, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(matrixC, allcounts, alldisp, MPI_INT, local_C, NROW*NCOL, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(tempMatrix, counts, disp, MPI_INT, local_temp, chunk, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatterv(outputMatrix, counts, disp, MPI_INT, local_out, chunk, MPI_INT, 0, MPI_COMM_WORLD);

    //A*B*C
    for(i = 0; i < chunk / NROW; i++){
        for(j = 0; j < NCOL; j++){
            for(k = 0; k < NROW; k++){
                // K iterates rows on matrixA and columns in matrixB
                local_temp[(i*NCOL) + j] += local_A[(i*NCOL) + k] * local_B[(k*NCOL) + j];
            }
        }

        for(j = 0; j < NROW; j++) {
            for (k = 0; k < NCOL; k++) {
                // K iterates rows on matrixA and columns in matrixB
                local_out[(i*NCOL) + j] += local_temp[(i*NCOL) + k] * local_C[(k*NCOL) + j];
            }
        }
    }
    MPI_Gatherv(local_out, chunk, MPI_INT, outputMatrix, counts, disp, MPI_INT, 0, MPI_COMM_WORLD);

    if (my_rank == 0){
        //Get the end time
        gettimeofday(&finishTime, NULL);  /* END TIME */
        
        //Calculate the interval length
        timeIntervalLength = (double)(finishTime.tv_sec-startTime.tv_sec) * 1000000
                            + (double)(finishTime.tv_usec-startTime.tv_usec);
        timeIntervalLength=timeIntervalLength/1000;

        #ifdef TEST_RESULTS
        //[Verifying the matrix summation]
        verifyMatrixSum(outputMatrix);
        #endif
        
        //Print the interval length
        printf("Interval length: %g msec.\n", timeIntervalLength);
        free(matrixA);
        free(matrixB);
        free(matrixC);
        free(tempMatrix);
        free(outputMatrix);
    }
    
    MPI_Finalize();
    return 0;
}


// Helper function to verify if the sum from parallel and serial versions match
void verifyMatrixSum(int* outputMatrix) {
    int i, j;

    double totalSum;
    totalSum=0;
    //
    for(i=0;i<NROW;i++){
        for(j=0;j<NCOL;j++)
        {
            totalSum+=(double)outputMatrix[(i*NCOL) + j];
        }
    }
    printf("\nTotal Sum = %g\n",totalSum);
}
