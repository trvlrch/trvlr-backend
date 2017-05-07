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

	public UserService() {
		api = new FirebaseService();
	}

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
		if (TravelerRepository.getInstance().save(user)) {
			return user;
		}
		return null;
	}

}