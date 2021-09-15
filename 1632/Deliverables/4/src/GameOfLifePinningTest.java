import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameOfLifePinningTest {
	/*
	 * READ ME: You may need to write pinning tests for methods from multiple
	 * classes, if you decide to refactor methods from multiple classes.
	 * 
	 * In general, a pinning test doesn't necessarily have to be a unit test; it can
	 * be an end-to-end test that spans multiple classes that you slap on quickly
	 * for the purposes of refactoring. The end-to-end pinning test is gradually
	 * refined into more high quality unit tests. Sometimes this is necessary
	 * because writing unit tests itself requires refactoring to make the code more
	 * testable (e.g. dependency injection), and you need a temporary end-to-end
	 * pinning test to protect the code base meanwhile.
	 * 
	 * For this deliverable, there is no reason you cannot write unit tests for
	 * pinning tests as the dependency injection(s) has already been done for you.
	 * You are required to localize each pinning unit test within the tested class
	 * as we did for Deliverable 2 (meaning it should not exercise any code from
	 * external classes). You will have to use Mockito mock objects to achieve this.
	 * 
	 * Also, you may have to use behavior verification instead of state verification
	 * to test some methods because the state change happens within a mocked
	 * external object. Remember that you can use behavior verification only on
	 * mocked objects (technically, you can use Mockito.verify on real objects too
	 * using something called a Spy, but you wouldn't need to go to that length for
	 * this deliverable).
	 */

	/* TODO: Declare all variables required for the test fixture. */
	private MainPanel mp;
	
	@Before
	public void setUp() {
		/*
		 * TODO: initialize the text fixture. For the initial pattern, use the "blinker"
		 * pattern shown in:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Examples_of_patterns
		 * The actual pattern GIF is at:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#/media/File:Game_of_life_blinker.gif
		 * Start from the vertical bar on a 5X5 matrix as shown in the GIF.
		 */
		mp = new MainPanel(5);
		Cell[][] cells = new Cell[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				cells[i][j] = Mockito.mock(Cell.class);
				if (i == 2 && j >= 1 && j <=3) {
					Mockito.when(cells[i][j].getAlive()).thenReturn(true);
				} else {
					Mockito.when(cells[i][j].getAlive()).thenReturn(false);
				}
			}
		}
		mp.setCells(cells);
		
	}
	
	/**
	 * Preconditions: all cells are not alive except [2][1], [2][2], and [2][3]
	 * Execution Steps: call mp.calculateNextIteration()
	 * Postcondtions: all cells are not alive except [1][2], [2][2], and [3][2]
	 * 
	 */
	@Test
	public void testNextIter1() {
		Cell[][] holder = mp.getCells();
		mp.calculateNextIteration();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (j == 2 && i >= 1 && i <= 3) {
					Mockito.verify(holder[i][j], Mockito.times(1)).setAlive(true);
				} else {
					Mockito.verify(holder[i][j], Mockito.times(1)).setAlive(false);
				}				
			}
		}
	}
	
	/**
	 * Preconditions: all cells are not alive except [2][1], [2][2], and [2][3]
	 * Execution Steps: Iterate cell [2][2] by calling mp.iterateCell(2,2)
	 * Postconditions: mp.iterateCell(2,2) returns true
	 */
	@Test
	public void testIterCellAliveMid() {
		assertTrue(mp.iterateCell(2, 2));
	}
	
	/**
	 * Preconditions: all cells are not alive except [2][1], [2][2], and [2][3]
	 * Execution Steps: Iterate cell [2][3] by calling mp.iterateCell(2,3)
	 * Postconditions: mp.iterateCell(2,3) returns false
	 */
	@Test
	public void testIterCellAliveRight() {
		assertFalse(mp.iterateCell(2, 3));
	}
	
	/**
	 * Preconditions: all cells are not alive except [2][1], [2][2], and [2][3]
	 * Execution Steps: Iterate cell [1][2] by calling mp.iterateCell(1,2)
	 * Postconditions: mp.iterateCell(1,2) returns true
	 */
	@Test
	public void testIterCellDeadTop() {
		assertTrue(mp.iterateCell(1, 2));
	}
	
	/**
	 * Preconditions: all cells are not alive except [2][1], [2][2], and [2][3]
	 * Execution Steps: Create three cells using the three constructors within Cell
	 * 1) Cell(true)
	 * 2) Cell(false)
	 * 3) Cell()
	 * Postconditions: validate that toString() for the true cell returns "X" and toString()
	 * returns "." for the false and default (unknown) cell
	 */
	@Test
	public void testToString() {
		Cell trueCell = new Cell(true);
		assertTrue(trueCell.toString().equals("X"));
		Cell falseCell = new Cell(false);
		assertTrue(falseCell.toString().equals("."));
		Cell unknownCell = new Cell();
		assertTrue(unknownCell.toString().equals("."));
	}


}
