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

	/**
	 * Constructor of Traveler
	 */
	public Traveler() {

	}

	/**
	 * Constructor of Traveler
	 *
	 * @param id        int
	 * @param firstName String
	 * @param lastname  String
	 * @param email     String
	 * @param uid       String
	 */
	public Traveler(int id, String firstName, String lastname, String email, String uid) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastname;
		this.email = email;
		this.uid = uid;
	}

	/**
	 * Constructor of Traveler
	 *
	 * @param firstName String
	 * @param lastname  String
	 * @param email     String
	 * @param uid       String
	 */
	public Traveler(String firstName, String lastname, String email, String uid) {
		this.firstName = firstName;
		this.lastName = lastname;
		this.email = email;
		this.uid = uid;
	}

	/**
	 * Get id
	 *
	 * @return int
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Set id
	 *
	 * @param id int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get firstname
	 *
	 * @return String
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set firstname
	 *
	 * @param firstName String
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get lastname
	 *
	 * @return String
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set lastname
	 *
	 * @param lastName String
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get email
	 *
	 * @return String
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set email
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get uid
	 *
	 * @return String
	 */
	public String getUid() {
		return this.uid;
	}

	/**
	 * Set uid
	 *
	 * @param uid String
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Get name
	 *
	 * @return String
	 */
	@Override
	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	/**
	 * To String
	 * <p>
	 * Returns the first and lastname
	 *
	 * @return String
	 */
	public String toString() {
		return getName();
	}

	/**
	 * Override equals method since IDs are unique
	 *
	 * @param t Object
	 * @return Boolean
	 */
	@Override
	public boolean equals(Object t) {
		if (t instanceof Traveler) {
			// id is unique
			return this.getId() == ((Traveler) t).getId();
		}
		return false;
	}

}
