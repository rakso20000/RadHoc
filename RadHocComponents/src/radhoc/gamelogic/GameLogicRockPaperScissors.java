package radhoc.gamelogic;

import radhoc.gamestates.GameStateRockPaperScissors.Shape;

public interface GameLogicRockPaperScissors extends GameLogic {
	
	/**
	 * Tries playing the specified shape.
	 * @param shape
	 * @return true if the shape was successfully played, false if it's not the player's turn
	 */
	boolean playShape(Shape shape);
	
}