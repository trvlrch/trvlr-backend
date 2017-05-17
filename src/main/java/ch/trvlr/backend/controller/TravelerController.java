package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.TravelerRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
