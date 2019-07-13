/*
*   Prime factors of 600851475143
*   Didnt really make it generalized, just tested numbers with known list of prime factors, kinda bad program
*   Run time of O(N^2)
*/
#include <stdio.h>
#include <stdbool.h>

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
    bool finished = false;
    long long num = 600851475143;
    int i = 3;

    while (!finished)
    {
        if (isPrime(i))                                     //Check if testing a prime number as a factor
        {
            if (num % i == 0)                               //If it is a factor, print it and update curr num
            {
                printf("%d ", i);
                num /= i;
            }
        }
        i += 2;                                            //Move to the next factor, no even primes so skip by 2
        if (num == 1 || i > num)                           //When num == 1 prime factors have been found 
        {                                                  //When i > num, not enough factors found
            finished = true;                    
        }
    }
    printf("are prime factors");
}