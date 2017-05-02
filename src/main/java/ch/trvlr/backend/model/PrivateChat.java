package ch.trvlr.backend.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by lucac on 29.04.2017.
 */
public class PrivateChat extends ChatRoom {

    public PrivateChat(int id, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
            super(id, createdOn, travelers, messages);
    }

    public PrivateChat(Traveler t1, Traveler t2) {
        super(0, new Date(), (ArrayList<Traveler>)Arrays.asList(t1, t2), new ArrayList<>());
    }
}
