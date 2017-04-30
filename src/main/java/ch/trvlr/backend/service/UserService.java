package ch.trvlr.backend.service;


import ch.trvlr.backend.model.Traveler;
import org.springframework.stereotype.Service;


/**
 * @author Rob Winch
 */
@Service
public class UserService {

	private static ApiService api;

	public UserService() {
		api = new FirebaseService();
	}

	public Traveler loadUserByUsername(String username) {
		// TODO get user data from firebase
		return new Traveler();
	}

	public Boolean validateUser(String token) {
		return api.validateToken(token);
	}

}