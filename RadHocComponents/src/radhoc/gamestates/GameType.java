package radhoc.gamestates;

public enum GameType {
	
	TIC_TAC_TOE,
	ROCK_PAPER_SCISSORS;
	
	public static GameType fromByte(byte b) throws GameStateParseException {
		
		return switch (b) {
			case 0 -> TIC_TAC_TOE;
			case 1 -> ROCK_PAPER_SCISSORS;
			default -> throw new GameStateParseException(String.format("GameType for value %d does not exist", b));
		};
		
	}
	
	public byte toByte() {
		
		return switch (this) {
			case TIC_TAC_TOE -> 0;
			case ROCK_PAPER_SCISSORS -> 1;
		};
		
	}
	
	@Override
	public String toString() {
		
		return switch (this) {
			case TIC_TAC_TOE -> "TicTacToe";
			case ROCK_PAPER_SCISSORS -> "RockPaperScissors";
		};
		
	}
}