package ch.trvlr.backend.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

    private List<String> stops;

    public List<String> getStops() {
        return Collections.unmodifiableList(this.stops);
    }
}
