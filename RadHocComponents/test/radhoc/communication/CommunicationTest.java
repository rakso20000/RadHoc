package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.SharkTestPeerFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameType;

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
	
	private void encounter(int n) throws SharkException, IOException, InterruptedException {
		
		alicePeer.getASAPTestPeerFS().startEncounter(4000 + n * 2, berndPeer.getASAPTestPeerFS());
		Thread.sleep(250);
		alicePeer.getASAPTestPeerFS().stopEncounter(berndPeer.getASAPTestPeerFS());
		
		berndPeer.getASAPTestPeerFS().startEncounter(4001 + n * 2, claraPeer.getASAPTestPeerFS());
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
		
		mockListenerC.assertNotCalled();
		
		communicationA.sendMove(3, 13, moveData);
		
		encounter(0);
		
		mockListenerA.assertNotCalled();
		mockListenerB.assertNotCalled();
		mockListenerC.assertCalled(13, moveData);
		
	}
	
	@Test
	void sendGlobalInvite() throws SharkException, IOException, InterruptedException {
		
		MockInviteListener mockListenerA = new MockInviteListener();
		MockInviteListener mockListenerB = new MockInviteListener();
		MockInviteListener mockListenerC = new MockInviteListener();
		
		communicationA.setInviteListener(mockListenerA);
		communicationB.setInviteListener(mockListenerB);
		communicationC.setInviteListener(mockListenerC);
		
		mockListenerB.assertNotInvited();
		mockListenerC.assertNotInvited();
		
		communicationA.sendInvite(GameType.TIC_TAC_TOE);
		
		encounter(1);
		
		mockListenerA.assertNotInvited();
		mockListenerB.assertInvited("Alice", 1, GameType.TIC_TAC_TOE);
		mockListenerC.assertInvited("Alice", 1, GameType.TIC_TAC_TOE);
		mockListenerA.assertNotAccepted();
		mockListenerB.assertNotAccepted();
		mockListenerC.assertNotAccepted();
		
	}
	
	@Test
	void sendInvite() throws SharkException, IOException, InterruptedException {
		
		MockInviteListener mockListenerA = new MockInviteListener();
		MockInviteListener mockListenerB = new MockInviteListener();
		MockInviteListener mockListenerC = new MockInviteListener();
		
		communicationA.setInviteListener(mockListenerA);
		communicationB.setInviteListener(mockListenerB);
		communicationC.setInviteListener(mockListenerC);
		
		mockListenerC.assertNotInvited();
		
		communicationB.sendInvite("clARA", GameType.TIC_TAC_TOE);
		
		encounter(2);
		
		mockListenerA.assertNotInvited();
		mockListenerB.assertNotInvited();
		mockListenerC.assertInvited("Bernd", 2, GameType.TIC_TAC_TOE);
		mockListenerA.assertNotAccepted();
		mockListenerB.assertNotAccepted();
		mockListenerC.assertNotAccepted();
		
	}
	
}