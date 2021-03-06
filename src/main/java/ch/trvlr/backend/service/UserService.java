package ch.trvlr.backend.service;


import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.TravelerRepository;
import org.springframework.stereotype.Service;


/**
 * @author Rob Winch
 */
@Service
public class UserService {

	private static AuthenticationInterface api;

	/**
	 * Constructor for UserService
	 */
	public UserService() {
		api = new FirebaseService();
	}

	/**
	 * Get a user by their token
	 * <p>
	 * Validates the user with the available api and if the token is invalid
	 * the returned traveler object will be null.
	 *
	 * @param token String
	 * @return Traveler
	 */
	public Traveler getUserByToken(String token) {
		Traveler user = api.getUserByToken(token);
		if (user != null) {
			user = syncUserWithDB(user);
		}
		return user;
	}

	private Traveler syncUserWithDB(Traveler user) {
		TravelerRepository repository = TravelerRepository.getInstance();
		Traveler existing = repository.getByEmail(user.getEmail());
		// override existing user
		if (existing != null) {
			user.setId(existing.getId());
		}
		if (TravelerRepository.getInstance().save(user) > 0) {
			return user;
		}
		return null;
	}

}