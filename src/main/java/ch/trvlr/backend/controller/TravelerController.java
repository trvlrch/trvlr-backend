package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.StationRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.springframework.web.bind.annotation.PathVariable;
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
public class TravelerController {

	private static TravelerRepository repository;

	public TravelerController() {
		repository = TravelerRepository.getInstance();
	}

	@RequestMapping(path = "/api/traveler/{firebaseId}", method = RequestMethod.GET)
	public Traveler getUserDataByFirebaseId(@PathVariable String firebaseId) {
		return repository.getByFirebaseId(firebaseId);
	}
}
