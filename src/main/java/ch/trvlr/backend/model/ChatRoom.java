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

	protected ChatRoom() {
	}

	protected ChatRoom(int id, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
		this.id = id;
		this.createdOn = createdOn;
		this.travelers = travelers;
		this.messages = messages;
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

}
