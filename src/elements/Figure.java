package elements;

/**
 * represents one of the two individual figures of a player
 */
public class Figure extends Element {
	private int level;

	public Figure(String name, int x, int y) {
		super(name, x, y);
		this.level = 0;
	}

	/**
	 * Moves the figure to another cell
	 * 
	 * @param x
	 * @param y
	 */
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
