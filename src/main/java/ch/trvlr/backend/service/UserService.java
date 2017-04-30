package ch.trvlr.backend.service;


import ch.trvlr.backend.model.Traveler;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author Rob Winch
 */
@Service
public class UserService {

	private static ApiService api;

	public UserService() {
		// api = new FirebaseService();
	}

	public Traveler loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO get user data from firebase
		return new Traveler();
	}

	public Boolean validateUser(String token) {
		return api.validateToken(token);
	}

}