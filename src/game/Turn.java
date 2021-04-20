package game;

/**
 * manages the order of the moves of the current player.
 *
 */
public class Turn {
	/**
	 * Specifies the maximum number that the current player can build an element.
	 */
	private int maxBuild;
	/**
	 * Specifies the maximum number that the current player can move a figure.
	 */
	private int maxMove;
	/**
	 * indicates the number of times the current player has moved one of his figures
	 */
	private int moved;
	/**
	 * indicates the number of times the current player has built either a cuboid or
	 * a dome on a cell
	 */
	private int built;
	/**
	 * indicates whether a card has be drawn
	 */
	private boolean drawn;
	/**
	 * indicates whether an apollo card has be drawn
	 */
	private boolean apolloDrawn;
	/**
	 * indicates whether a hermes card has be drawn
	 */
	private boolean hermesDrawn;
	/**
	 * indicates whether an atlas card has be drawn
	 */
	private boolean atlasDrawn;
	/**
	 * indicates whether an athena card has be drawn
	 */
	private boolean athenaDrawn;
	/**
	 * indicates whether the current player can move his figures to a higher level.
	 */
	private boolean canMoveUp;

	public Turn() {
		this.moved = 0;
		this.built = 0;
		this.maxMove = 1;
		this.maxBuild = 1;
		this.drawn = false;
		this.apolloDrawn = false;
		this.hermesDrawn = false;
		this.athenaDrawn = false;
		this.atlasDrawn = false;
		this.canMoveUp = true;
	}

	public Turn(boolean canMoveUp) {
		this();
		this.canMoveUp = canMoveUp;
	}

	public boolean isDrawn() {
		return drawn;
	}

	public void hasDrawn() {
		this.drawn = true;
	}

	public boolean canMove() {
		return moved < maxMove;
	}

	public void increaseMove() {
		++moved;
	}

	public boolean canBuild() {
		return built < maxBuild;
	}

	public void increaseBuilt() {
		++built;
	}

	public boolean isApolloDrawn() {
		return apolloDrawn;
	}

	public void setApolloDrawn(boolean apolloDrawn) {
		this.apolloDrawn = apolloDrawn;
	}

	public boolean isHermesDrawn() {
		return hermesDrawn;
	}

	public void setHermesDrawn(boolean hermesDrawn) {
		this.hermesDrawn = hermesDrawn;
	}

	public boolean isAtlasDrawn() {
		return atlasDrawn;
	}

	public void setAtlasDrawn(boolean atlasDrawn) {
		this.atlasDrawn = atlasDrawn;
	}

	public boolean isAthenaDrawn() {
		return athenaDrawn;
	}

	public void setAthenaDrawn(boolean athenaDrawn) {
		this.athenaDrawn = athenaDrawn;
	}

	public int getMoved() {
		return moved;
	}

	public int getBuilt() {
		return built;
	}

	public void setMaxBuild(int maxBuild) {
		this.maxBuild = maxBuild;
	}

	public void setMaxMove(int max) {
		this.maxMove = max;
	}

	public boolean getCanMoveUp() {
		return canMoveUp;
	}

	public void setCanMoveUp(boolean canMoveUp) {
		this.canMoveUp = canMoveUp;
	}

}
