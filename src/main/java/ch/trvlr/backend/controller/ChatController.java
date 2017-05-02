package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Message;
import ch.trvlr.backend.model.Traveler;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@Controller
public class ChatController {

	/**
	 * Receive and send messages via websockets.
	 * <p>
	 * Note: public and private chats work the same way. The permission handling is
	 * done at the SUBSCRIBE event as part of the AuthenticationInterceptor.
	 *
	 * @param roomId   String
	 * @param message  Message
	 * @param traveler Traveler
	 * @return Message
	 * @throws Exception
	 */
	@MessageMapping("/chat/{roomId}")
	public Message handleMessages(@DestinationVariable("roomId") String roomId, @Payload Message message, Traveler traveler) throws Exception {
		System.out.println("Message received for room: " + roomId);
		System.out.println("User: " + traveler.toString());
		message.setAuthor(traveler);
		return message;
	}

}
