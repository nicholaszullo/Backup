import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;

/* Replace the TODO comments with your own invariant assertions! */

@RunWith(JUnitQuickcheck.class)
public class ArraysTest {
	
	@Property
	public void testAbs(@InRange(minInt = -10, maxInt = 10) int x) {
		int y = Math.abs(x);
		// TODO: Add invariant assertions.
	}
	
	@Test
	public void testSorted() {
		int[] arr = {2, 1, 0};
		int[] ret = Arrays.stream(arr).sorted().toArray();
		assertArrayEquals(new int[]{0, 1, 2}, ret);
	}
	
	@Property
	public void testSortedStochastic(int[] arr) {
		// Sorts arr in ascending order and stores the result in ret.
		int[] ret = Arrays.stream(arr).sorted().toArray();

//		System.out.println("arr=" + Arrays.toString(arr));
//		System.out.println("ret=" + Arrays.toString(ret));

		// TODO: Add invariant assertions.
	}
	
	@Property
	public void testFilterOutOdd(int[] arr) {
		// Filters out odd numbers from arr and stores the result in ret
		int[] ret = Arrays.stream(arr).filter(n -> n % 2 == 0).toArray();
		
//		System.out.println("arr=" + Arrays.toString(arr));
//		System.out.println("ret=" + Arrays.toString(ret));

		// TODO: Add invariant assertions.
	}
	
}