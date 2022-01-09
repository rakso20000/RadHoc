package radhoc.gamelogic;

public interface GameLogicTicTacToe extends GameLogic {
	
	/**
	 * Tries playing a shape at the specified position
	 * Top left corner: (0, 0)
	 * Bottom left corner: (0, 2)
	 * Top right corner: (2, 0)
	 * Bottom right corner: (2, 2)
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @return true if the shape was played successfully, false if the move was not possible
	 * @throws IllegalArgumentException x or y are out of bounds
	 */
	boolean playShapeAt(int x, int y) throws IllegalArgumentException;
	
}