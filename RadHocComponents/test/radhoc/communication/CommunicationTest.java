package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.SharkTestPeerFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommunicationTest {
	
	private SharkTestPeerFS alicePeer;
	private SharkTestPeerFS berndPeer;
	
	private Communication communicationAlice;
	private Communication communicationBernd;
	
	@BeforeEach
	void setUp() throws IOException, SharkException {
		
		Path directory;
		
		directory = Files.createTempDirectory("radhoctests");
		
		alicePeer = new SharkTestPeerFS("alice", directory + File.separator + "alice");
		berndPeer = new SharkTestPeerFS("bernd", directory + File.separator + "bernd");
		
		communicationAlice = CommunicationFactory.createCommunication(1, "Alice", alicePeer);
		communicationBernd = CommunicationFactory.createCommunication(2, "Bernd", berndPeer);
		
		alicePeer.start();
		berndPeer.start();
		
	}
	
	@Test
	void sendMove() throws SharkException, IOException {
		
		byte[] moveData = new byte[] {
				15, 17, 26, 19, 123, 13, -12, -36, 15
		};
		
		MockMoveListener mockListener = new MockMoveListener();
		
		communicationBernd.setMoveListener(mockListener);
		
		communicationAlice.sendMove(2, 13, moveData);
		
		alicePeer.getASAPTestPeerFS().startEncounter(7777, berndPeer.getASAPTestPeerFS());
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mockListener.assertCalled(13, moveData);
		
	}
	
}