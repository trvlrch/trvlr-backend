package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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

	@RequestMapping("/api/public-chats")
	public List<ChatRoom> getAllPublicChats() {
		return repository.getAll();
	}

	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST)
	public ChatRoom createPublicChat(@RequestBody String from, @RequestBody String to) {
		// TODO from/to validation
		ChatRoom room = new PublicChat(new Station(from), new Station(to));
		if (repository.save(room) > 0) {
			return room;
		}
		return null;
	}

	@RequestMapping("/api/public-chats/search")
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return repository.findChatRoomsForConnection(from, to);
	}

	@RequestMapping("/api/public-chats/{roomId}")
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		return repository.getById(roomId);
	}

	@RequestMapping("/api/public-chats/list/{travelerId}")
	public List<ChatRoom> getPublicChatsByTraveler(@PathVariable int travelerId) {
		return repository.getByTravelerId(travelerId);
	}

	@RequestMapping("/api/public-chats/{roomId}/travelers")
	public List<Traveler> getAllTravelersForPublicChat(@PathVariable int roomId) {
		TravelerRepository travelerRepository = TravelerRepository.getInstance();
		return travelerRepository.getAllTravelersForChat(roomId);
	}
}
