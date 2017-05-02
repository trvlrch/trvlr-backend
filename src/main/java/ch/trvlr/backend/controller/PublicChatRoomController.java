package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
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
		List<ChatRoom> rooms = null;
		try {
			rooms = repository.getAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rooms;
	}

	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST)
	public ChatRoom createPublicChat(@RequestBody String from, @RequestBody String to) {
		// TODO from/to validation
		ChatRoom room = new PublicChat(from, to);
		try {
			if (repository.save(room)) {
				return room;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/api/public-chats/search")
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		List<ChatRoom> chatRooms = null;
		try {
			chatRooms = repository.findChatRoomsForConnection(from, to);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatRooms;
	}

	@RequestMapping("/api/public-chats/{roomId}")
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		try {
			return repository.getById(roomId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/api/public-chats/{roomId}/travelers")
	public List<Traveler> getAllTravelersForPublicChat(@PathVariable int roomId) {
		try {
			ChatRoom chatRoom = repository.getById(roomId);
			return chatRoom.getAllTravelers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
