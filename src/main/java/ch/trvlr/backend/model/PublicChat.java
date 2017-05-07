package ch.trvlr.backend.model;

import ch.trvlr.backend.repository.StationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lucac on 29.04.2017.
 */
public class PublicChat extends ChatRoom {

    public PublicChat(int id, Station from, Station to, Date createdOn, ArrayList<Traveler> travelers, ArrayList<Message> messages) {
        super(id, createdOn, travelers, messages);
        this.from = from;
        this.to = to;
    }

    public PublicChat(Station from, Station to) {
        super(0, new Date(), new ArrayList<>(), new ArrayList<>());
        this.from = from;
        this.to = to;
    }

    public PublicChat(String from, String to) {
        super(0, new Date(), new ArrayList<>(), new ArrayList<>());

        StationRepository stationRepository =  StationRepository.getInstance();

        Station fromStation = stationRepository.getByName(from);
		Station toStation = stationRepository.getByName(to);

		this.from = (fromStation == null) ? new Station(from) : fromStation;
        this.to = (toStation == null) ? new Station(to) : toStation;
    }

    public Station getFrom() {
        return this.from;
    }

    public Station getTo() {
        return this.to;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public void setTo(Station to) {
        this.to = to;
    }

}
