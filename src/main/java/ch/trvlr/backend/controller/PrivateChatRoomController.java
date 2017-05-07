package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PrivateChat;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

	@ApiOperation(value = "Create private chat",
			notes = "The endpoint expects a json request body a list containing the travelerIds to add e.g. [{\"travelerId\": 1}, {\"travelerId\": 2}] ")
	@RequestMapping(path = "/api/private-chats", method = RequestMethod.POST, consumes = "application/json")
	public ChatRoom createPrivateChat(@RequestBody String postPayload) {
		JSONObject json = new JSONObject(postPayload);
		JSONArray ids = json.getJSONArray("travelerIds");

		if (ids.length() > 0) {
			PrivateChat room = new PrivateChat();
			for (int i = 0; i < ids.length(); i++) {
				Traveler traveler = TravelerRepository.getInstance().getById(ids.getInt(i));
				room.addTraveler(traveler);
			}

			int id = repository.save(room);

			if (id > 0) {
				room.setId(id);
				return room;
			}
		}
		return null;
	}

	@RequestMapping(path = "/api/private-chats/{roomId}", method = RequestMethod.GET)
	public ChatRoom getPrivateChat(@PathVariable int roomId) {
		// TODO authentication - only members of the chat should be allowed fetch this info
		return repository.getById(roomId);
	}

	@RequestMapping(path = "/api/private-chat/{roomId}/travelers", method = RequestMethod.GET)
	public List<Traveler> getAllTravelersForPrivateChat(@PathVariable int roomId) {
		// TODO authentication - only members of the chat should be allowed fetch this info
		ChatRoom chatRoom = repository.getById(roomId);
		return chatRoom.getAllTravelers();
	}

	@ApiOperation(value = "Join private chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/private-chats/{roomId}/join", method = RequestMethod.POST, consumes = "application/json")
	public ChatRoom createPrivateChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PrivateChat room = (PrivateChat) repository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = TravelerRepository.getInstance().getById(json.getInt("travelerId"));

		room.addTraveler(traveler);
		if (repository.save(room) > 0) {
			return room;
		}
		return null;
	}

	@ApiOperation(value = "Leave private chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/private-chats/{roomId}/leave", method = RequestMethod.POST, consumes = "application/json")
	public Boolean leavePrivateChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PrivateChat room = (PrivateChat) repository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = TravelerRepository.getInstance().getById(json.getInt("travelerId"));

		room.removeTraveler(traveler);
		return (repository.save(room) > 0);
	}

	@RequestMapping(path = "/api/private-chats/list/{travelerId}", method = RequestMethod.GET)
	public List<ChatRoom> getPrivateChatsByTraveler(@PathVariable int travelerId) {
		ArrayList<ChatRoom> rooms = repository.getByTravelerId(travelerId);
		rooms.removeIf(c -> !c.isPrivate());
		return rooms;
	}
}
