package radhoc.gamestates;

import java.util.List;

public interface GameStateRockPaperScissors extends GameState {
	
	enum Shape {
		ROCK,
		PAPER,
		SCISSORS
	}
	
	/**
	 * Adds one to this player's score
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void playerScored() throws IllegalStateException;
	
	/**
	 * Adds one to the opponent's score
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void opponentScored() throws IllegalStateException;
	
	/**
	 * Returns how many rounds this player has won
	 * @return int
	 */
	int getPlayerScore();
	
	/**
	 * Returns how many rounds the opponent has won
	 * @return int
	 */
	int getOpponentScore();
	
	/**
	 * Adds a shape to the shapes this player has played
	 * @param shape
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void addPlayerShape(Shape shape) throws IllegalStateException;
	
	/**
	 * Adds a shape to the shapes the opponent has played
	 * @param shape
	 * @throws IllegalStateException when this GameState is not STILL_PLAYING
	 */
	void addOpponentShape(Shape shape) throws IllegalStateException;
	
	/**
	 * Returns the Shapes this player has played
	 * @return List of Shape
	 */
	List<Shape> getPlayerShapes();
	
	/**
	 * Returns the Shapes the opponent has played
	 * @return List of Shape
	 */
	List<Shape> getOpponentShapes();
	
	/**
	 * Sets whether this GameState is playable. If this GameState is not STILL_PLAYING the GameState won't be playable regardless
	 * @param playable
	 */
	void setPlayable(boolean playable);
	
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