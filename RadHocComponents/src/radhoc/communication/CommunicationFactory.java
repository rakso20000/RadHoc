package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import radhoc.communication.impl.CommunicationSharkFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class CommunicationFactory {
	
	/**
	 * Creates a Communication for the given user, using the given SharkPeer
	 * @param userID ID of this user
	 * @param username Name of this user
	 * @param sharkPeer SharkPeer used by the created Communication
	 * @return Future of Communication, Communication is ready once SharkComponent.onStart() is called following a call to SharkPeer.start()
	 * @throws SharkException If an error occurs in the ASAP/Shark library
	 */
	public static Future<Communication> createCommunication(long userID, String username, SharkPeer sharkPeer) throws SharkException {
		
		CompletableFuture<Communication> future = new CompletableFuture<>();
		sharkPeer.addComponent(new CommunicationSharkFactory(userID, username, future), Communication.class);
		
		return future;
		
	}
	
}