package ch.trvlr.backend.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

	/**
	 * Constructor of PublicChat
	 *
	 * @param id        int
	 * @param from      Station
	 * @param to        Station
	 * @param createdOn Date
	 * @param travelers ArrayList
	 * @param messages  ArrayList
	 */
	public PublicChat(int id, Station from, Station to, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
		super(id, createdOn, travelers, messages);
		this.from = from;
		this.to = to;
	}

	/**
	 * Constructor of PublicChat
	 *
	 * @param from Station
	 * @param to   Station
	 */
	public PublicChat(Station from, Station to) {
		super(0, new Date(), new ArrayList<>(), new ArrayList<>());
		this.from = from;
		this.to = to;
	}

	/**
	 * Get from station
	 *
	 * @return Station
	 */
	public Station getFrom() {
		return this.from;
	}

	/**
	 * Get to station
	 *
	 * @return Station
	 */
	public Station getTo() {
		return this.to;
	}

	/**
	 * Set from station
	 *
	 * @param from Station
	 */
	public void setFrom(Station from) {
		this.from = from;
	}

	/**
	 * Set to station
	 *
	 * @param to Station
	 */
	public void setTo(Station to) {
		this.to = to;
	}

}
