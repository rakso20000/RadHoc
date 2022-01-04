package radhoc.invitations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import radhoc.gamestates.GameState;
import radhoc.gamestates.GameStateManager;
import radhoc.gamestates.GameStateManagerFactory;
import radhoc.gamestates.GameType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvitationManagerTest {
	
	private GameStateManager gameStateManager;
	private MockCommunication mockCommunication;
	private InvitationManager invitationManager;
	
	@BeforeEach
	void setUp() throws IOException {
		
		File directory = Files.createTempDirectory("radhoctests").toFile();
		
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		mockCommunication = new MockCommunication();
		invitationManager = InvitationManagerFactory.createInvitationManager(gameStateManager, mockCommunication);
		
	}
	
	@Test
	void sendGlobalInvite() {
		
		mockCommunication.assertNotGlobalInvited();
		
		invitationManager.sendInvite(GameType.TIC_TAC_TOE);
		
		mockCommunication.assertGlobalInvited(GameType.TIC_TAC_TOE);
		
	}
	
	@Test
	void sendInvite() {
		
		mockCommunication.assertNotInvited();
		
		invitationManager.sendInvite("Petra", GameType.TIC_TAC_TOE);
		
		mockCommunication.assertInvited("Petra", GameType.TIC_TAC_TOE);
		
	}
	
	@Test
	void getEmptyInvitations() {
		
		List<Invitation> invitations = invitationManager.getInvitations();
		assertTrue(invitations.isEmpty());
		
	}
	
	@Test
	void receiveInvitation() {
		
		mockCommunication.mockReceiveInvite("Dieter", 4, GameType.TIC_TAC_TOE);
		
		List<Invitation> invitations = invitationManager.getInvitations();
		assertEquals(1, invitations.size());
		
		Invitation invitation = invitations.get(0);
		assertEquals("Dieter", invitation.getOpponentName());
		assertEquals(4, invitation.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitation.getGameType());
		
	}
	
	@Test
	void declineInvitation() {
		
		mockCommunication.mockReceiveInvite("Alice", 1, GameType.TIC_TAC_TOE);
		mockCommunication.mockReceiveInvite("Bernd", 2, GameType.TIC_TAC_TOE);
		mockCommunication.mockReceiveInvite("Clara", 3, GameType.TIC_TAC_TOE);
		
		Invitation invitationB = invitationManager.getInvitations().get(1);
		invitationB.decline();
		
		List<Invitation> invitations = invitationManager.getInvitations();
		assertEquals(2, invitations.size());
		
		Invitation invitationA = invitations.get(0);
		assertEquals("Alice", invitationA.getOpponentName());
		assertEquals(1, invitationA.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationA.getGameType());
		
		Invitation invitationC = invitations.get(1);
		assertEquals("Clara", invitationC.getOpponentName());
		assertEquals(3, invitationC.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationC.getGameType());
		
	}
	
	@Test
	void acceptInvitation() {
		
		mockCommunication.mockReceiveInvite("Clara", 3, GameType.TIC_TAC_TOE);
		
		Invitation invitation = invitationManager.getInvitations().get(0);
		
		mockCommunication.assertNotAccepted();
		
		invitation.accept();
		
		mockCommunication.assertAccepted(3, GameType.TIC_TAC_TOE);
		
		GameState gameState = gameStateManager.getGameState(mockCommunication.getAcceptedGameID());
		assertEquals("Clara", gameState.getOpponentName());
		assertEquals(3, gameState.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
		
	}
	
	@Test
	void inviteAccepted() {
		
		mockCommunication.mockInviteAccepted("Petra", 27, 14, GameType.TIC_TAC_TOE);
		
		GameState gameState = gameStateManager.getGameState(14);
		assertEquals("Petra", gameState.getOpponentName());
		assertEquals(27, gameState.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
		
	}
	
}