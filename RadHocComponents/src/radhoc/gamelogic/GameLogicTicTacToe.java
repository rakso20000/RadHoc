package radhoc.gamelogic;

public interface GameLogicTicTacToe extends GameLogic {
	/**
	 * Tries playing a shape at the specified position
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @return true if the shape was played successfully, false if the move was not possible
	 */
	boolean playShapeAt(int x, int y);
}