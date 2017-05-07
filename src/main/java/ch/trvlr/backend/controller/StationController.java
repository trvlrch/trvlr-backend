package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.repository.StationRepository;
import org.springframework.web.bind.annotation.RequestMapping;
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

	public StationController() {
		repository = StationRepository.getInstance();
	}

	@RequestMapping("/api/stations")
	public List<Station> getAllStations() {
		return repository.getAll();
	}
}
