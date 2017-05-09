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
	private int weight;

	public Station(int id, String name, int weight) {
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	public Station(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Station(String name) {
		this.name = name;
	}

	public Station() {
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

	public int getWeight() {
		return this.weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String toString() {
		return getName();
	}

}
