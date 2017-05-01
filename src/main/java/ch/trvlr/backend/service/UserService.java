package ch.trvlr.backend.service;


import ch.trvlr.backend.model.Traveler;
import org.springframework.stereotype.Service;


/**
 * @author Rob Winch
 */
@Service
public class UserService {

	private static AuthenticationApiInterface api;

	public UserService() {
		api = new FirebaseService();
	}

	public Traveler getUserByToken(String token) {
		return api.getUserByToken(token);
	}

}