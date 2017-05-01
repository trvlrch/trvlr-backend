package ch.trvlr.backend.service;

import ch.trvlr.backend.model.Traveler;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public interface AuthenticationApiInterface {

	Traveler getUserByToken(String token);
}
