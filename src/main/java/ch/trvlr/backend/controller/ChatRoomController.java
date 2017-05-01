package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class ChatRoomController {

	@RequestMapping("/api/public-chats")
	public List<ChatRoom> getAllPublicChats() {
		ChatRoom foo = new PublicChat(Arrays.asList("Bern", "Zurich", "Winterthur"));
		ChatRoom bar = new PublicChat(Arrays.asList("St Gallen", "Zurich", "Genf"));
		List<ChatRoom> rooms = Arrays.asList(foo, bar);

		return rooms;
	}

	@RequestMapping("/api/public-chats/{roomId}/travelers")
	public List<Traveler> getAllTravelersByRoom(@PathVariable int roomId) throws SQLException {
		ChatRoomRepository repository = ChatRoomRepository.getInstance();
		ChatRoom room = repository.getById(roomId);

		return room.getAllTravelers();
	}
}
