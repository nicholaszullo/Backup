import java.util.*;
public class QuickTest
{
	public static void main(String[] args)
	{
		if (args.length != 3){
			System.out.println("Not a valid input!");
			System.exit(0);
		}
		int size = Integer.parseInt(args[0]);
		int base = Integer.parseInt(args[1]);
		int trials = Integer.parseInt(args[2]);
		int sum1 = 0;
		int sum2 = 0;
		
		Integer[] arr1 = new Integer[size];								//Create arrays
		Integer[] arr2 = new Integer[size];
		Random rand = new Random();
		
		Quick sort1 = new Quick();
		TextMergeQuick sort2 = new TextMergeQuick();
		
		for (int k = 0; k <= trials; k++)
		{
			for (int i = 0; i < size; i++){
				Integer temp = new Integer(rand.nextInt());
				arr1[i] = temp;											//Add all the numbers to the arrays
				arr2[i] = temp;			
			}
		
			long start = System.currentTimeMillis();		
			sort1.quickSort(arr1,size);
			long end = System.currentTimeMillis() - start;
			sum1 += end;
			
			sort2.setMin(base);
			start = System.currentTimeMillis();	
			sort2.quickSort(arr2,size);
			end = System.currentTimeMillis() - start;
			sum2 += end;
		}	
		
		System.out.println("Simple Quick Sort average time: " + sum1/trials + " millisec/test");
		System.out.println("Median of 3 Quick Sort average time: " + sum2/trials + " millisec/test");
		/*
			DATA
			The median of 3 piviot sort (sort2) was faster when base was smaller. 
			If the number for base is larger, the total time will be slower than the basic quick sort		
		
		*/




	}
}