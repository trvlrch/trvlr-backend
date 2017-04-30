package ch.trvlr.backend.interceptor;

import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;
import java.util.LinkedList;
import java.util.Map;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class AuthenticationInterceptor extends ChannelInterceptorAdapter {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			Principal user = authenticateUser(message);
			accessor.setUser(user);
		}

		return message;
	}

	private Principal authenticateUser(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		String token = this.parseToken(headers);

		UserService service = new UserService();
		if (service.validateUser(token)) {
			System.out.println("valid token");

			//User user = service.getUserByToken();
			//String id = SimpMessageHeaderAccessor.getSessionId(headers);

			return new Traveler("Hans", "Dampf", "yo@yo.yo"); // access authentication header(s)
		}

		System.out.println("invalid token");
		return null;
	}
	// this.messagingTemplate.convertAndSend("/topic/friends/signin", Arrays.asList(user.getName()));

	private String parseToken(MessageHeaders headers) {
		Object headersObj = headers.get("nativeHeaders");
		ObjectMapper m = new ObjectMapper();
		Map<String, LinkedList<String>> props = m.convertValue(headersObj, Map.class);
		LinkedList<String> tokenArray = props.get("token");
		return tokenArray.get(0);
	}

}
