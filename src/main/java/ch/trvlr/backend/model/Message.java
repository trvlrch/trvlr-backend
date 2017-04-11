package ch.trvlr.backend.model;

import java.util.Date;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class Message {

	private String name;
	private String text;
	private Date time;

	public Message() {
	}

	public Message(String name, String text) {
		this.name = name;
		this.text = text;
		this.time = new Date();

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTime() {
		return this.time;
	}
}
