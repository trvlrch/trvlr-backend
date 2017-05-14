package ch.trvlr.backend.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lucac on 29.04.2017.
 */
public class PrivateChat extends ChatRoom {

	/**
	 * Constructor of PrivateChat
	 *
	 * @param id        int
	 * @param createdOn Date
	 * @param travelers ArrayList
	 * @param messages  ArrayList
	 */
	public PrivateChat(int id, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
		super(id, createdOn, travelers, messages);
	}

	/**
	 * Constructor of PrivateChat
	 *
	 * @param t1 Traveler
	 * @param t2 Traveler
	 */
	public PrivateChat(Traveler t1, Traveler t2) {
		super(0, new Date(), t1, t2, new ArrayList<>());
	}

	/**
	 * Constructor of PrivateChat
	 */
	public PrivateChat() {

	}
}
