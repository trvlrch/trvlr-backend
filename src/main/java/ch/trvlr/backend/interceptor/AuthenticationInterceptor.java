package ch.trvlr.backend.interceptor;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PrivateChat;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * Hook into inbound messages before they reach the controller.
	 * <p>
	 * This hook is responsible to authenticate users and checks permissions
	 * for specific STOMP Commands.
	 *
	 * @param message Message<?>
	 * @param channel MessageChannel
	 * @return
	 */
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		String sessionId = accessor.getSessionId();

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			Traveler user = (Traveler) authenticateUser(message);
			if (user == null) {
				sendErrorMessage("Connection denied: invalid login", sessionId);
			}
			accessor.setUser(user);
		}

		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			System.out.println("---- Destination -----");
			System.out.println(accessor.getDestination());
			System.out.println(accessor.getUser());
			Traveler user = (Traveler) accessor.getUser();
			if (!validateChatRoomAccess(user, accessor.getDestination())) {
				sendErrorMessage("Subscription denied: insufficient permissions", sessionId);
			}
		}

		return message;
	}

	/**
	 * Authenticate user before he is allowed to connect to
	 * the socket.
	 *
	 * @param message Message<?>
	 * @return Principal
	 */
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
		System.out.println(user.getUid());
		return user;
	}

	/**
	 * Check if user is allowed to susbscribe to the given destination.
	 * <p>
	 * Rules:
	 * - PublicChat: The room has to exist.
	 * - PrivateChat: The user has to be a participant of the chatRoom to join.
	 *
	 * @param user        Traveler
	 * @param destination String
	 * @return
	 */
	private Boolean validateChatRoomAccess(Traveler user, String destination) {
		int roomId = parseRoomId(".*/chat/(\\d+)", destination);
		ChatRoomRepository repository = ChatRoomRepository.getInstance();
		if (roomId > 0) {
			// check if room exists
			ChatRoom room = repository.getById(roomId);
			if (room == null) {
				return false;
			}

			// if public room add user
			if (room instanceof PublicChat) {
				room.addTraveler(user);
				repository.save(room);
			}

			// if private room check user permissions
			if (room instanceof PrivateChat) {
				Traveler traveler = room.getTraveler(user.getId());
				if (traveler == null) {
					return false;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}

	private int parseRoomId(String pattern, String target) {
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(target);

		if (m.find()) {
			return Integer.parseInt(m.group(1));
		}

		return 0;
	}

	private String parseToken(MessageHeaders headers) {
		Object headersObj = headers.get("nativeHeaders");
		ObjectMapper m = new ObjectMapper();
		Map<String, LinkedList<String>> props = m.convertValue(headersObj, Map.class);
		LinkedList<String> tokenArray = props.get("token");
		return tokenArray.get(0);
	}

	private void sendErrorMessage(String message, String sessionId) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
		headerAccessor.setMessage(message);
		headerAccessor.setSessionId(sessionId);
		Message<byte[]> error = MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders());
		this.clientOutboundChannel.send(error);
	}

}
