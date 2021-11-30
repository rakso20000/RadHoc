package radhoc.gamestates;

public interface GameStateTicTacToe extends GameState {
	
	public static enum Shape {
		NONE,
		CIRCLE,
		CROSS;
	}
	
	/**
	 * Returns which shape this device's user plays
	 * @return Shape
	 */
	Shape getOwnShape();
	
	/**
	 * Returns the shape at the specified position
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @return Shape
	 */
	Shape getShapeAt(int x, int y);
	
	/**
	 * Sets the shape at the specified position
	 * @param x 0 <= x <= 2
	 * @param y 0 <= y <= 2
	 * @param shape
	 */
	void setShapeAt(int x, int y, Shape shape);
}