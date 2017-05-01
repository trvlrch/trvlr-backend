package ch.trvlr.backend.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lucac on 29.04.2017.
 */
public class PrivateChat extends ChatRoom {

    public PrivateChat(int id, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
            super(id, createdOn, travelers, messages);
    }
}
