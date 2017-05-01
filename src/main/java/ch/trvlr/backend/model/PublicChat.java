package ch.trvlr.backend.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

    private String from;
    private String to;
    private List<String> stops;

    public PublicChat(int id, String from, String to, Date createdOn) {
        super(id, createdOn);
        this.from = from;
        this.to = to;
    }

    public PublicChat(List<String> stops) {
        this.stops = stops;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }


    public void addStop(String stop) {
        this.stops.add(stop);
    }

    public List<String> getStops() {
        return Collections.unmodifiableList(this.stops);
    }
}
