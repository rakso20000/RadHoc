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
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void setShapeAt(int x, int y, Shape shape) throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * Marks this game as won
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void win() throws IllegalStateException;
	
	/**
	 * Marks this game as lost
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void lose() throws IllegalStateException;
	
	/**
	 * Marks this game as a draw
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void draw() throws IllegalStateException;
	
}