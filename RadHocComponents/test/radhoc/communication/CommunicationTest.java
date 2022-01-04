package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkTestPeerFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class CommunicationTest {
	
	private Path directory;
	
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
	void setUp() throws IOException, SharkException, ExecutionException, InterruptedException {
		
		directory = Files.createTempDirectory("radhoctests");
		
		alicePeer = new SharkTestPeerFS("alice", directory + File.separator + "alice");
		berndPeer = new SharkTestPeerFS("bernd", directory + File.separator + "bernd");
		claraPeer = new SharkTestPeerFS("clara", directory + File.separator + "clara");
		
		var communicationAFuture = CommunicationFactory.createCommunication(1, "Alice", alicePeer);
		var communicationBFuture = CommunicationFactory.createCommunication(2, "Bernd", berndPeer);
		var communicationCFuture = CommunicationFactory.createCommunication(3, "Clara", claraPeer);
		
		alicePeer.start();
		berndPeer.start();
		claraPeer.start();
		
		communicationA = communicationAFuture.get();
		communicationB = communicationBFuture.get();
		communicationC = communicationCFuture.get();
		
		moveListenerA = new MockMoveListener();
		moveListenerB = new MockMoveListener();
		moveListenerC = new MockMoveListener();
		
		inviteListenerA = new MockInviteListener();
		inviteListenerB = new MockInviteListener();
		inviteListenerC = new MockInviteListener();
		
	}
	
	private void setListeners() {
		
		communicationA.setMoveListener(moveListenerA);
		communicationB.setMoveListener(moveListenerB);
		communicationC.setMoveListener(moveListenerC);
		
		communicationA.setInviteListener(inviteListenerA);
		communicationB.setInviteListener(inviteListenerB);
		communicationC.setInviteListener(inviteListenerC);
		
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
	void uninitializedCommunication() throws SharkException {
		
		SharkPeer dieterPeer = new SharkTestPeerFS("dieter", directory + File.separator + "dieter");
		var communicationDFuture = CommunicationFactory.createCommunication(4, "Dieter", dieterPeer);
		
		assertFalse(communicationDFuture.isDone());
		
	}
	
	@Test
	void sendMove() throws SharkException, IOException, InterruptedException {
		
		setListeners();
		
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
		
		setListeners();
		
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
		
		setListeners();
		
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
		
		setListeners();
		
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
	
	@Test
	void setListenersAfterReceive() throws SharkException, IOException, InterruptedException {
		
		byte[] moveData = new byte[] {
			1, 2, 3, 5, 8, 13, 21, 34, 45, 79
		};
		
		communicationA.acceptInvite(3, 15, GameType.TIC_TAC_TOE);
		communicationB.sendInvite(GameType.TIC_TAC_TOE);
		communicationC.sendInvite("BERND", GameType.TIC_TAC_TOE); //TODO use different GameTypes
		communicationC.sendMove(1, 10, moveData);
		
		encounter(4);
		
		inviteListenerA.assertNotInvited();
		inviteListenerB.assertNotInvited();
		inviteListenerC.assertNotInvited();
		inviteListenerC.assertNotAccepted();
		moveListenerA.assertNotCalled();
		
		setListeners();
		
		inviteListenerA.assertInvited("Bernd", 2, GameType.TIC_TAC_TOE);
		inviteListenerB.assertInvited("Clara", 3, GameType.TIC_TAC_TOE);
		inviteListenerC.assertInvited("Bernd", 2, GameType.TIC_TAC_TOE);
		inviteListenerA.assertNotAccepted();
		inviteListenerB.assertNotAccepted();
		inviteListenerC.assertAccepted("Alice", 1, 15, GameType.TIC_TAC_TOE);
		moveListenerA.assertCalled(10, moveData);
		moveListenerB.assertNotCalled();
		moveListenerC.assertNotCalled();
		
	}
	
}