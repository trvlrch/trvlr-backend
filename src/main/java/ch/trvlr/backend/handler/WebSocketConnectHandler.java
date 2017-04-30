package ch.trvlr.backend.handler;

import ch.trvlr.backend.model.User;
import ch.trvlr.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.LinkedList;
import java.util.Map;

public class WebSocketConnectHandler<S>	implements ApplicationListener<SessionConnectEvent> {
	private SimpMessageSendingOperations messagingTemplate;

	public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
	}

	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		String token = this.parseToken(headers);

		System.out.println(headers.toString());
		System.out.println(token);

		UserService service = new UserService();
		if (service.validateUser(token)) {
			//User user = service.getUserByToken();
			String id = SimpMessageHeaderAccessor.getSessionId(headers);
		} else {
			System.out.println("Invalid token");
			return;
		}
		// this.messagingTemplate.convertAndSend("/topic/friends/signin", Arrays.asList(user.getName()));
	}

	private String parseToken(MessageHeaders headers) {
		Object headersObj = headers.get("nativeHeaders");
		ObjectMapper m = new ObjectMapper();
		Map<String,LinkedList<String>> props = m.convertValue(headersObj, Map.class);
		LinkedList<String> tokenArray = props.get("token");
		return tokenArray.get(0);
	}
}