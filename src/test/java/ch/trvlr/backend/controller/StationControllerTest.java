package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.repository.StationRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class StationControllerTest {

	@Test
	public void getAllStations() throws Exception {
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<Station> stations = new ArrayList<>();
		stations.add(new Station("foo"));

		when(mockedStationRepo.getAll(anyString())).thenReturn(stations);

		StationController stationController = new StationController(mockedStationRepo);

		List<Station> allStations = stationController.getAllStations();

		assertNotNull(allStations);
		assertEquals(allStations.size(), 1);
	}

}