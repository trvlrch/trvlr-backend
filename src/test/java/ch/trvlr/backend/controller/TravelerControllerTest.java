package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.TravelerRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class TravelerControllerTest {
	@Test
	public void getUserDataByFirebaseId() {
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		Traveler traveler = new Traveler(1, " ", " ", " ", "a");
		when(mockedTravelerRepo.getByFirebaseId(anyString())).thenReturn(traveler);

		TravelerController controller = new TravelerController(mockedTravelerRepo);
		Traveler result = controller.getUserDataByFirebaseId("a");

		assertNotNull(result);
		assertEquals(result, traveler);

	}

}