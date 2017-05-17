package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.TravelerRepository;
import ch.trvlr.backend.service.UserService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class TravelerController {

	private static TravelerRepository repository;

	/**
	 * Constructor for TravelerController
	 */
	public TravelerController() {
		repository = TravelerRepository.getInstance();
	}

	/**
	 * Constructor for TravelerController
	 * <p>
	 * Supports dependency injection for repositories
	 */
	public TravelerController(TravelerRepository repo) {
		repository = repo;
	}

	/**
	 * Get user data by their firebase ID
	 *
	 * @param firebaseId String
	 * @return Traveler
	 */
	@RequestMapping(path = "/api/traveler/{firebaseId}", method = RequestMethod.GET)
	public Traveler getUserDataByFirebaseId(@PathVariable String firebaseId) {
		Traveler traveler = repository.getByFirebaseId(firebaseId);

		return traveler;
	}

	/**
	 * Auth user by firebase ID or token
	 *
	 * @return Traveler
	 */
	@RequestMapping(path = "/api/traveler/auth", method = RequestMethod.POST, consumes = "application/json")
	public Traveler authUser(@RequestBody String postPayload) {
		JSONObject json = new JSONObject(postPayload);
		String firebaseUid = json.getString("firebaseUid");
		String firebaseToken = json.getString("firebaseToken");

		Traveler traveler = repository.getByFirebaseId(firebaseToken);

		if (traveler == null && firebaseToken.length() > 0) {
			UserService service = new UserService();
			traveler = service.getUserByToken(firebaseToken);
		}

		return traveler;
	}
}
