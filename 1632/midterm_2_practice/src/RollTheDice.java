import java.util.Random;

public class RollTheDice {
	private int p1Chips;
	private int p2Chips;
	private int rolls;
	private Random rand;

	RollTheDice(int chips1, int chips2) {
		p1Chips = chips1;
		p2Chips = chips2;
		rolls = 0;
		rand = new Random();
	}

	public int getP1Chips() { return p1Chips; }
	public int getP2Chips() { return p2Chips; }

	public boolean round(int roll1, int roll2) {
		if (p1Chips > 0 && p2Chips > 0) {
			p1Chips = p1Chips + (roll1 - roll2);
			p2Chips = p2Chips - (roll1 - roll2);
			if (p1Chips + p2Chips <= 0) {
				assert false;
			}
			return true;
		}
		return false;
	}

	public void play() {
		while (true) {
			int roll1 = rand.nextInt(6);
			int roll2 = rand.nextInt(6);
			if (round(roll1, roll2) == false) {
				break;
			}
			rolls++;
		}
	}

	public static void main(String[] args) {
		RollTheDice game = new RollTheDice(10, 10);
		game.play();
	}

}