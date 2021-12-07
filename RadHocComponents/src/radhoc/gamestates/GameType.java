package radhoc.gamestates;

public enum GameType {
	TIC_TAC_TOE;
	
	public static GameType fromByte(byte b) throws IllegalArgumentException {
		
		switch (b) {
		case 0:
			return TIC_TAC_TOE;
		default:
			throw new IllegalArgumentException();
		}
		
	}
	
	public byte toByte() {
		
		switch (this) {
		case TIC_TAC_TOE:
			return 0;
		default:
			throw new IllegalArgumentException();
		}
		
	}
	
}