package radhoc.gamestates;

import static org.junit.jupiter.api.Assertions.*;

public class MockUpdateListener implements UpdateListener {
	
	private boolean updated;
	private boolean updatedTwice;
	
	@Override
	public void onUpdate() {
		
		if (updated)
			updatedTwice = true;
		
		updated = true;
		
	}
	
	public void assertNotUpdated() {
		
		assertFalse(updated);
		
	}
	
	public void assertUpdated() {
		
		assertTrue(updated);
		assertFalse(updatedTwice);
		
		updated = false;
		
	}
	
}