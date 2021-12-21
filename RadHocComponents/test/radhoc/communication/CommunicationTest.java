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
	
	private MockMoveListener moveListenerA;
	private MockMoveListener moveListenerB;
	private MockMoveListener moveListenerC;
	
	private MockInviteListener inviteListenerA;
	private MockInviteListener inviteListenerB;
	private MockInviteListener inviteListenerC;
	
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
		
		moveListenerA = new MockMoveListener();
		moveListenerB = new MockMoveListener();
		moveListenerC = new MockMoveListener();
		
		communicationA.setMoveListener(moveListenerA);
		communicationB.setMoveListener(moveListenerB);
		communicationC.setMoveListener(moveListenerC);
		
		inviteListenerA = new MockInviteListener();
		inviteListenerB = new MockInviteListener();
		inviteListenerC = new MockInviteListener();
		
		communicationA.setInviteListener(inviteListenerA);
		communicationB.setInviteListener(inviteListenerB);
		communicationC.setInviteListener(inviteListenerC);
		
		alicePeer.start();
		berndPeer.start();
		claraPeer.start();
		
	}
	
	private void encounter(int n) throws SharkException, IOException, InterruptedException {
		
		alicePeer.getASAPTestPeerFS().startEncounter(4000 + n * 3, berndPeer.getASAPTestPeerFS());
		Thread.sleep(200);
		alicePeer.getASAPTestPeerFS().stopEncounter(berndPeer.getASAPTestPeerFS());
		
		berndPeer.getASAPTestPeerFS().startEncounter(4001 + n * 3, claraPeer.getASAPTestPeerFS());
		Thread.sleep(200);
		berndPeer.getASAPTestPeerFS().stopEncounter(claraPeer.getASAPTestPeerFS());
		
		alicePeer.getASAPTestPeerFS().startEncounter(4002 + n * 3, berndPeer.getASAPTestPeerFS());
		Thread.sleep(200);
		alicePeer.getASAPTestPeerFS().stopEncounter(berndPeer.getASAPTestPeerFS());
		
	}
	
	@Test
	void sendMove() throws SharkException, IOException, InterruptedException {
		
		byte[] moveData = new byte[] {
			15, 17, 26, 19, 123, 13, -12, -36, 15
		};
		
		moveListenerC.assertNotCalled();
		
		communicationA.sendMove(3, 13, moveData);
		
		encounter(0);
		
		moveListenerA.assertNotCalled();
		moveListenerB.assertNotCalled();
		moveListenerC.assertCalled(13, moveData);
		
	}
	
	@Test
	void sendGlobalInvite() throws SharkException, IOException, InterruptedException {
		
		inviteListenerB.assertNotInvited();
		inviteListenerC.assertNotInvited();
		
		communicationA.sendInvite(GameType.TIC_TAC_TOE);
		
		encounter(1);
		
		inviteListenerA.assertNotInvited();
		inviteListenerB.assertInvited("Alice", 1, GameType.TIC_TAC_TOE);
		inviteListenerC.assertInvited("Alice", 1, GameType.TIC_TAC_TOE);
		inviteListenerA.assertNotAccepted();
		inviteListenerB.assertNotAccepted();
		inviteListenerC.assertNotAccepted();
		
	}
	
	@Test
	void sendInvite() throws SharkException, IOException, InterruptedException {
		
		inviteListenerC.assertNotInvited();
		
		communicationB.sendInvite("clARA", GameType.TIC_TAC_TOE);
		
		encounter(2);
		
		inviteListenerA.assertNotInvited();
		inviteListenerB.assertNotInvited();
		inviteListenerC.assertInvited("Bernd", 2, GameType.TIC_TAC_TOE);
		inviteListenerA.assertNotAccepted();
		inviteListenerB.assertNotAccepted();
		inviteListenerC.assertNotAccepted();
		
	}
	
	@Test
	void acceptInvite() throws SharkException, IOException, InterruptedException {
		
		inviteListenerA.assertNotAccepted();
		
		communicationC.acceptInvite(1, 16, GameType.TIC_TAC_TOE);
		
		encounter(3);
		
		inviteListenerA.assertNotInvited();
		inviteListenerB.assertNotInvited();
		inviteListenerC.assertNotInvited();
		inviteListenerA.assertAccepted("Clara", 3, 16, GameType.TIC_TAC_TOE);
		inviteListenerB.assertNotAccepted();
		inviteListenerC.assertNotAccepted();
		
	}
	
}