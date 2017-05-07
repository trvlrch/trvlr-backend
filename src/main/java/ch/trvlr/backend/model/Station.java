package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.ISqlObject;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Station implements ISqlObject {

	private int id;
	private String name;

	public Station() {

	}

	public Station(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Station(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

}
