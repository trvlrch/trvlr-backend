package ch.trvlr.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

    private String from;
    private String to;

    public PublicChat(int id, String from, String to, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
        super(id, createdOn, travelers, messages);
        this.from = from;
        this.to = to;
    }

    public PublicChat(String from, String to) {
        super(0, new Date(), new ArrayList<>(), new ArrayList<>());
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

}
