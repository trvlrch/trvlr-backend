package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.ISqlObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class ChatRoom implements ISqlObject {

	private int id;
	private Date createdOn;
	private ArrayList<Message> messages = new ArrayList<>();
	private ArrayList<Traveler> travelers = new ArrayList<>();

	protected Station from = null;
	protected Station to = null;

	protected ChatRoom() {
	}

	protected ChatRoom(int id, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
		this.id = id;
		this.createdOn = createdOn;
		this.travelers = travelers;
		this.messages = messages;
	}

	protected ChatRoom(int id, Date createdOn, Traveler t1, Traveler t2, ArrayList<Message> messages) {
		this.id = id;
		this.createdOn = createdOn;
		this.messages = messages;

		travelers.add(t1);
		travelers.add(t2);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	/**
	 * Get Message
	 *
	 * @param id int
	 * @return Message
	 */
	public Message getMessage(int id) {
		return this.messages.get(id);
	}

	/**
	 * Get all messages
	 *
	 * @return List<Message>
	 */
	public List<Message> getAllMessages() {
		return Collections.unmodifiableList(this.messages);
	}

	/**
	 * Add a message object
	 *
	 * @param message Message
	 */
	public void addMessage(Message message) {
		this.messages.add(message);
	}

	/**
	 * Get a traveler by index
	 *
	 * @param index int
	 * @return Traveler
	 */
	public Traveler getTraveler(int index) {
		return this.travelers.get(index);
	}

	/**
	 * Get a traveler by id
	 *
	 * @param id int
	 * @return Traveler
	 */
	public Traveler getTravelerById(int id) {
		for (Traveler traveler : this.travelers) {
			if (traveler.getId() == id) {
				return traveler;
			}
		}
		return null;
	}

	/**
	 * Get all travelers
	 *
	 * @return List<Traveler>
	 */
	public List<Traveler> getAllTravelers() {
		return Collections.unmodifiableList(this.travelers);
	}

	public void addTraveler(Traveler traveler) {
		if (!this.travelers.contains(traveler)) {
			this.travelers.add(traveler);
		}
	}

	/**
	 * Remove a traveler form the chat room
	 *
	 * @param traveler Traveler
	 */
	public void removeTraveler(Traveler traveler) {
		this.travelers.removeIf(current -> current.getId() == traveler.getId());
	}

	/**
	 * Check if chat room is private
	 *
	 * @return Boolean
	 */
	public boolean isPrivate() {
		return this.from == null && this.to == null;
	}

}
