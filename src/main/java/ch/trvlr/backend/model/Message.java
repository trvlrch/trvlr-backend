package ch.trvlr.backend.model;

import java.io.Serializable;
import java.util.Date;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Message {

	private int id;
	private Traveler author;
	private String text;
	private Date timestamp;

	public int getId() {
		return this.id;
	}

	public String getAuthor() {
		// return only name of the author
		// otherwise each client will receive all user details
		return this.author.getName();
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
