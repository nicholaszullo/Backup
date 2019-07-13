/*
*   Sum of even valued fibonocci series numbers less than 4000000
*
*   O(N)
*/

#include <stdio.h>

int main(void)
{
    int a1 = 1;                         //Initialize series
    int a2 = 2;
    int a3 = 0;
    int sum = 2;                        //Start at 2 because starting to calc series starting at n = 3 so passed 2 already
    while (a3 < 4000000)                //When the series reaches 4000000, stop   
    {
        a3 = a2 + a1;                   //Update curr (Progress series to next value) 
        if (a3 % 2 == 0){               //If the current value is even, add it to sum
            sum += a3;
        }
        
        a1 = a2;                        //Update prev prev
        a2 = a3;                        //Update prev

   }
   printf("%d", sum);

}