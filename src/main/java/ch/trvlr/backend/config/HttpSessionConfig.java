package ch.trvlr.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@EnableRedisHttpSession
public class HttpSessionConfig {

}
