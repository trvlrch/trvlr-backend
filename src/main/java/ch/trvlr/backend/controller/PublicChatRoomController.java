package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.ChatRoom;
import ch.trvlr.backend.model.PublicChat;
import ch.trvlr.backend.model.Station;
import ch.trvlr.backend.model.Traveler;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.StationRepository;
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

	private static ChatRoomRepository chatRoomRepository;
	private static TravelerRepository travelerRepository;
	private static StationRepository stationRepository;

	/**
	 * Constructor for PublicChatRoomController
	 */
	public PublicChatRoomController() {
		chatRoomRepository = ChatRoomRepository.getInstance();
		travelerRepository = TravelerRepository.getInstance();
		stationRepository = StationRepository.getInstance();
	}

	/**
	 * Constructor for PublicChatRoomController
	 * <p>
	 * Supports dependency injection for repositories
	 */
	public PublicChatRoomController(ChatRoomRepository chatRepo, TravelerRepository travelerRepo, StationRepository stationRepo) {
		chatRoomRepository = chatRepo;
		travelerRepository = travelerRepo;
		stationRepository = stationRepo;
	}

	/**
	 * Get all public chats
	 * <p>
	 * The resulting list can be limited to a certain number of items and can be ordered by popularity.
	 * The orderby parameter currently only supports "popularity"
	 *
	 * @param orderby String
	 * @param limit   int
	 * @return List<ChatRoom>
	 */
	@RequestMapping(path = "/api/public-chats", method = RequestMethod.GET)
	public List<ChatRoom> getAllPublicChats(@RequestParam(value = "orderby", required = false) String orderby, @RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
		List<ChatRoom> rooms;
		if (orderby != null && orderby.equals("popularity")) {
			rooms = chatRoomRepository.getMostPopular(limit);
		} else {
			rooms = chatRoomRepository.getAll(limit);
		}
		rooms.removeIf(ChatRoom::isPrivate);
		return rooms;
	}

	/**
	 * Create a public chat room
	 * <p>
	 * The method expects a POST request with a JSON payload.
	 * e.g. {"from": "Zurich", "to": "Hoeri"}
	 *
	 * @param postPayload String
	 * @return ChatRoom
	 */
	@ApiOperation(value = "Create private chat",
			notes = "The endpoint expects a json request body a list containing the from and to station names e.g. {\"from\": \"Zurich\", \"to\": \"Hoeri\"} ")
	@RequestMapping(path = "/api/public-chats", method = RequestMethod.POST, consumes = "application/json")
	public ChatRoom createPublicChat(@RequestBody String postPayload) {
		JSONObject json = new JSONObject(postPayload);
		String from = json.getString("from");
		String to = json.getString("to");

		ArrayList<ChatRoom> rooms = chatRoomRepository.findChatRoomsForConnection(from, to);
		if (rooms.size() > 0) {
			return rooms.get(0);
		} else {
			Station fromStation = stationRepository.getByName(from);
			Station toStation = stationRepository.getByName(to);

			fromStation = (fromStation == null) ? new Station(from) : fromStation;
			toStation = (toStation == null) ? new Station(to) : toStation;

			PublicChat chatRoom = new PublicChat(fromStation, toStation);
			chatRoomRepository.save(chatRoom);
			return chatRoom;
		}
	}

	/**
	 * Get a public chat room by id
	 *
	 * @param roomId int
	 * @return ChatRoom
	 */
	@RequestMapping(path = "/api/public-chats/{roomId}", method = RequestMethod.GET)
	public ChatRoom getPublicChat(@PathVariable int roomId) {
		return chatRoomRepository.getById(roomId);
	}

	/**
	 * Get all travelers for a public chat room
	 *
	 * @param roomId int
	 * @return List<Traveler>
	 */
	@RequestMapping(path = "/api/public-chats/{roomId}/travelers", method = RequestMethod.GET)
	public List<Traveler> getAllTravelersForPublicChat(@PathVariable int roomId) {
		return travelerRepository.getAllTravelersForChat(roomId);
	}

	/**
	 * Leave a public chat room
	 * <p>
	 * The method expects a POST request with a JSON payload.
	 * e.g. {"travelerId": 1}
	 *
	 * @param roomId      int
	 * @param postPayload String
	 * @return Boolean
	 */
	@ApiOperation(value = "Leave public chat",
			notes = "The endpoint expects a json request body containing the travelerId e.g. {\"travelerId\": 1} ")
	@RequestMapping(path = "/api/public-chats/{roomId}/leave", method = RequestMethod.POST, consumes = "application/json")
	public Boolean leavePublicChat(@PathVariable int roomId, @RequestBody String postPayload) {
		PublicChat room = (PublicChat) chatRoomRepository.getById(roomId);
		JSONObject json = new JSONObject(postPayload);
		Traveler traveler = travelerRepository.getById(json.getInt("travelerId"));
		room.removeTraveler(traveler);
		return (chatRoomRepository.save(room) > 0);
	}

	/**
	 * Find chat rooms for a connection (from, to)
	 *
	 * @param from String
	 * @param to   String
	 * @return List<ChatRoom>
	 */
	@RequestMapping(path = "/api/public-chats/search", method = RequestMethod.GET)
	public List<ChatRoom> findChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		return chatRoomRepository.findChatRoomsForConnection(from, to);
	}

	/**
	 * Join a public chat room for a connection (from, to)
	 * <p>
	 * If the connection does not exist yet, a new one will be created.
	 *
	 * @param from String
	 * @param to   String
	 * @return List<ChatRoom>
	 */
	@RequestMapping(path = "/api/public-chats/join", method = RequestMethod.GET)
	public List<ChatRoom> joinChatRoomsForConnection(@RequestParam(value = "from") String from, @RequestParam(value = "to") String to) {
		List<ChatRoom> rooms = chatRoomRepository.findChatRoomsForConnection(from, to);

		if (rooms == null || rooms.size() == 0) {
			rooms = new ArrayList<>();

			Station fromStation = stationRepository.getByName(from);
			Station toStation = stationRepository.getByName(to);

			// create new public chatroom if both stations are valid
			if (fromStation != null && toStation != null) {
				PublicChat room = new PublicChat(fromStation, toStation);
				int id = chatRoomRepository.save(room);
				if (id > 0) {
					room.setId(id);
					rooms.add(room);
				}
			}
		}
		return rooms;
	}

	/**
	 * Get all publich chat rooms of a traveler
	 *
	 * @param travelerId int
	 * @return List<ChatRoom>
	 */
	@RequestMapping(path = "/api/public-chats/list/{travelerId}", method = RequestMethod.GET)
	public List<ChatRoom> getPublicChatsByTraveler(@PathVariable int travelerId) {
		ArrayList<ChatRoom> rooms = chatRoomRepository.getByTravelerId(travelerId);
		rooms.removeIf(ChatRoom::isPrivate);
		return rooms;
	}

}
