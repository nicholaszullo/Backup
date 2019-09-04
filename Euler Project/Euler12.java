/*
What is the value of the first triangle number to have over five hundred divisors?
76576500 ! took a super long time to add up to it. Find shortcuts?
*/


public class Euler12
{
    public static void main(String[] args)
    {
        boolean found = false;
        long curr = 0;
        int k = 1;

        while (!found)
        {
            curr += k;
            k++;
        //    System.out.println(curr);
            if (numDivisors(curr) >= 500)
                found = true;
        }

        System.out.println("The triangle number with 500 divisors is " + curr);

    }

    private static int numDivisors(long num)
    {
        int divisors = 0;

        for (int i = 1; i < num/2; i++)
        {
            if (num%i == 0)
                divisors++;
        }
        return divisors+2;

    }
}