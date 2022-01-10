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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvitationManagerTest {
	
	private File invitationsFile;
	private GameStateManager gameStateManager;
	private MockCommunication mockCommunication;
	private InvitationManager invitationManager;
	
	@BeforeEach
	void setUp() throws IOException {
		
		File directory = Files.createTempDirectory("radhoctests").toFile();
		invitationsFile = new File(directory, "invitations");
		
		gameStateManager = GameStateManagerFactory.createGameStateManager(directory);
		mockCommunication = new MockCommunication();
		invitationManager = InvitationManagerFactory.createInvitationManager(gameStateManager, mockCommunication, invitationsFile);
		
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
		
		mockCommunication.mockInviteAccepted("Petra", 27, 14, GameType.TIC_TAC_TOE, true);
		
		GameState gameState = gameStateManager.getGameState(14);
		assertEquals("Petra", gameState.getOpponentName());
		assertEquals(27, gameState.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
		assertTrue(gameState.isPlayable());
		
	}
	
	@Test
	void randomizedGameID() {
		
		final int NUMBER_INVITATIONS = 100000;
		
		for (int i = 0; i < NUMBER_INVITATIONS; ++i)
			mockCommunication.mockReceiveInvite(String.format("Person %d", i), i, GameType.TIC_TAC_TOE);
		
		List<Invitation> invitations = invitationManager.getInvitations();
		List<Long> gameIDs = new ArrayList<>(NUMBER_INVITATIONS);
		
		for (int i = 0; i < NUMBER_INVITATIONS; ++i) {
			
			Invitation invitation = invitations.get(i);
			invitation.accept();
			
			mockCommunication.assertAccepted(i, GameType.TIC_TAC_TOE);
			long gameID = mockCommunication.getAcceptedGameID();
			
			GameState gameState = gameStateManager.getGameState(gameID);
			assertEquals(String.format("Person %d", i), gameState.getOpponentName());
			assertEquals(i, gameState.getOpponentID());
			assertEquals(GameType.TIC_TAC_TOE, gameState.getGameType());
			
			gameIDs.add(gameID);
			
		}
		
		long distinctCount = gameIDs.stream()
			.distinct()
			.count();
		
		assertEquals(NUMBER_INVITATIONS, distinctCount);
		
	}
	
	@Test
	void randomizeStartingPlayer() {
		
		boolean playerStarted = false;
		boolean opponentStarted = false;
		
		for (int i = 0;; ++i) {
			
			mockCommunication.mockReceiveInvite(String.format("Person %d", i), i, GameType.TIC_TAC_TOE);
			
			Invitation invitation = invitationManager.getInvitations().get(0);
			invitation.accept();
			
			mockCommunication.assertAccepted(i, GameType.TIC_TAC_TOE);
			boolean recipientStarts = mockCommunication.isAcceptRecipientStarting();
			
			GameState gameState = gameStateManager.getGameState(mockCommunication.getAcceptedGameID());
			assertTrue(recipientStarts != gameState.isPlayable());
			
			//Make sure both players get to start at some point
			if (recipientStarts)
				opponentStarted = true;
			else
				playerStarted = true;
			
			if (playerStarted && opponentStarted)
				break;
			
		}
		
	}
	
	@Test
	void saveInvitations() {
		
		mockCommunication.mockReceiveInvite("Alice", 1, GameType.TIC_TAC_TOE);
		mockCommunication.mockReceiveInvite("Bernd", 2, GameType.TIC_TAC_TOE);
		mockCommunication.mockReceiveInvite("Clara", 3, GameType.TIC_TAC_TOE);
		
		invitationManager.save();
		invitationManager = InvitationManagerFactory.createInvitationManager(gameStateManager, mockCommunication, invitationsFile);
		
		List<Invitation> invitations = invitationManager.getInvitations();
		assertEquals(3, invitations.size());
		
		Invitation invitationA = invitations.get(0);
		assertEquals("Alice", invitationA.getOpponentName());
		assertEquals(1, invitationA.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationA.getGameType());
		
		Invitation invitationB = invitations.get(1);
		assertEquals("Bernd", invitationB.getOpponentName());
		assertEquals(2, invitationB.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationB.getGameType());
		
		Invitation invitationC = invitations.get(2);
		assertEquals("Clara", invitationC.getOpponentName());
		assertEquals(3, invitationC.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationC.getGameType());
		
		invitationA.accept();
		mockCommunication.mockReceiveInvite("Dieter", 4, GameType.TIC_TAC_TOE);
		invitationC.decline();
		
		invitationManager.save();
		invitationManager = InvitationManagerFactory.createInvitationManager(gameStateManager, mockCommunication, invitationsFile);
		
		invitations = invitationManager.getInvitations();
		assertEquals(2, invitations.size());
		
		invitationB = invitations.get(0);
		assertEquals("Bernd", invitationB.getOpponentName());
		assertEquals(2, invitationB.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationB.getGameType());
		
		Invitation invitationD = invitations.get(1);
		assertEquals("Dieter", invitationD.getOpponentName());
		assertEquals(4, invitationD.getOpponentID());
		assertEquals(GameType.TIC_TAC_TOE, invitationD.getGameType());
		
	}
	
}