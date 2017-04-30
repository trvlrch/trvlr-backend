package ch.trvlr.backend.model;

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

    public Traveler getAuthor() {
        return this.author;
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
