package radhoc.gamestates;

public enum GameType {
	
	TIC_TAC_TOE;
	
	public static GameType fromByte(byte b) throws GameStateParseException {
		
		return switch (b) {
			case 0 -> TIC_TAC_TOE;
			default -> throw new GameStateParseException(String.format("GameType for value %d does not exist", b));
		};
		
	}
	
	public byte toByte() {
		
		return switch (this) {
			case TIC_TAC_TOE -> 0;
		};
		
	}
	
	@Override
	public String toString() {
		
		return switch (this) {
			case TIC_TAC_TOE -> "TicTacToe";
		};
		
	}
}