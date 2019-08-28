/*
    run as java Euler11 Euler1input.txt
    VERY ineffiecent. Makes a whole pass of grid for each operation 
    Like O(N^8) lol
    


*/

import java.io.*;

public class Euler11 
{
    private static Integer[][] arr;
    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
            System.exit(0);
        BufferedReader read = new BufferedReader(new FileReader(args[0]));

        arr = new Integer[20][20];
        int k = 0;
        int max_product = 0;

        while (read.ready())
        {
            String line = read.readLine();
            String[] split = line.split(" ");
            for (int i = 0; i < split.length; i++)
            {
                arr[i][k] = Integer.parseInt(split[i]);
            }
            k++;
        }
        
        
        for (int i = 0; i < arr.length; i++)
        {
            for (k = 0; k < arr[i].length-4; k++)
            {
                int product = arr[i][k]*arr[i][k+1]*arr[i][k+2]*arr[i][k+3];
                if (product >= max_product)
                    max_product = product;
            }   

        }


        for (k = 0; k < arr[0].length; k++)
        {
            for (int i = 0; i < arr.length-4; i++)
            {
                int product = arr[i][k]*arr[i+1][k]*arr[i+2][k]*arr[i+3][k];
                if (product >= max_product)
                    max_product = product;
            }   

        }
        
        
        for (int i = 0; i < arr.length-4; i++)
        {
            for (k = 0; k < arr[i].length-4; k++)
            {
                int product = arr[i][k]*arr[i+1][k+1]*arr[i+2][k+2]*arr[i+3][k+3];
                if (product >= max_product)
                    max_product = product;
            }
        }
        
        
        for (int i = 0; i < arr.length-4; i++)
        {
            for (k = 19; k > 3; k--)
            {
                int product = arr[i][k]*arr[i+1][k-1]*arr[i+2][k-2]*arr[i+3][k-3];
                if (product >= max_product)
                    max_product = product;
            }
        }
        System.out.println("The max product from the square is " + max_product);
    }


}