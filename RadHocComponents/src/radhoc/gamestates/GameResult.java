package radhoc.gamestates;

public enum GameResult {
	
	STILL_PLAYING,
	VICTORY,
	DEFEAT,
	DRAW;
	
	public static GameResult fromByte(byte b) throws GameStateParseException {
		
		return switch (b) {
			case 0 -> STILL_PLAYING;
			case 1 -> VICTORY;
			case 2 -> DEFEAT;
			case 3 -> DRAW;
			default -> throw new GameStateParseException(String.format("GameResult for value %d does not exist", b));
		};
		
	}
	
	public byte toByte() {
		
		return switch (this) {
			case STILL_PLAYING -> 0;
			case VICTORY -> 1;
			case DEFEAT -> 2;
			case DRAW -> 3;
		};
		
	}
	
}