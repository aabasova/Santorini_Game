package elements;

/**
 * represents the elements that can be placed on the board cells.
 *
 */
public class Element {
	/**
	 * the x-coordinate of the element on the board
	 */
	protected int x;
	/**
	 * the y-coordinate of the element on the board
	 */
	protected int y;
	private String name;

	public Element(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getName() {
		return name;
	}

}
