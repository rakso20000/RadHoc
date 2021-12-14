package radhoc.communication;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import radhoc.communication.impl.CommunicationSharkFactory;

public class CommunicationFactory {
	
	public static Communication createCommunication(long userID, String username, SharkPeer sharkPeer) throws SharkException {
		
		sharkPeer.addComponent(new CommunicationSharkFactory(userID, username), Communication.class);
		
		return (Communication) sharkPeer.getComponent(Communication.class);
		
	}
	
}