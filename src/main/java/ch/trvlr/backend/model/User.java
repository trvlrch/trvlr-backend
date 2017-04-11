package ch.trvlr.backend.model;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class User {

	private String id;
	private String name;

	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
