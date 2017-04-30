package ch.trvlr.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class ChatRoom {

	private int id;
	private Date createdOn;
	private ArrayList<Message> messages = new ArrayList<>();
	private ArrayList<Traveler> travelers = new ArrayList<>();

	public int getId() {
		return this.id;
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
		this.travelers.add(traveler);
	}
}
