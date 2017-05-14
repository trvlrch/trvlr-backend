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

	/**
	 * Constructor of Station
	 *
	 * @param id     int
	 * @param name   String
	 * @param weight int
	 */
	public Station(int id, String name, int weight) {
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	/**
	 * Constructor of Station
	 *
	 * @param id   int
	 * @param name String
	 */
	public Station(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Constructor of Station
	 *
	 * @param name String
	 */
	public Station(String name) {
		this.name = name;
	}

	/**
	 * Constructor of Station
	 */
	public Station() {
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
	 * Get name
	 *
	 * @return String
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set name
	 *
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get weight
	 *
	 * @return int
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Set weight
	 *
	 * @param weight int
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * To string
	 * <p>
	 * Returns the name of the traveler
	 *
	 * @return String
	 */
	public String toString() {
		return getName();
	}

}
