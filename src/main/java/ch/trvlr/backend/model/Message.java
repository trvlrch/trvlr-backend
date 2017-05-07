package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.ISqlObject;

import java.io.Serializable;
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

	public Message() {
		this.timestamp = new Date();
	}

	public Message(int id, Traveler author, String text, Date timestamp) {
		this.id = id;
		this.author = author;
		this.text = text;
		this.timestamp = timestamp;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
	    this.id = id;
    }

	public String getAuthor() {
		// return only name of the author
		// otherwise each client will receive all user details
		return this.author.getName();
	}

	public int getAuthorId() {
	    return this.author.getId();
    }

    public int getChatRoomId() {
	    return this.chatRoomId;
    }

	public void setChatRoomId(int chatRoomId) {
		this.chatRoomId = chatRoomId;
	}

	public void setAuthor(Traveler author) {
		this.author = author;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}
}
