package data;

import java.util.ArrayList;

import elements.Cuboid;
import elements.Dome;
import elements.Element;
import elements.Figure;

/**
 * represents the board of the game
 *
 */
public class Board {
	/**
	 * contains the individual cells of the boards
	 */
	private Cell[][] cells;
	/**
	 * indicates the number of available domes for the current game
	 */
	private int availableDomes;
	/**
	 * indicates the number of available cuboids for the current game
	 */
	private int availableCuboids;

	public Board() {
		cells = new Cell[5][5];
		availableCuboids = 54;
		availableDomes = 18;
		initializeCells();
	}

	/**
	 * initializes the 5x5 board with 25 cells and their positions.
	 */
	public void initializeCells() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				this.cells[i][j] = new Cell(i, j);
			}
		}
	}

	/**
	 * Adds either a cuboid or a dome to the specified Cell coordinates and updates
	 * the corresponding varieble
	 * 
	 * @param element a cuboid or a dome
	 * @param x       the new x-coordinate to place the element on
	 * @param y       the new y-coordinate to place the element on
	 */
	public void addElement(Element element, int x, int y) {
		if (element instanceof Cuboid) {
			decreaseCuboids();
		}
		if (element instanceof Dome) {
			decreaseDomes();
		}
		cells[x][y].addElement(element);
	}

	public void addElementAtlas(Dome dome, int x, int y) {
		decreaseDomes();
		cells[x][y].addElementAtlas(dome);
	}

	/**
	 * Checks whether the figure can be placed on the cell corresponding to the
	 * given coordinates based on the number of elements placed on the cell
	 * 
	 * @param figure the figure to move
	 * @param x      the x-coordinate of the cell
	 * @param y      the y-coordinate of the cell
	 * @return true if the destination cell isnt over one level higher than the
	 *         figure
	 */
	public boolean canMoveFigureBasedOnLevel(Figure figure, int x, int y) {
		return (cells[x][y].getCellLevel() <= figure.getLevel()
				|| cells[x][y].getCellLevel() == (figure.getLevel() + 1));
	}

	/**
	 * Removes the figure from its old cell and adds it to the cell which
	 * corresponds to the given coordinates
	 * 
	 * @param figure the figure to move
	 * @param x      destination x-coordinate
	 * @param y      destination y-coordinate
	 * @return true if moving the figure was successful
	 */
	public boolean moveFigure(Figure figure, int x, int y) {
		if (canMoveFigureBasedOnLevel(figure, x, y)) {
			cells[figure.getX()][figure.getY()].removeElement(figure);
			cells[x][y].addElement(figure);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * Moves the given figure to the cell corresponding to the given coordinates and
	 * places the opponent figure from the destination coordinates to the initial
	 * coordinates of the figure
	 * 
	 * @param figure figure to move
	 * @param x      destination x-coordinate
	 * @param y      destination y-coordinate
	 * @return the opponent figure that has been moved to the initial cell of the
	 *         parameter figure
	 */
	public Figure moveFigureApollo(Figure figure, int x, int y) {
		int initialX = figure.getX();
		int initialY = figure.getY();
		cells[figure.getX()][figure.getY()].removeElement(figure);
		Figure opponentFigure = cells[x][y].addElementApollo(figure);
		cells[initialX][initialY].addElement(opponentFigure);
		return opponentFigure;
	}

	public void moveFigureHermes(Figure figure, int x, int y) {
		cells[figure.getX()][figure.getY()].removeElement(figure);
		cells[x][y].addElement(figure);
	}

	/**
	 * Creates a String with the top element of each cell of the given line
	 * 
	 * @param line the line number
	 * @return a String representation of the top Elements
	 */
	public String printLine(int line) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cells.length; i++) {
			Element topElement = cells[line][i].getTopElement();
			if (i != 0) {
				sb.append(",");
			}
			if (topElement == null) {
				sb.append(".");
			} else {
				sb.append(topElement.getName());
			}
		}
		return sb.toString();
	}

	/**
	 * Creates a String representation with the top element of each cell on the
	 * board
	 * 
	 * @return the top element of each cell in the entire board
	 */
	public String print() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cells.length; i++) {
			sb.append(printLine(i));
			if (i < cells.length - 1) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Creates an ArrayList of cells containing or unblocked cell around the cell
	 * corresponding to the given coordinates. An unblocked cell is one which doesnt
	 * contain a figure or a dome
	 * 
	 * @param x
	 * @param y
	 * @return an ArraList with cells.
	 */
	public ArrayList<Cell> getUnblockedNeighbors(int x, int y) {
		ArrayList<Cell> unblocked = new ArrayList<Cell>();
		for (Cell cell : getNeighbors(x, y)) {
			if (!cell.isBlocked()) {
				unblocked.add(cell);
			}
		}
		return unblocked;
	}

	/**
	 * Creates an ArrayList of cells containing all the cells around the cell
	 * corresponding to the given coordinates.
	 * 
	 * @param i
	 * @param j
	 * @return ArrayList with cells.
	 */
	public ArrayList<Cell> getNeighbors(int x, int y) {
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		if (isWithinBounds(x, y)) {
			if (isWithinBounds(x + 1, y))
				neighbors.add(cells[x + 1][y]);
			if (isWithinBounds(x - 1, y))
				neighbors.add(cells[x - 1][y]);
			if (isWithinBounds(x, y + 1))
				neighbors.add(cells[x][y + 1]);
			if (isWithinBounds(x, y - 1))
				neighbors.add(cells[x][y - 1]);
			if (isWithinBounds(x - 1, y + 1))
				neighbors.add(cells[x - 1][y + 1]);
			if (isWithinBounds(x + 1, y - 1))
				neighbors.add(cells[x + 1][y - 1]);
			if (isWithinBounds(x + 1, y + 1))
				neighbors.add(cells[x + 1][y + 1]);
			if (isWithinBounds(x - 1, y - 1))
				neighbors.add(cells[x - 1][y - 1]);
		}
		return neighbors;
	}

	/**
	 * @param x
	 * @param y
	 * @return true if the coordinates are within the bounds of the board
	 */
	public boolean isWithinBounds(int x, int y) {
		boolean flag = false;
		if (x >= 0 && y >= 0 && x <= cells.length - 1 && y <= cells.length - 1) {
			flag = true;
		}
		return flag;
	}

	public void decreaseDomes() {
		this.availableDomes = availableDomes - 1;
	}

	public void decreaseCuboids() {
		this.availableCuboids = availableCuboids - 1;
	}

	public int getAvailableDomes() {
		return availableDomes;
	}

	public int getAvailableCuboids() {
		return availableCuboids;
	}

	/**
	 * Creates a String representation of the existing domes and cuboids and their
	 * number
	 * 
	 * @return a String representation of the existing domes and cuboids
	 */
	public String bag() {
		return "C;" + this.availableCuboids + "D;" + this.availableDomes;
	}

	public Cell[][] getCells() {
		return cells;
	}

	/**
	 * Returns the cell level of the corresponding cell.
	 * 
	 * @param x
	 * @param y
	 * @return the cell level
	 */
	public int getCellLevel(int x, int y) {
		return cells[x][y].getCellLevel();
	}

}
