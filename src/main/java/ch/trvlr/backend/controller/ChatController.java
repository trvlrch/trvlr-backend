package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */

@Controller
public class ChatController {

	@MessageMapping("/chat/{roomId}")
	public Message handleMessage(@DestinationVariable("roomId") String roomId, @Payload Message message) throws Exception {
		System.out.println("Message received for room: " + roomId);
		return message;
	}
}
