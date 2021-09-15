import org.junit.*;

public class RollTheDiceJUnit {
	private RollTheDice game;
	
	@Before
	public void setUp() throws Exception {
		game = new RollTheDice(10, 10);
	}

	@Test
	public void testRound() {
		game.round(3, 5);
		assertTrue(game.getP1Chips() > 0);
		assertTrue(game.getP2Chips() > 0);
	}

	@Test
	public void testPlay() {
		game.play();
		int p1 = game.getP1Chips();
		int p2 = game.getP2Chips();
		
	}
}