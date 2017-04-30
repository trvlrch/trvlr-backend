package ch.trvlr.backend.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

    private List<String> stops;

    public PublicChat() {
    }

    public PublicChat(List<String>stops) {
        this.stops = stops;
    }

    public void addStop(String stop) {
        this.stops.add(stop);
    }

    public List<String> getStops() {
        return Collections.unmodifiableList(this.stops);
    }
}
