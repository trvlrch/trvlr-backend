package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PrivateChat;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.springframework.web.bind.annotation.*;

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

	private static ChatRoomRepository repository;

	public ChatRoomController() {
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

	@RequestMapping("/api/public-chats/{roomId}")
	public ChatRoom getPublicChat(@PathVariable int roomId) {
        try {
            return repository.getById(roomId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    @RequestMapping("/api/private-chat/{roomId}/travelers")
    public List<Traveler> getAllTravelersForPrivateChat(@PathVariable int roomId) {
        try {
            ChatRoom chatRoom = repository.getById(roomId);
            return chatRoom.getAllTravelers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
