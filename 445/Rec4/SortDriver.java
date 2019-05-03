import java.util.*;
public class SortDriver
{

	public static void main(String[] args)
	{
		if (args.length < 1)
			System.exit(0);
		
		int alength = Integer.parseInt(args[0]);					//Get the size of array from command line 
		Integer[] arr1 = new Integer[alength];								//Create arrays
		Integer[] arr2 = new Integer[alength];
		Random rand = new Random();
		
		for (int i = 0; i < alength; i++){
			Integer temp = new Integer(rand.nextInt());
			arr1[i] = temp;											//Add all the numbers to the arrays
			arr2[i] = temp;			
			
		}
		
		TextMergeQuick sorter = new TextMergeQuick();
		
		long start = System.nanoTime();								//Time merge sort
		sorter.mergeSort(arr1,alength);								//Run merge sort
		long end = System.nanoTime() - start;
		System.out.println("Time to merge Sort: " + end);
		
		start = System.nanoTime();									//Time quick sort
		sorter.quickSort(arr2,alength);								//Run quick sort
		end = System.nanoTime() - start;
		System.out.println("Time to Quick Sort: " + end);
		
		/*	Results
			1000
			merge - 9817057
			quick - 6367545
			160000
			merge - 163285560
			quick - 73280102
			1280000
			merge - 888699208
			quick - 488801608
			
			Conclusion
			Quick sort is much faster than merge sort in large cases
		
		
		
		
		
		
		
		
		
		*/
	}
}