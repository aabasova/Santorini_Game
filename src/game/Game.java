package game;

import java.util.ArrayList;

import data.Board;
import data.CardSet;
import data.Cell;
import data.Player;
import edu.kit.informatik.Terminal;
import elements.Cuboid;
import elements.Dome;
import elements.Element;
import elements.Figure;

/**
 * Manages the current game.
 *
 */
public class Game {
	/**
	 * the board of the game.
	 */
	private Board board;
	/**
	 * Represents player one.
	 */
	private Player p1;
	/**
	 * Represents player two.
	 */
	private Player p2;
	/**
	 * Indicates whether the game is still on and no player has won yet.
	 */
	private boolean gameOn;
	/**
	 * Manages the turn of a player.
	 */
	private Turn turn;
	/**
	 * Contains the card set for game.
	 */
	private CardSet cardset;

	public Game() {
		this.board = new Board();
		this.gameOn = true;
		this.cardset = new CardSet();
	}

	/**
	 * Creates the two players, their figures and creates a turn instance.
	 * 
	 * @param figure1
	 * @param figure2
	 * @param figure3
	 * @param figure4
	 */
	public void initializePlayers(String figure1, String figure2, String figure3, String figure4) {
		p1 = new Player("p1");
		p2 = new Player("p2");
		p1.setTurn(true);
		p2.setTurn(false);
		String[] figure1p1 = figure1.split(";");
		String[] figure2p1 = figure2.split(";");
		String[] figure1p2 = figure3.split(";");
		String[] figure2p2 = figure4.split(";");
		String[] names = { figure1p1[0], figure2p1[0], figure1p2[0], figure2p2[0] };
		if (checkFigureNames(names)) {
			p1.placeFigures(figure1p1[0], figure1p1[1], figure1p1[2], figure2p1[0], figure2p1[1], figure2p1[2]);
			p2.placeFigures(figure1p2[0], figure1p2[1], figure1p2[2], figure2p2[0], figure2p2[1], figure2p2[2]);

			for (Figure figure : p1.getFigures()) {
				board.addElement(figure, figure.getX(), figure.getY());
			}
			for (Figure figure : p2.getFigures()) {
				board.addElement(figure, figure.getX(), figure.getY());
			}
			turn = new Turn();
		} else {
			Terminal.printError("incorrent figure name format.");
		}
	}

	/**
	 * Checks whether the four figure names match to the given pattern.
	 * 
	 * @param names a String array
	 * @return true all names match to the given pattern
	 */
	public boolean checkFigureNames(String[] names) {
		for (String name : names) {
			if (!name.matches("[0-9a-z]+")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Changes the turns and creates a new turn instance. Prints the name of the
	 * player whose turn is to play.
	 */
	public void changeTurns() {
		if (turn.getMoved() > 0 && turn.getBuilt() > 0) {
			if (turn.getCanMoveUp()) {
				turn = new Turn();
			} else {
				turn = new Turn(false);
			}
			p1.changeTurn();
			p2.changeTurn();
			Terminal.printLine(getActivePlayer().getName());
		} else {
			Terminal.printError("each player has to at least move a figure and build an element.");
		}
	}

	/**
	 * Returns the player instance which is still in turn.
	 * 
	 * @return a player instance
	 */
	public Player getActivePlayer() {
		if (p2.isTurn()) {
			return p2;
		} else {
			return p1;
		}
	}

	/**
	 * Returns the player instance which is not in turn.
	 * 
	 * @return a player instance
	 */
	public Player getInactivePlayer() {
		if (p2.isTurn()) {
			return p1;
		} else {
			return p2;
		}
	}

	/**
	 * Checks whether a figure can be moved from its initial coordinates to the
	 * destination coordinates.
	 * 
	 * @param fromX initial x-coordinate
	 * @param fromY initial y-coordinate
	 * @param toX   destination x-coordinate
	 * @param toY   destination y-coordinate
	 * @return true if the destination coordinates are around the initial
	 *         coordinates.
	 */
	public boolean moveAllowed(int fromX, int fromY, int toX, int toY) {
		ArrayList<Cell> cells = board.getNeighbors(fromX, fromY);
		for (Cell c : cells) {
			if (c.getX() == toX && c.getY() == toY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parses the given parameter. Creates either a dome or a cuboid element based
	 * on the first letter of the given parameter. Checks whether the element can be
	 * placed to the coordinates. If not an appropriate message is printed. If yes
	 * then the new element is added to the board's cell and the turn instance is
	 * updated.
	 * 
	 * @param parsedCommand a String array
	 */
	public void build(String[] parsedCommand) {
		String[] values = parsedCommand[1].split(";");
		String type = values[0];
		int x = Integer.parseInt(values[1]);
		int y = Integer.parseInt(values[2]);
		Element element = null;
		if (type.equals("C")) {
			element = new Cuboid(x, y);
		} else if (type.equals("D")) {
			element = new Dome(x, y);
		} else {
			Terminal.printError("please choose a valid type");
			return;
		}
		if (turn.getMoved() == 0) {
			Terminal.printError("you can build an element after moving one of your figures.");
		} else if (!turn.canBuild()) {
			Terminal.printError("you have reached the maximum number of built elements in this turn.");
		} else if (board.getCells()[x][y].getContainsTower() && element instanceof Cuboid) {
			Terminal.printError("the maximum number of cuboids has been reached on this cell.");
		} else if (board.getCells()[x][y].isOccupied()) {
			Terminal.printError("this cell is occupied by another player's figure.");
		} else if (element instanceof Dome && board.getAvailableDomes() == 0) {
			Terminal.printError("no more domes are available in the board.");
		} else if (element instanceof Cuboid && board.getAvailableCuboids() == 0) {
			Terminal.printError("no more cuboids are available in the board.");
		} else if (turn.isAtlasDrawn() && element instanceof Dome) {
			board.addElementAtlas((Dome) element, x, y);
			turn.increaseBuilt();
			updateGameOn();
		} else if (!board.getCells()[x][y].getContainsTower() && element instanceof Dome) {
			Terminal.printError("you can build a Dome on a tower.");
		} else {
			this.board.addElement(element, x, y);
			turn.increaseBuilt();
			updateGameOn();
		}
	}

	/**
	 * Checks if drawing a card is possible. If not an appropriate message is
	 * printed. If yes then the drawnCards variable of the player is updated. The
	 * card is removed from the cardset and the turn instance is updated.
	 * 
	 * @param cardSymbol the card name
	 */
	public void drawCard(String cardSymbol) {
		if (this.turn.isDrawn()) {
			Terminal.printError("you have already drawn a card in this turn.");
		} else if (turn.getMoved() > 0) {
			Terminal.printError("you have already moved a figure.");
		} else if (turn.getBuilt() > 0) {
			Terminal.printError("you have already built an element.");
		} else if (!getActivePlayer().canDrawCard()) {
			Terminal.printError("you have reached the maximum number of drawn cards.");
		} else if (!draw(cardSymbol)) {
			Terminal.printError("please enter a valid cardsymbol.");
		} else {
			this.turn.hasDrawn();
			getActivePlayer().increaseDrawnCards();
			cardset.remove(cardSymbol);
			Terminal.printLine("OK");
		}
	}

	/**
	 * Checks if the given card name exists in the card set. If not a message is
	 * printed. If yes, then the corresponding updates are executed.
	 * 
	 * @param cardSymbol the card name
	 * @return true if the given card name exists in the card set
	 */
	public boolean draw(String cardSymbol) {
		if (cardset.contains(cardSymbol)) {
			switch (cardSymbol) {
			case "Apollo":
				turn.setApolloDrawn(true);
				break;
			case "Artemis":
				this.turn.setMaxMove(2);
				break;
			case "Athena":
				turn.setAthenaDrawn(true);
				break;
			case "Atlas":
				turn.setAtlasDrawn(true);
				break;
			case "Demeter":
				this.turn.setMaxBuild(2);
				break;
			case "Hermes":
				turn.setHermesDrawn(true);
				break;
			default:
				Terminal.printError("card symbol not recognized");
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if moving the given figure is possible. If not then an appropriate
	 * error-message is printed. If moving the figure is possible, then the figure
	 * will be added to the selected cell on the board. The new coordinates of the
	 * figure are saved for the player. The figure level is updated.
	 * 
	 * @param figureName the name of the figure that is going to be moved
	 * @param toX        the destination x-coordinate
	 * @param toY        the destination y-coordinate
	 */
	public void move(String figureName, int toX, int toY) {
		Figure figure = getActivePlayer().getFigure(figureName);
		if (!turn.canMove()) {
			Terminal.printError("you have reached the maximum number of moves in this turn.");
		} else if (getActivePlayer().getFigure(figureName) == null) {
			Terminal.printError("this player doesnt own a figure with the given figure name.");
		} else if (turn.getBuilt() > 0) {
			Terminal.printError("you have already built a dome or a cuboid.");
		} else if (!board.canMoveFigureBasedOnLevel(figure, toX, toY)) {
			Terminal.printError("the specified cell is more than one level higher than your figure level.");
		} else if (!turn.getCanMoveUp() && board.getCells()[toX][toY].getCellLevel() > figure.getLevel()) {
			Terminal.printError("you cant move up as your opponent has previously drawn an athena card.");
		} else if (turn.isHermesDrawn() && board.getCells()[toX][toY].getCellLevel() == figure.getLevel()) {
			board.moveFigureHermes(figure, toX, toY);
			getActivePlayer().move(figureName, toX, toY);
			turn.increaseMove();
			updateGameOn();
		} else if (!moveAllowed(figure.getX(), figure.getY(), toX, toY)) {
			Terminal.printError("you can move your figure to one of its surrounding cells.");
		} else if (turn.isApolloDrawn() && board.getCells()[toX][toY].isOccupied()) {
			Figure opponent = board.moveFigureApollo(figure, toX, toY);
			int newFigureLevel = board.getCellLevel(toX, toY);
			int opponentLevel = board.getCellLevel(opponent.getX(), opponent.getY());

			getActivePlayer().move(figureName, toX, toY);
			getActivePlayer().getFigure(figureName).setLevel(newFigureLevel);
			turn.increaseMove();

			getInactivePlayer().move(opponent.getName(), opponent.getX(), opponent.getY());
			getInactivePlayer().getFigure(opponent.getName()).setLevel(opponentLevel);
			updateGameOn();
		} else if (board.getCells()[toX][toY].isBlocked()) {
			Terminal.printError("this cell is blocked, it either contains another figure or a dome");
		} else if (turn.isAthenaDrawn() && board.getCellLevel(toX, toY) == (figure.getLevel() + 1)) {
			turn.setCanMoveUp(false);
			board.moveFigure(figure, toX, toY);
			int newFigureLevel = board.getCellLevel(toX, toY);
			getActivePlayer().move(figureName, toX, toY);
			getActivePlayer().getFigure(figureName).setLevel(newFigureLevel);
			turn.increaseMove();
			updateGameOn();
		} else {
			int newFigureLevel = board.getCellLevel(toX, toY);
			board.moveFigure(figure, toX, toY);
			getActivePlayer().move(figureName, toX, toY);
			getActivePlayer().getFigure(figureName).setLevel(newFigureLevel);
			turn.increaseMove();
			updateGameOn();
		}
	}

	/**
	 * Checks whether the current player has a figure that has reached the third
	 * level, meaning it stands on a tower thus the player has won. It also checks
	 * whether the opponent can move one of its figures or build an element and
	 * updates the gameOn variable.
	 */
	public void updateGameOn() {
		for (Figure figure : getActivePlayer().getFigures()) {
			if (figure.getLevel() == 4) {
				gameOn = false;
				return;
			}
		}
		Figure[] opponentFigures = getInactivePlayer().getFigures();
		int x1 = opponentFigures[0].getX();
		int y1 = opponentFigures[0].getY();
		int x2 = opponentFigures[1].getX();
		int y2 = opponentFigures[1].getY();
		ArrayList<Cell> cel1 = board.getUnblockedNeighbors(x1, y1);
		ArrayList<Cell> cel2 = board.getUnblockedNeighbors(x2, y2);
		if (cel1.isEmpty() && cel2.isEmpty()) {
			gameOn = false;
			return;
		}
		boolean freeCell = false;
		for (Cell[] cellLine : board.getCells()) {
			for (Cell cell : cellLine) {
				if (!cell.isBlocked()) {
					freeCell = true;
				}
			}
		}
		if (!freeCell) {
			gameOn = false;
			return;
		}
		if (gameOn) {
			Terminal.printLine("OK");
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		if (args.length == 4) {
			game.initializePlayers(args[0], args[1], args[2], args[3]);
			while (game.gameOn) {
				String[] parsedCommand = Terminal.readLine().split(" ");
				if (parsedCommand[0].equals("draw-card")) {
					game.drawCard(parsedCommand[1]);
				} else if (parsedCommand[0].equals("move")) {
					String[] values = parsedCommand[1].split(";");
					int x = Integer.parseInt(values[1]);
					int y = Integer.parseInt(values[2]);
					game.move(values[0], x, y);
				} else if (parsedCommand[0].equals("build")) {
					game.build(parsedCommand);
				} else if (parsedCommand[0].equals("print")) {
					Terminal.printLine(game.board.print());
				} else if (parsedCommand[0].equals("cellprint")) {
					String[] values = parsedCommand[1].split(";");
					int x = Integer.parseInt(values[0]);
					int y = Integer.parseInt(values[1]);
					Terminal.printLine(game.board.getCells()[x][y].cellprint());
				} else if (parsedCommand[0].equals("surrender")) {
					game.p1.changeTurn();
					game.p2.changeTurn();
					game.gameOn = false;
				} else if (parsedCommand[0].equals("bag")) {
					Terminal.printLine(game.board.bag());
				} else if (parsedCommand[0].equals("turn")) {
					game.changeTurns();
				} else if (parsedCommand[0].equals("quit")) {
					return;
				} else {
					Terminal.printError("unknown command.");
				}
			}
			Terminal.printLine(game.getActivePlayer().getName() + " wins");
		} else {
			Terminal.printError("please enter two figures and their positions for each player.");
		}
	}
}
