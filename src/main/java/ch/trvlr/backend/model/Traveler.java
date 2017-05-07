package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.ISqlObject;

import java.security.Principal;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Traveler implements Principal, ISqlObject {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String uid;

	public Traveler() {

	}

    public Traveler(int id, String firstName, String lastname, String email, String uid) {
	    this.id = id;
        this.firstName = firstName;
        this.lastName = lastname;
        this.email = email;
        this.uid = uid;
    }

	public Traveler(String firstName, String lastname, String email, String uid) {
		this.firstName = firstName;
		this.lastName = lastname;
		this.email = email;
		this.uid = uid;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
	    this.id = id;
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

	public String getUid() {
		return this.uid;
	}

	public void setUid(String authToken) {
		this.uid = authToken;
	}

	@Override
	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public String toString() {
		return getName();
	}

	@Override public boolean equals(Object t) {
		if (t instanceof Traveler) {
			// id is unique
			return this.getId() == ((Traveler) t).getId();
		}
		return false;
	}

}
