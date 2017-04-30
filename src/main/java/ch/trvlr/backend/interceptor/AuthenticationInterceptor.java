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
import org.springframework.messaging.support.MessageBuilder;
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
	private MessageChannel clientOutboundChannel;

	public AuthenticationInterceptor(MessageChannel clientOutboundChannel) {
		super();
		this.clientOutboundChannel = clientOutboundChannel;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		String sessionId = accessor.getSessionId();

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			Principal user = authenticateUser(message);

			if (user == null) {
				// send error message
				StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
				headerAccessor.setMessage("Connection denied: invalid login");
				headerAccessor.setSessionId(sessionId);
				Message<byte[]> error = MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders());
				this.clientOutboundChannel.send(error);
			}

			accessor.setUser(user);
		}

		return message;
	}

	private Principal authenticateUser(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		String token = this.parseToken(headers);

		UserService service = new UserService();
		Traveler user = service.getUserByToken(token);

		if (user.getUid() == null) {
			System.out.println("invalid token");
			return null;
		}

		System.out.println("valid token");
		return user;
	}

	private String parseToken(MessageHeaders headers) {
		Object headersObj = headers.get("nativeHeaders");
		ObjectMapper m = new ObjectMapper();
		Map<String, LinkedList<String>> props = m.convertValue(headersObj, Map.class);
		LinkedList<String> tokenArray = props.get("token");
		return tokenArray.get(0);
	}

}
