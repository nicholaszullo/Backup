import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gov.nasa.jpf.vm.Verify;
import java.lang.Math;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Code by @author Wonsun Ahn
 * 
 * <p>Uses the Java Path Finder model checking tool to check BeanCounterLogic in
 * various modes of operation. It checks BeanCounterLogic in both "luck" and
 * "skill" modes for various numbers of slots and beans. It also goes down all
 * the possible random path taken by the beans during operation.
 */

public class BeanCounterLogicTest {
	private static BeanCounterLogic logic; // The core logic of the program
	private static Bean[] beans; // The beans in the machine
	private static String failString; // A descriptive fail string for assertions

	private static int slotCount; // The number of slots in the machine we want to test
	private static int beanCount; // The number of beans in the machine we want to test
	private static boolean isLuck; // Whether the machine we want to test is in "luck" or "skill" mode

	/**
	 * Sets up the test fixture.
	 */
	@BeforeClass
	public static void setUp() {
		if (Config.getTestType() == TestType.JUNIT) {
			slotCount = 5;
			beanCount = 3;
			isLuck = false;
		} else if (Config.getTestType() == TestType.JPF_ON_JUNIT) {
			/*
			 * Use the Java Path Finder Verify API to generate choices for slotCount,
			 * beanCount, and isLuck: slotCount should take values 1-5, beanCount should
			 * take values 0-3, and isLucky should be either true or false. For reference on
			 * how to use the Verify API, look at:
			 * https://github.com/javapathfinder/jpf-core/wiki/Verify-API-of-JPF
			 */
			slotCount = Verify.getInt(1, 5);
			beanCount = Verify.getInt(0, 3);
			isLuck = Verify.getBoolean();
		} else {
			assert (false);
		}

		// Create the internal logic
		logic = BeanCounterLogic.createInstance(slotCount);
		// Create the beans
		beans = new Bean[beanCount];
		for (int i = 0; i < beanCount; i++) {
			beans[i] = Bean.createInstance(slotCount, isLuck, new Random(i));
		}

		// A failstring useful to pass to assertions to get a more descriptive error.
		failString = "Failure in (slotCount=" + slotCount
				+ ", beanCount=" + beanCount + ", isLucky=" + isLuck + "):";
	}

	@AfterClass
	public static void tearDown() {
	}
	
	private int getInFlightBeanCount() {
		int inFlight = 0;
		for (int i = 0; i < slotCount; i++) {
			if (logic.getInFlightBeanXPos(i) != -1) {
				inFlight += 1;
			}
		}
		return inFlight;
	}
	
	private int getSlotBeanCount() {
		int inSlot = 0;
		for (int i = 0; i < slotCount; i++) {
			if (logic.getSlotBeanCount(i) != -1) {
				inSlot += logic.getSlotBeanCount(i);
			}
		}
		return inSlot;
	}
	
	private int getHalfCount(int num) {
		if (num % 2 == 0) {
			return num / 2;
		} else {
			return (num + 1) / 2;
		}
	}

	/**
	 * Test case for void void reset(Bean[] beans).
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 * Invariants: If beanCount is greater than 0,
	 *             remaining bean count is beanCount - 1
	 *             in-flight bean count is 1 (the bean initially at the top)
	 *             in-slot bean count is 0.
	 *             If beanCount is 0,
	 *             remaining bean count is 0
	 *             in-flight bean count is 0
	 *             in-slot bean count is 0.
	 */
	@Test
	public void testReset() {
		logic.reset(beans);
		if (beanCount > 0) {
			assertEquals(logic.getRemainingBeanCount(), beanCount - 1);
			assertEquals(getInFlightBeanCount(), 1);
			assertEquals(getSlotBeanCount(), 0);
		} else if (beanCount == 0) {
			assertEquals(logic.getRemainingBeanCount(), 0);
			assertEquals(getInFlightBeanCount(), 0);
			assertEquals(getSlotBeanCount(), 0);
		}
	}

	/**
	 * Test case for boolean advanceStep().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 * Invariants: After each advanceStep(),
	 *             all positions of in-flight beans are legal positions in the logical coordinate system.
	 */
	@Test
	public void testAdvanceStepCoordinates() {
		logic.reset(beans);
		while (logic.advanceStep()) {
			for (int i = 0; i < slotCount; i++) {
				int xpos = logic.getInFlightBeanXPos(i);
				if (xpos > 0) {
					assertTrue(xpos <= i);
					assertTrue(i <= slotCount);
				}
				
			}
		}
	}

	/**
	 * Test case for boolean advanceStep().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 * Invariants: After each advanceStep(),
	 *             the sum of remaining, in-flight, and in-slot beans is equal to beanCount.
	 */
	@Test
	public void testAdvanceStepBeanCount() {
		logic.reset(beans);
		while (logic.advanceStep()) {
			int remBeans = logic.getRemainingBeanCount();
			int inFlight = getInFlightBeanCount();
			int inSlot = getSlotBeanCount();
			int totalBeans = remBeans + inFlight + inSlot;
			assertEquals(totalBeans, beanCount);
		}
	}

	/**
	 * Test case for boolean advanceStep().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 * Invariants: After the machine terminates,
	 *             remaining bean count is 0
	 *             in-flight bean count is 0
	 *             in-slot bean count is beanCount.
	 */
	@Test
	public void testAdvanceStepPostCondition() {
		logic.reset(beans);
		while (logic.advanceStep()) {
			
		}
		assertTrue(logic.getRemainingBeanCount() == 0);
		assertTrue(getInFlightBeanCount() == 0);
		assertTrue(getSlotBeanCount() == beanCount);
	}
	
	/**
	 * Test case for void lowerHalf()().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 *                  Call logic.lowerHalf().
	 * Invariants: After the machine terminates,
	 *             remaining bean count is 0
	 *             in-flight bean count is 0
	 *             in-slot bean count is beanCount.
	 *             After calling logic.lowerHalf(),
	 *             slots in the machine contain only the lower half of the original beans.
	 *             Remember, if there were an odd number of beans, (N+1)/2 beans should remain.
	 *             Check each slot for the expected number of beans after having called logic.lowerHalf().
	 */
	@Test
	public void testLowerHalf() {
		int[] prevSlotCount = new int[slotCount];
		logic.reset(beans);
		while (logic.advanceStep()) {
			
		}
		assertTrue(logic.getRemainingBeanCount() == 0);
		assertTrue(getInFlightBeanCount() == 0);
		assertTrue(getSlotBeanCount() == beanCount);
		for (int i = 0; i < slotCount; i++) {
			prevSlotCount[i] = logic.getSlotBeanCount(i);
			//System.out.print(" " + logic.getSlotBeanCount(i));
		}
		int stop = beanCount % 2 == 0 ? beanCount / 2 : (beanCount - 1) / 2;
		for (int i = 0, j = slotCount - 1; i < stop;) {
			if (prevSlotCount[j] == 0) {
				j--;
			} else {
				prevSlotCount[j]--;
				i++;
			}
		}
		logic.lowerHalf();
		assertTrue(getSlotBeanCount() == getHalfCount(beanCount));
		for (int i = 0; i < slotCount; i++) {
			assertTrue(logic.getSlotBeanCount(i) == prevSlotCount[i]);
		}

	}
	
	/**
	 * Test case for void upperHalf().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 *                  Call logic.upperHalf().
	 * Invariants: After the machine terminates,
	 *             remaining bean count is 0
	 *             in-flight bean count is 0
	 *             in-slot bean count is beanCount.
	 *             After calling logic.upperHalf(),
	 *             slots in the machine contain only the upper half of the original beans.
	 *             Remember, if there were an odd number of beans, (N+1)/2 beans should remain.
	 *             Check each slot for the expected number of beans after having called logic.upperHalf().
	 */
	@Test
	public void testUpperHalf() {
		int[] prevSlotCount = new int[slotCount];
		logic.reset(beans);
		while (logic.advanceStep()) {
			
		}
		assertTrue(logic.getRemainingBeanCount() == 0);
		assertTrue(getInFlightBeanCount() == 0);
		assertTrue(getSlotBeanCount() == beanCount);
		for (int i = 0; i < slotCount; i++) {
			prevSlotCount[i] = logic.getSlotBeanCount(i);
		}
		int stop = beanCount % 2 == 0 ? beanCount / 2 : (beanCount - 1) / 2;
		for (int i = 0, j = 0; i < stop; ) {
			if (prevSlotCount[j] == 0) {
				j++;
			} else {
				prevSlotCount[j]--;
				i++;
			}
		}
		logic.upperHalf();
		assertTrue(getSlotBeanCount() == getHalfCount(beanCount));
		for (int i = 0; i < slotCount; i++) {
			assertTrue(logic.getSlotBeanCount(i) == prevSlotCount[i]);
			
		}
	}
	
	/**
	 * Test case for void repeat().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 *                  Call logic.repeat();
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 * Invariants: If the machine is operating in skill mode,
	 *             bean count in each slot is identical after the first run and second run of the machine. 
	 */
	@Test
	public void testRepeat() {
		logic.reset(beans);
		while (logic.advanceStep()) {
			
		}
		int[] slotResults = new int[slotCount];
		for (int i = 0; i < slotCount; i++) {
			slotResults[i] = logic.getSlotBeanCount(i);
		}
		logic.repeat();
		while (logic.advanceStep()) {
			
		}
		
		if (!isLuck) {
			for (int i = 0; i < slotCount; i++) {
				assertTrue(slotResults[i] == logic.getSlotBeanCount(i));
			}
		}
	}
	
	/**
	 * Test case for void repeat().
	 * Preconditions: None.
	 * Execution steps: Call logic.reset(beans).
	 *                  Call logic.advanceStep() in a loop until it returns false (the machine terminates).
	 * Invariants:  After every advanceStep() call,
	 * 				verify that the x-position of the bean has no change or an increment by 1
	 *             
	 */
	@Test
	public void testSingleBeanStep() {
		logic.reset(beans);
		int prevX = 0;
		int prevY = 0;
		while (prevY < slotCount) {
			prevX = logic.getInFlightBeanXPos(prevY);
			logic.advanceStep();
			prevY += 1;
			int currX = logic.getInFlightBeanXPos(prevY);
			if (currX != -1) {
				int deltaX = Math.abs(prevX - currX);
				assertTrue(deltaX == 0 || deltaX == 1);
			}
		}
	}
}
