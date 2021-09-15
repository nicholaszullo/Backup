import java.util.Random;

import gov.nasa.jpf.annotation.FilterField;

/* Add a @FilterField annotation to a variable that causes state space explosion! */

public class Rand {
	private static Random random = new Random();
	private static boolean[] arr = new boolean[10];
	private static int a;
	private static boolean b;
	private static int count = 0;

	public static void main(String[] args) {
		while (true) {
			a = random.nextInt(10); // randomly returns any number from 0 to 9
			b = random.nextBoolean(); // randomly returns true or false
			arr[a] = b;
			if (b) {
				count++;
			}
		}
	}
}
