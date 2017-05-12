package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.*;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.StationRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class PublicChatRoomControllerTest {

	private PublicChat immutableMock;

	@Before
	public void setUp() throws Exception {
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message());

		immutableMock = new PublicChat(new Station("test"), new Station("test2"));
		immutableMock.addTraveler(new Traveler());
		immutableMock.addTraveler(new Traveler());
	}
	@Test
	public void getAllPublicChats() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> rooms = new ArrayList<>();
		rooms.add(immutableMock);
		rooms.add(new PrivateChat());

		when(mockedRepo.getAll(anyInt())).thenReturn(rooms);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<ChatRoom> allRooms = chat.getAllPublicChats("", 0);

		assertNotNull(allRooms);
		assertEquals(allRooms.size(), 1);
	}

	@Test
	public void getPublicChat() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		when(mockedRepo.getById(anyInt())).thenReturn(immutableMock);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		ChatRoom publicChat = chat.getPublicChat(1);
		assertNotNull(publicChat);
		assertEquals(publicChat, immutableMock);
	}

	@Test
	public void createPublicChat() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(new ArrayList<>());
		when(mockedRepo.save(anyObject())).thenReturn(1);
		when(mockedStationRepo.getByName(anyString())).thenReturn(new Station("foo"));

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		String payload = "{\"from\": \"Zurich\", \"to\": \"Hoeri\"}";
		ChatRoom publicChat = chat.createPublicChat(payload);
		assertNotNull(publicChat);
		assertThat(publicChat, instanceOf(PublicChat.class));
		assertNotEquals(publicChat, immutableMock);

	}

	@Test
	public void createPublicChatWithExisting() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> rooms = new ArrayList<>();
		rooms.add(immutableMock);
		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(rooms);
		when(mockedRepo.save(anyObject())).thenReturn(1);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		String payload = "{\"from\": \"Zurich\", \"to\": \"Hoeri\"}";
		ChatRoom publicChat = chat.createPublicChat(payload);
		assertNotNull(publicChat);
		assertThat(publicChat, instanceOf(PublicChat.class));
		assertEquals(publicChat, immutableMock);
	}

	@Test(expected=org.json.JSONException.class)
	public void createPublicChatChatWithInvalidJson() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		String payload = "errr";
		chat.createPublicChat(payload);
	}

	@Test
	public void getAllTravelersForPublicChat() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<Traveler> travelers = new ArrayList<>();
		travelers.add(new Traveler());
		travelers.add(new Traveler());
		when(mockedTravelerRepo.getAllTravelersForChat(anyInt())).thenReturn(travelers);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<Traveler> testTravelers = chat.getAllTravelersForPublicChat(1);
		assertNotNull(testTravelers);
		assertEquals(testTravelers.size(), 2);
	}

	@Test
	public void leavePublicChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		Traveler traveler = new Traveler(1, "", "", "", "");
		PublicChat mock = new PublicChat(new Station("test"), new Station("test2"));

		mock.addTraveler(traveler);
		when(mockedTravelerRepo.getById(anyInt())).thenReturn(traveler);
		when(mockedRepo.getById(anyInt())).thenReturn(mock);
		when(mockedRepo.save(anyObject())).thenReturn(1);

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		String postPayload = "{\"travelerId\": 1}";
		assertTrue(publicChat.leavePublicChat(1, postPayload));
	}

	@Test(expected=org.json.JSONException.class)
	public void leavePublicChatWithInvalidJson() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		String postPayload = "errr";
		publicChat.leavePublicChat(1, postPayload);
	}

	@Test
	public void findChatRoomsForConnection() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> chatRooms = new ArrayList<>();
		chatRooms.add(immutableMock);
		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(chatRooms);

		PublicChatRoomController chat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<ChatRoom> result = chat.findChatRoomsForConnection("foo", "bar");
		assertNotNull(result);
		assertEquals(result.size(), 1);
	}

	@Test
	public void getPublicChatsByTraveler() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> chatRooms = new ArrayList<>();
		chatRooms.add(immutableMock);
		chatRooms.add(new PrivateChat());
		when(mockedRepo.getByTravelerId(anyInt())).thenReturn(chatRooms);

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		// this should only return private chats
		List<ChatRoom> rooms = publicChat.getPublicChatsByTraveler(1);

		assertNotNull(rooms);
		assertEquals(rooms.size(), 1);
		assertEquals(rooms.get(0), immutableMock);
	}

	@Test
	public void joinChatRoomsForConnection() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> chatRooms = new ArrayList<>();
		chatRooms.add(immutableMock);
		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(chatRooms);

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<ChatRoom> rooms = publicChat.joinChatRoomsForConnection("foo", "bar");

		assertNotNull(rooms);
		assertEquals(rooms.size(), 1);
		assertEquals(rooms.get(0), immutableMock);
	}

	@Test
	public void joinChatRoomsForConnectionWithoutExistingRooms() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(null);
		when(mockedRepo.save(anyObject())).thenReturn(1);
		when(mockedStationRepo.getByName(anyString())).thenReturn(new Station("foo"), new Station("bar"));

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<ChatRoom> rooms = publicChat.joinChatRoomsForConnection("foo", "bar");

		assertNotNull(rooms);
		assertEquals(rooms.size(), 1);
	}

	@Test
	public void joinChatRoomsForConnectionWithInvalidStations() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);
		StationRepository mockedStationRepo = mock(StationRepository.class);

		ArrayList<ChatRoom> chatRooms = new ArrayList<>();
		chatRooms.add(immutableMock);
		when(mockedRepo.findChatRoomsForConnection(anyString(), anyString())).thenReturn(null);
		when(mockedRepo.save(anyObject())).thenReturn(1);
		when(mockedStationRepo.getByName(anyString())).thenReturn(null);

		PublicChatRoomController publicChat = new PublicChatRoomController(mockedRepo, mockedTravelerRepo, mockedStationRepo);

		List<ChatRoom> rooms = publicChat.joinChatRoomsForConnection("foo", "bar");

		assertNotNull(rooms);
		assertEquals(rooms.size(), 0);
	}


}