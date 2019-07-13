/*
*   Find the largest palindromic number from 2 3 digit bases
*   7/13/19 Nick Zullo Brute Force O(N^2)
*/

#include <stdio.h>
#include <stdbool.h>

bool checkPal(int num)
{
    int arr[10];                                    //Array that stores the digits, no way for 10 digits to be exceeded with int data type
    int i = 0;
    while (num > 0 && i < 10)                       //Split the number into an array of digits
    {
        arr[i] = num % 10;
        num /= 10;
        i++;                                        //Need to increment counter after print or index out of bounds in arr[i]
    }
    i--;                                            //i needs to move down one because it moved up on last digit of loop
    int k = 0;
    bool pal = false;
    while (!pal)                                    //Written with a recursive mindset but in a loop
    {
        if (k >= i)                                 //Base case accounts for odd and even digitNums, if even k > i if odd k == i 
        {                                           //Reaches here if numbers before match
            pal = true;
        } 
        else if (arr[k] == arr[i])                  //Current numbers are a match
        {
            k++;                                    //Move to new numbers to compare
            i--;
        }
        else {                                      //Not a palindrome
            return pal;
        }

    }  
    return pal;                                     //will only return true if gets here
}

int main(void)
{
    int ii, kk;                                     //Values that make the largest 3 digit palindrome
    int max = 0;                                    //Largest 3 digit palindrome
    for (int i = 100; i < 1000; i++)                //First number
    {
        for (int k = 100; k < 1000; k++)            //Second number 
        {
            if (checkPal(i*k))                      //Check if product is a palindrome
            {
                if (i*k >= max)                     //If product is bigger than prevous palindrome, update values
                {
                    max = i*k;
                    ii = i;
                    kk = k;
                } 
            } 
        }
    }
    printf("The largest palendrome number from 2 3 digit bases is %d * %d = %d ", ii, kk, max);
}
