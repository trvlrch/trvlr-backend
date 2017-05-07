package ch.trvlr.backend.service;

import ch.trvlr.backend.model.Traveler;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public interface AuthenticationInterface {

	Traveler getUserByToken(String token);
}
