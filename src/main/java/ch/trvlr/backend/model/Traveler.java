package ch.trvlr.backend.model;

import java.security.Principal;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Traveler implements Principal {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String authToken;

	public Traveler() {

	}

	public Traveler(String firstName, String lastname, String email) {
		this.firstName = firstName;
		this.lastName = lastname;
		this.email = email;
	}

	public int getId() {
		return this.id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAuthToken() {
		return this.authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public String toString() {
		return getName();
	}
}
