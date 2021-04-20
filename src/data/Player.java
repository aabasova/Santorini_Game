package data;

import elements.Figure;

/**
 * Represents a player of the game
 *
 */
public class Player {
	private String name;
	/**
	 * Indicates the maximum number of cards a player can drawn during a game.
	 */
	private int maxCards;
	/**
	 * Indicates the actual number of cards the player has drawn.
	 */
	private int drawnCards;
	/**
	 * Contains the two figures of the player
	 */
	private Figure[] figures;
	/**
	 * Indicates if its this player's turn to play.
	 */
	private boolean turn;

	public Player(String name) {
		this.name = name;
		figures = new Figure[2];
		drawnCards = 0;
		maxCards = 3;
	}

	/**
	 * Initialize the personal figures of the player
	 * 
	 * @param firstName  name of the first figure
	 * @param x1         x-coordinate of first figure
	 * @param y1         y-coordinate of first figure
	 * @param secondName name of the second figure
	 * @param x2         x-coordinate of second figure
	 * @param y2         y-coordinate of second figure
	 */
	public void placeFigures(String firstName, String x1, String y1, String secondName, String x2, String y2) {
		figures[0] = new Figure(firstName, Integer.parseInt(x1), Integer.parseInt(y1));
		figures[1] = new Figure(secondName, Integer.parseInt(x2), Integer.parseInt(y2));
	}

	/**
	 * Moves the players figure to the specified cell-coordinates.
	 * 
	 * @param figureName
	 * @param toX
	 * @param toY
	 * @return true if the move was successful
	 */
	public boolean move(String figureName, int toX, int toY) {
		for (Figure figure : figures) {
			if (figure.getName().equals(figureName)) {
				figure.move(toX, toY);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if the player reached the limit of drawing cards.
	 */
	public boolean canDrawCard() {
		return drawnCards < maxCards;
	}

	public void increaseDrawnCards() {
		++this.drawnCards;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public void changeTurn() {
		this.turn = !turn;
	}

	public boolean isTurn() {
		return turn;
	}

	public Figure[] getFigures() {
		return figures;
	}

	/**
	 * Returns the figure instance based on the given figure name.
	 * 
	 * @param name
	 * @return a figure instance
	 */
	public Figure getFigure(String name) {
		for (Figure figure : figures) {
			if (figure.getName().equals(name)) {
				return figure;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public int getDrawnCards() {
		return drawnCards;
	}

}
