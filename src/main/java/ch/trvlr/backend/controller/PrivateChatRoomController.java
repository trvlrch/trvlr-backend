package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PrivateChat;
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
public class PrivateChatRoomController {

	private static ChatRoomRepository repository;

	public PrivateChatRoomController() {
		repository = ChatRoomRepository.getInstance();
	}

	@RequestMapping(path = "/api/private-chats", method = RequestMethod.POST)
	public ChatRoom createPrivateChat(@RequestBody int travelerId1, @RequestBody int travelerId2) {
		try {
			Traveler t1 = TravelerRepository.getInstance().getById(travelerId1);
			Traveler t2 = TravelerRepository.getInstance().getById(travelerId2);
			PrivateChat room = new PrivateChat(t1, t2);
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

	@RequestMapping("/api/private-chats/{roomId}")
	public ChatRoom getPrivateChat(@PathVariable int roomId) {
		try {
			// TODO authentication - only members of the chat should be allowed fetch this info
			return repository.getById(roomId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/api/private-chat/{roomId}/travelers")
	public List<Traveler> getAllTravelersForPrivateChat(@PathVariable int roomId) {
		try {
			// TODO authentication - only members of the chat should be allowed fetch this info
			ChatRoom chatRoom = repository.getById(roomId);
			return chatRoom.getAllTravelers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
