package ch.trvlr.backend.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

public class WebSocketConnectHandler<S>	implements ApplicationListener<SessionConnectEvent> {
	private SimpMessageSendingOperations messagingTemplate;

	public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
	}

	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		String token = headers.get("nativeHeaders").toString();
		//Principal user = SimpMessageHeaderAccessor.getUser(headers);

		// TODO validate and initiate user
		System.out.println(headers.toString());
		System.out.println(token);

		/*if (user == null) {
			return;
		}*/
		String id = SimpMessageHeaderAccessor.getSessionId(headers);

		// this.messagingTemplate.convertAndSend("/topic/friends/signin", Arrays.asList(user.getName()));
	}
}