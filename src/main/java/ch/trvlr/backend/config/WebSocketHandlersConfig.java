package ch.trvlr.backend.config;

import ch.trvlr.backend.handler.WebSocketConnectHandler;
import ch.trvlr.backend.handler.WebSocketDisconnectHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;

/**
 * These handlers are separated from WebSocketConfig because they are specific to this
 * application and do not demonstrate a typical Spring Session setup.
 *
 * @author Rob Winch
 */
@Configuration
public class WebSocketHandlersConfig<S extends ExpiringSession> {

	@Bean
	public WebSocketConnectHandler<S> webSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketConnectHandler<S>(messagingTemplate);
	}

	@Bean
	public WebSocketDisconnectHandler<S> webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketDisconnectHandler<S>(messagingTemplate);
	}
}