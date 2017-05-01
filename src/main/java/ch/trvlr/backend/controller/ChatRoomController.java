package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class ChatRoomController {

	private static ChatRoomRepository repository;

	public ChatRoomController() {
		repository = ChatRoomRepository.getInstance();
	}

	@RequestMapping("/api/public-chats")
	public List<ChatRoom> getAllPublicChats() {
		// TODO remove mocks
		ChatRoom foo = new PublicChat(Arrays.asList("Bern", "Zurich", "Winterthur"));
		ChatRoom bar = new PublicChat(Arrays.asList("St Gallen", "Zurich", "Genf"));
		List<ChatRoom> rooms = Arrays.asList(foo, bar);

		// List<ChatRoom> rooms = repository.getAll();

		return rooms;
	}

	@RequestMapping("/api/public-chats/search")
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		// TODO implement
		// return repository.findChatRoomsForConnection(from, to);
		return getAllPublicChats();
	}

	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST)
	public ChatRoom createPublicChat(@RequestBody String from, @RequestBody String to) {
		// TODO from/to validation
		ChatRoom room = new PublicChat(Arrays.asList(from, to));
		if (repository.save(room)) {
			return room;
		} else {
			return null;
		}
	}

	@RequestMapping("/api/public-chats/{roomId}")
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		return repository.getById(roomId);
	}

	@RequestMapping("/api/public-chats/{roomId}/travelers")
	public List<Traveler> getAllTravelersByRoom(@PathVariable int roomId) {
		ChatRoom room = repository.getById(roomId);
		return room.getAllTravelers();
	}
}
