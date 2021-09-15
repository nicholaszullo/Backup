import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class MonkeySimStochasticTest {
	/**
	 * TODO: For Extra Credit, write a property-based test method testMonkeySim.
	 * Invokes runSimulation with s being the starting monkey with the banana. The
	 * invariant that you should check is that runSimulation does not throw an
	 * InfiniteLoopException. As of now, runSimulation throws no exception of the
	 * kind and just falls into the infinite loop, given the defect triggering
	 * sequence. So, MonkeySim will also have to be modified to throw that exception
	 * for this to work.
	 * 
	 * @param s The starting monkey with the banana. Use the @InRange annotation to
	 *          make sure the generated random number for s is greater than or equal
	 *          to 1.
	 */
	private PrintStream stdout;
	
	@Before
	public void setUp() {
		// Force reset the monkey counter to 0 before initializing the test fixture, to
		// make sure the monkey numbering starts from 0 each time.
		try {
			Field f = Monkey.class.getDeclaredField("monkeyNum");
			f.setAccessible(true);
			f.set(null, 0);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Property(trials = 100)
	public void testMonkeySim(@InRange(minInt=1) int s) {
		System.out.println("s: " + s);
		try {
			Monkey tmpMonkey;
			Banana b = new Banana();
			MonkeyWatcher mw = new MonkeyWatcher();
	
			List<Monkey> monkeyList = new LinkedList<Monkey>();
			for (int j = 0; j < s + 1; j++) {
				tmpMonkey = new Monkey();
				monkeyList.add(tmpMonkey);
			}
			monkeyList.get(s).throwBananaTo(b);
	
			MonkeySim monkeySim = new MonkeySim();
			int numRounds = monkeySim.runSimulation(monkeyList, mw);
			System.out.println("Completed in " + numRounds + " rounds.");
		}
		catch(InfiniteLoopException e) {
			System.out.println(e);
		}
	}
}
