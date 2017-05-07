package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.StationRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class PublicChatRoomController {

	private static ChatRoomRepository repository;

	public PublicChatRoomController() {
		repository = ChatRoomRepository.getInstance();
	}

	@RequestMapping(path = "/api/public-chats", method = RequestMethod.GET)
	public List<ChatRoom> getAllPublicChats() {
		return repository.getAll();
	}

	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST, consumes="application/json")
	public ChatRoom createPublicChat(@RequestBody String postPayload) {
		JSONObject json = new JSONObject(postPayload);
		String from = json.getString("from");
		String to = json.getString("to");

		ArrayList<ChatRoom> rooms = repository.findChatRoomsForConnection(from, to);
		if (rooms.size() > 0) {
			return rooms.get(0);
		} else {
			PublicChat chatRoom = new PublicChat(from, to);
			repository.save(chatRoom);
			return chatRoom;
		}
	}

	@RequestMapping(path = "/api/public-chats/search", method = RequestMethod.GET)
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return repository.findChatRoomsForConnection(from, to);
	}

	@RequestMapping(path = "/api/public-chats/{roomId}", method = RequestMethod.GET)
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		return repository.getById(roomId);
	}

	@RequestMapping(path = "/api/public-chats/list/{travelerId}", method = RequestMethod.GET)
	public List<ChatRoom> getPublicChatsByTraveler(@PathVariable int travelerId) {
		return repository.getByTravelerId(travelerId);
	}

	@RequestMapping(path = "/api/public-chats/{roomId}/travelers", method = RequestMethod.GET)
	public List<Traveler> getAllTravelersForPublicChat(@PathVariable int roomId) {
		TravelerRepository travelerRepository = TravelerRepository.getInstance();
		return travelerRepository.getAllTravelersForChat(roomId);
	}
}
