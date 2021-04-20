package data;

/**
 * Represents a set of cards
 *
 */
public class CardSet {
	/**
	 * Contains the card symbols
	 */
	private String[] symbols = { "Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hermes" };

	/**
	 * Checks if the given cardname exists
	 * 
	 * @param card the cardname
	 * @return true if cardname exists in the symbols array
	 */
	public boolean contains(String card) {
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] != null) {
				if (symbols[i].equals(card)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the given cardname from the array
	 * 
	 * @param cardSymbol cardname
	 */
	public void remove(String cardSymbol) {
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] != null) {
				if (symbols[i].equals(cardSymbol)) {
					symbols[i] = null;
					break;
				}
			}
		}
	}

}
