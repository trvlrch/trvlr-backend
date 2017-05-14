package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.repository.StationRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class StationController {

	private static StationRepository repository;

	/**
	 * Constructor for StationController
	 */
	public StationController() {
		repository = StationRepository.getInstance();
	}

	/**
	 * Constructor for StationController
	 * <p>
	 * Supports dependency injection for repositories
	 */
	public StationController(StationRepository repo) {
		repository = repo;
	}

	/**
	 * Get all stations sorted by name and weight
	 *
	 * @return List<Station>
	 */
	@RequestMapping(path = "/api/stations", method = RequestMethod.GET)
	public List<Station> getAllStations() {
		return repository.getAll("substr(name, 1, 1) ASC, weight DESC, name ASC");
	}
}
