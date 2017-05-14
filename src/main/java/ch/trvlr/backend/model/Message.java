package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.ISqlObject;

import java.util.Date;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Message implements ISqlObject {

	private int id;
	private Traveler author;
	private String text;
	private Date timestamp;
	private int chatRoomId; // no need to reference the whole chat room object

	/**
	 * Constructor of Message
	 */
	public Message() {
		this.timestamp = new Date();
	}

	/**
	 * Constructor of Message
	 *
	 * @param id        int
	 * @param author    Traveler
	 * @param text		String
	 * @param timestamp	Date
	 */
	public Message(int id, Traveler author, String text, Date timestamp) {
		this.id = id;
		this.author = author;
		this.text = text;
		this.timestamp = timestamp;
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
	 * Get author
	 *
	 * @return Traveler
	 */
	public String getAuthor() {
		// return only name of the author
		// otherwise each client will receive all user details
		return this.author.getName();
	}

	/**
	 * Get author id
	 *
	 * @return int
	 */
	public int getAuthorId() {
		return this.author.getId();
	}

	/**
	 * Get chat room id
	 *
	 * @return int
	 */
	public int getChatRoomId() {
		return this.chatRoomId;
	}

	/**
	 * Set chat room id
	 *
	 * @param chatRoomId int
	 */
	public void setChatRoomId(int chatRoomId) {
		this.chatRoomId = chatRoomId;
	}

	/**
	 * Set author
	 *
	 * @param author Traveler
	 */
	public void setAuthor(Traveler author) {
		this.author = author;
	}

	/**
	 * Get text
	 *
	 * @return String
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Set text
	 *
	 * @param text String
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Get timestamp
	 *
	 * @return Date
	 */
	public Date getTimestamp() {
		return this.timestamp;
	}
}
