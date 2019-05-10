/*
*   Finds the nth prime number as given by the user
*/


#include <stdio.h>
#include <stdbool.h>

bool isPrime(int val)
{
    for (int i = 2; i < val/2; i++)                                 //sqrt(val) or val/2?
    {
        if (val%i == 0){                                            //If a number is divisible by another number it is not prime
            return false;                                           //Not prime
        }
    }
    return true;                                                    //Prime
}

int main(void)
{
    int num;
    printf("Enter the nth prime number you would like to find: ");  //Get user input for how many primes to find
    scanf("%d", &num);
    printf("What is prime number number %d?\n", num);

    if (num == 1){                                                  //If user wants 1st prime, print it, loop does not handle even numbers to be faster
        printf("Prime number 1 is 2");
        return 0;
    }

    int curr = 1;                                                   //Start checking prime numbers starting at 1 (+2 immediately in loop so really starts at 3)
    int i = 1;                                                      //Number of primes found, starts at 1 because skipping 2
    while (i < num)                                                 //Look at numbers until given location
    {
        curr +=2;                                                   //Skip by 2 because the only even prime number is 2 so dont check evens
        if (isPrime(curr))
        {
            i++;                                                    //Increment number of primes found if the number is prime
        }
        
    }
    printf("Prime number %d is %d", num, curr);                     //Print the user value and found prime

}
