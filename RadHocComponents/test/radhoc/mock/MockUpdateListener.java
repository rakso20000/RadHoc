package radhoc.mock;

import radhoc.gamestates.UpdateListener;

import static org.junit.jupiter.api.Assertions.*;

public class MockUpdateListener implements UpdateListener {
	
	private boolean updated;
	
	@Override
	public void onUpdate() {
		
		updated = true;
		
	}
	
	public void assertNotUpdated() {
		
		assertFalse(updated);
		
	}
	
	public void assertUpdated() {
		
		//For now, UpdateListener is allowed to be called twice
		
		assertTrue(updated);
		
		updated = false;
		
	}
	
}