package radhoc.gamestates;

public interface GameStateTicTacToe extends GameState {
	
	enum Shape {
		NONE,
		CIRCLE,
		CROSS
	}
	
	/**
	 * Returns which shape this device's user plays
	 * @return Shape
	 */
	Shape getOwnShape();
	
	/**
	 * Returns the shape at the specified position
	 * Top left corner: (0, 0)
	 * Bottom left corner: (0, 2)
	 * Top right corner: (2, 0)
	 * Bottom right corner: (2, 2)
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @return Shape
	 * @throws IllegalArgumentException x or y are out of bounds
	 */
	Shape getShapeAt(int x, int y) throws IllegalArgumentException;
	
	/**
	 * Sets the shape at the specified position
	 * Top left corner: (0, 0)
	 * Bottom left corner: (0, 2)
	 * Top right corner: (2, 0)
	 * Bottom right corner: (2, 2)
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @param shape
	 * @throws IllegalArgumentException x or y are out of bounds
	 */
	void setShapeAt(int x, int y, Shape shape) throws IllegalArgumentException;
	
	/**
	 * Marks this game as won
	 */
	void win();
	
	/**
	 * Marks this game as lost
	 */
	void lose();
	
	/**
	 * Marks this game as a draw
	 */
	void draw();
	
}