package data;

import elements.Cuboid;
import elements.Dome;
import elements.Element;
import elements.Figure;

/**
 * Represents a cell in the board
 *
 */
public class Cell {
	/**
	 * the x-coordinate of the cell in the board
	 */
	private int x;
	/**
	 * the y-coordinate of the cell in the board
	 */
	private int y;
	/**
	 * Contains the elements placed on the cell
	 */
	private Element[] elements;
	/**
	 * Indicates whether the cell contains a tower
	 */
	private boolean containsTower;

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		elements = new Element[5];
		containsTower = false;
	}

	/**
	 * Method adds an element to the cell. The element can be a dome, a cuboid or a
	 * figure.
	 * 
	 * @param newElement
	 * @return true if adding the new element was successful
	 */
	public boolean addElement(Element newElement) {
		if (!isBlocked()) {
			if (!getContainsTower() || (getContainsTower() && newElement instanceof Dome)) {
				for (int i = 0; i < elements.length; i++) {
					if (elements[i] == null) {
						elements[i] = newElement;
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Adds a dome to the cell. Method is executed after an atlas card has been
	 * drawn.
	 * 
	 * @param dome dome to add
	 */
	public void addElementAtlas(Dome dome) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				elements[i] = dome;
				break;
			}
		}
	}

	/**
	 * If a figure is placed on the cell then the figure is removed and the
	 * parameter figure is added to the cell.
	 * 
	 * @param figure figure to add in the cell
	 * @return the figure that was standing on the cell before executing this
	 *         method.
	 */
	public Figure addElementApollo(Figure figure) {
		if (isOccupied()) {
			Figure opponentFigure = getOccupyingFigure();
			removeElement(getOccupyingFigure());
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] == null) {
					elements[i] = figure;
					break;
				}
			}
			return opponentFigure;
		}
		return null;
	}

	/**
	 * Removes the given element from the cell
	 * 
	 * @param element element to remove
	 */
	public void removeElement(Element element) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getName().equals(element.getName())) {
				elements[i] = null;
				break;
			}
		}
		shiftElements();
	}

	/**
	 * Shifts the elements of the array to the left so t hat all null elements are
	 * at the end of the array
	 */
	public void shiftElements() {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null && i < elements.length - 1) {
				if (elements[i + 1] != null) {
					elements[i] = elements[i + 1];
					elements[i + 1] = null;
				}
			}
		}
	}

	/**
	 * Indicates whether a cell is blocked. A cell is block if it contains a dome or
	 * a players figure
	 * 
	 * @return true if it contains a figure or a dome
	 */
	public boolean isBlocked() {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof Figure || elements[i] instanceof Dome) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if a figure is on the cell
	 */
	public boolean isOccupied() {
		for (Element element : elements) {
			if (element instanceof Figure) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the figure standing on the cell
	 */
	public Figure getOccupyingFigure() {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] instanceof Figure) {
				return (Figure) elements[i];
			}
		}
		return null;
	}

	/**
	 * @return true if three consecutive cuboids stand on the cell
	 */
	public boolean getContainsTower() {
		containsTower = false;
		if (elements.length > 3) {
			for (int i = 1; i < elements.length - 1; i++) {
				Element ele1 = elements[i - 1];
				Element ele2 = elements[i];
				Element ele3 = elements[i + 1];
				if (ele1 instanceof Cuboid && ele2 instanceof Cuboid && ele3 instanceof Cuboid) {
					containsTower = true;
				}
			}
		}
		return containsTower;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/**
	 * Returns the top element of the cell
	 * 
	 * @return the top element of the cell
	 */
	public Element getTopElement() {
		Element lastElement = null;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null) {
				lastElement = elements[i];
			} else {
				break;
			}
		}
		return lastElement;
	}

	/**
	 * Creates a String representation of the elements standing on the cell
	 * 
	 * @return a String representation
	 */
	public String cellprint() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(elements[i].getName());
			}
		}
		if (sb.length() == 0) {
			sb.append("Empty");
		}
		return sb.toString();
	}

	/**
	 * Computes the level of the cell. The level of the cell equals to the number of
	 * elements standing on the cell.
	 * 
	 * @return the number of elements standing on the cell
	 */
	public int getCellLevel() {
		int num = 0;
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null) {
				++num;
			}
		}
		return num;
	}
}
