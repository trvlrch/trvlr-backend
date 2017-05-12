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

import java.util.ArrayList;
import java.util.List;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
@RestController
public class PrivateChatRoomController {

	private static ChatRoomRepository chatRoomRepository;
	private static TravelerRepository travelerRepository;

	public PrivateChatRoomController() {
		chatRoomRepository = ChatRoomRepository.getInstance();
		travelerRepository = TravelerRepository.getInstance();
	}

	public PrivateChatRoomController(ChatRoomRepository chatRepo, TravelerRepository travelerRepo) {
		chatRoomRepository = chatRepo;
		travelerRepository = travelerRepo;
	}

	@ApiOperation(value = "Create private chat",
			notes = "The endpoint expects a json request body a list containing the travelerIds to add e.g. {\"travelerIds\": [1, 2]} ")
	@RequestMapping(path = "/api/private-chats", method = RequestMethod.POST, consumes = "application/json")
	public ChatRoom createPrivateChat(@RequestBody String postPayload) {
		JSONObject json = new JSONObject(postPayload);
		JSONArray ids = json.getJSONArray("travelerIds");

		if (ids.length() > 0) {
			PrivateChat room = new PrivateChat();
			for (int i = 0; i < ids.length(); i++) {
				Traveler traveler = travelerRepository.getById(ids.getInt(i));
				room.addTraveler(traveler);
			}

			int id = chatRoomRepository.save(room);

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
		return chatRoomRepository.getById(roomId);
	}

	@RequestMapping(path = "/api/private-chats/{uId1}/{uId2}", method = RequestMethod.GET)
	public ChatRoom getPrivateChat(@PathVariable int uId1, @PathVariable int uId2) {
		// TODO authentication - only members of the chat should be allowed fetch this info
		return chatRoomRepository.getPrivateChat(uId1, uId2);
	}

	@RequestMapping(path = "/api/private-chat/{roomId}/travelers", method = RequestMethod.GET)
	public List<Traveler> getAllTravelersForPrivateChat(@PathVariable int roomId) {
		// TODO authentication - only members of the chat should be allowed fetch this info
		ChatRoom chatRoom = chatRoomRepository.getById(roomId);
		return chatRoom.getAllTravelers();
	}

	@ApiOperation(value = "Join private chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/private-chats/{roomId}/join", method = RequestMethod.POST, consumes = "application/json")
	public ChatRoom joinPrivateChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PrivateChat room = (PrivateChat) chatRoomRepository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = travelerRepository.getById(json.getInt("travelerId"));

		room.addTraveler(traveler);
		if (chatRoomRepository.save(room) > 0) {
			return room;
		}
		return null;
	}

	@ApiOperation(value = "Leave private chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/private-chats/{roomId}/leave", method = RequestMethod.POST, consumes = "application/json")
	public Boolean leavePrivateChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PrivateChat room = (PrivateChat) chatRoomRepository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = travelerRepository.getById(json.getInt("travelerId"));

		room.removeTraveler(traveler);
		return (chatRoomRepository.save(room) > 0);
	}

	@RequestMapping(path = "/api/private-chats/list/{travelerId}", method = RequestMethod.GET)
	public List<ChatRoom> getPrivateChatsByTraveler(@PathVariable int travelerId) {
		ArrayList<ChatRoom> rooms = chatRoomRepository.getByTravelerId(travelerId);
		rooms.removeIf(c -> !c.isPrivate());
		return rooms;
	}
}
