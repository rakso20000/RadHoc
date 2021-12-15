package radhoc.gamestates;

public enum GameType {
	
	TIC_TAC_TOE;
	
	public static GameType fromByte(byte b) throws IllegalArgumentException {
		
		return switch (b) {
			case 0 -> TIC_TAC_TOE;
			default -> throw new IllegalArgumentException();
		};
		
	}
	
	public byte toByte() {
		
		return switch (this) {
			case TIC_TAC_TOE -> 0;
		};
		
	}
	
}