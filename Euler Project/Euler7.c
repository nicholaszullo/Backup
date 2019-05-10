/*  By listing the first six prime numbers: 2, 3, 5, 7, 11, and 13, 
 *  we can see that the 6th prime is 13.
 *
 *  What is the 10001st prime number?
 */


#include <stdio.h>
#include <stdbool.h>                                                //Why is this needed to return a bool from a method?

bool isPrime(int val)
{
    for (int i = 2; i < val/2; i++)                                 //Start at 2 not 1 because all numbers are divisible by 1, could start at 3 because no evens passed into method
    {
        if (val%i == 0){                                            //If a number is divisible by another number it is not prime
            return false;                                           //Not prime
        }
    }
    return true;                                                    //Prime
}

int main(void)
{
    printf("What is the 10001th prime number?\n");

    int curr = 1;                                                   //Start checking prime numbers starting at 1 (but +2 immediately)
    int i = 1;                                                      //Start at 1, number of primes found (2 is skipped over by loop so assume it is already found)
    while (i < 10001)                                               //Look at numbers until 10001th prime
    {
        curr += 2;                                                  //Skip by 2 because the only even prime number is 2 so dont check evens
        if (isPrime(curr))
        {
            i++;                                                    //Increment number of primes found if the number is prime
        }
        
    }
    printf("The 10001th prime number is %d", curr);                 //Print the found number

}
