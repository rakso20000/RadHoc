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
	private SharkTestPeerFS claraPeer;
	
	private Communication communicationA;
	private Communication communicationB;
	private Communication communicationC;
	
	@BeforeEach
	void setUp() throws IOException, SharkException {
		
		Path directory;
		
		directory = Files.createTempDirectory("radhoctests");
		
		alicePeer = new SharkTestPeerFS("alice", directory + File.separator + "alice");
		berndPeer = new SharkTestPeerFS("bernd", directory + File.separator + "bernd");
		claraPeer = new SharkTestPeerFS("clara", directory + File.separator + "clara");
		
		communicationA = CommunicationFactory.createCommunication(1, "Alice", alicePeer);
		communicationB = CommunicationFactory.createCommunication(2, "Bernd", berndPeer);
		communicationC = CommunicationFactory.createCommunication(3, "Clara", claraPeer);
		
		alicePeer.start();
		berndPeer.start();
		claraPeer.start();
		
	}
	
	private void encounter() throws SharkException, IOException, InterruptedException {
		
		alicePeer.getASAPTestPeerFS().startEncounter(7777, berndPeer.getASAPTestPeerFS());
		Thread.sleep(250);
		alicePeer.getASAPTestPeerFS().stopEncounter(berndPeer.getASAPTestPeerFS());
		
		berndPeer.getASAPTestPeerFS().startEncounter(7778, claraPeer.getASAPTestPeerFS());
		Thread.sleep(250);
		berndPeer.getASAPTestPeerFS().stopEncounter(claraPeer.getASAPTestPeerFS());
		
	}
	
	@Test
	void sendMove() throws SharkException, IOException, InterruptedException {
		
		byte[] moveData = new byte[] {
			15, 17, 26, 19, 123, 13, -12, -36, 15
		};
		
		MockMoveListener mockListenerA = new MockMoveListener();
		MockMoveListener mockListenerB = new MockMoveListener();
		MockMoveListener mockListenerC = new MockMoveListener();
		
		communicationA.setMoveListener(mockListenerA);
		communicationB.setMoveListener(mockListenerB);
		communicationC.setMoveListener(mockListenerC);
		
		mockListenerA.assertNotCalled();
		mockListenerB.assertNotCalled();
		mockListenerC.assertNotCalled();
		
		communicationA.sendMove(3, 13, moveData);
		
		encounter();
		
		mockListenerA.assertNotCalled();
		mockListenerB.assertNotCalled();
		mockListenerC.assertCalled(13, moveData);
		
	}
	
}