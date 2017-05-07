package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation(value = "Create private chat",
			notes = "The endpoint expects a json request body a list containing the from and to station names e.g. {\"from\": \"Zurich\", \"to\": \"Hoeri\"} ")
	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST, consumes = "application/json")
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

	@RequestMapping(path = "/api/public-chats/{roomId}", method = RequestMethod.GET)
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		return repository.getById(roomId);
	}

	@RequestMapping(path = "/api/public-chats/{roomId}/travelers", method = RequestMethod.GET)
	public List<Traveler> getAllTravelersForPublicChat(@PathVariable int roomId) {
		TravelerRepository travelerRepository = TravelerRepository.getInstance();
		return travelerRepository.getAllTravelersForChat(roomId);
	}

	@ApiOperation(value = "Leave public chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/public-chats/{roomId}/leave", method = RequestMethod.POST, consumes = "application/json")
	public Boolean leavePublicChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PublicChat room = (PublicChat) repository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = TravelerRepository.getInstance().getById(json.getInt("travelerId"));
		room.removeTraveler(traveler);
		return (repository.save(room) > 0);
	}

	@RequestMapping(path = "/api/public-chats/search", method = RequestMethod.GET)
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return repository.findChatRoomsForConnection(from, to);
	}

	@RequestMapping(path = "/api/public-chats/list/{travelerId}", method = RequestMethod.GET)
	public List<ChatRoom> getPublicChatsByTraveler(@PathVariable int travelerId) {
		ArrayList<ChatRoom> rooms = repository.getByTravelerId(travelerId);
		rooms.removeIf(ChatRoom::isPrivate);
		return rooms;
	}

}
