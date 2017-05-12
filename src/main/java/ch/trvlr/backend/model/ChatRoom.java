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

	public Message getMessage(int id) {
		return this.messages.get(id);
	}

	public List<Message> getAllMessages() {
		return Collections.unmodifiableList(this.messages);
	}

	public void addMessage(Message message) {
		this.messages.add(message);
	}

	public Traveler getTraveler(int id) {
		return this.travelers.get(id);
	}

	public List<Traveler> getAllTravelers() {
		return Collections.unmodifiableList(this.travelers);
	}

	public void addTraveler(Traveler traveler) {
		if (!this.travelers.contains(traveler)) {
			this.travelers.add(traveler);
		}
	}

	public void removeTraveler(Traveler traveler) {
		this.travelers.removeIf(current -> current.getId() == traveler.getId());
	}

	public boolean isPrivate() {
		return this.from == null && this.to == null;
	}

}
