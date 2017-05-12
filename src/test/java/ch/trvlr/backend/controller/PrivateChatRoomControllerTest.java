package ch.trvlr.backend.controller;

import ch.trvlr.backend.model.*;
import ch.trvlr.backend.repository.ChatRoomRepository;
import ch.trvlr.backend.repository.TravelerRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class PrivateChatRoomControllerTest {

	private PrivateChat mockOne;
	private PrivateChat mockTwo;

	private ArrayList<Traveler> travelers;

	@Before
	public void setUp() throws Exception {
		travelers = new ArrayList<>();
		travelers.add(new Traveler());
		travelers.add(new Traveler());

		ArrayList<Message> messages = new ArrayList<>();
		messages.add(new Message());

		mockOne = new PrivateChat(2, new Date(), travelers, messages);
		mockTwo = new PrivateChat();
	}

	@Test
	public void createPrivateChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedTravelerRepo.getById(anyInt())).thenReturn(
				new Traveler(1,"","","",""),
				new Traveler(2,"","","",""));
		when(mockedRepo.save(anyObject())).thenReturn(1);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "{\"travelerIds\": [1, 2]}";
		ChatRoom chat = privateChat.createPrivateChat(postPayload);

		assertNotNull(chat.getAllTravelers());
		assertEquals(2, chat.getAllTravelers().size());
	}

	@Test
	public void createPrivateChatWithInvalidIds() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedRepo.save(anyObject())).thenReturn(1);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "{\"travelerIds\": []}";
		ChatRoom chat = privateChat.createPrivateChat(postPayload);

		assertNull(chat);
	}

	@Test(expected=org.json.JSONException.class)
	public void createPrivateChatWithInvalidJson() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedRepo.save(anyObject())).thenReturn(1);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "error";
		ChatRoom chat = privateChat.createPrivateChat(postPayload);
	}

	@Test
	public void getPrivateChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedRepo.getById(anyInt())).thenReturn(mockOne);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		ChatRoom newChat = privateChat.getPrivateChat(2);
		assertEquals(newChat, mockOne);
	}

	@Test
	public void getOrCreatePrivateChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedRepo.getOrCreatePrivateChat(anyInt(), anyInt())).thenReturn(mockOne);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		ChatRoom newChat = privateChat.getOrCreatePrivateChat(1, 2);
		assertEquals(newChat, mockOne);
	}

	@Test
	public void getAllTravelersForPrivateChat() throws Exception {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		when(mockedRepo.getById(anyInt())).thenReturn(mockOne);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		List<Traveler> testTravelers = privateChat.getAllTravelersForPrivateChat(1);
		assertEquals(testTravelers, travelers);
	}

	@Test
	public void joinPrivateChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		PrivateChat mockChat = new PrivateChat();
		Traveler traveler = new Traveler(1, "", "", "", "");
		when(mockedTravelerRepo.getById(anyInt())).thenReturn(traveler);
		when(mockedRepo.getById(anyInt())).thenReturn(mockChat);
		when(mockedRepo.save(anyObject())).thenReturn(1);

		PrivateChatRoomController controller = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "{\"travelerId\": 1}";
		ChatRoom result = controller.joinPrivateChat(1, postPayload);

		assertNotNull(result);
		assertEquals(result.getAllTravelers().get(0), traveler);
	}

	@Test(expected=org.json.JSONException.class)
	public void joinPrivateChatWithInvalidJson() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		PrivateChatRoomController controller = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "errr";
		controller.joinPrivateChat(1, postPayload);
	}

	@Test
	public void leavePrivateChat() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		PrivateChat mockChat = new PrivateChat();
		Traveler traveler = new Traveler(1, "", "", "", "");
		mockChat.addTraveler(traveler);
		when(mockedTravelerRepo.getById(anyInt())).thenReturn(traveler);
		when(mockedRepo.getById(anyInt())).thenReturn(mockTwo);
		when(mockedRepo.save(anyObject())).thenReturn(1);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "{\"travelerId\": 1}";
		assertTrue(privateChat.leavePrivateChat(1, postPayload));
	}

	@Test(expected=org.json.JSONException.class)
	public void leavePrivateChatWithInvalidJson() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		String postPayload = "errr";
		privateChat.leavePrivateChat(1, postPayload);
	}

	@Test
	public void getPrivateChatsByTraveler() {
		ChatRoomRepository mockedRepo = mock(ChatRoomRepository.class);
		TravelerRepository mockedTravelerRepo = mock(TravelerRepository.class);

		ArrayList<ChatRoom> chatRooms = new ArrayList<>();
		chatRooms.add(mockOne);
		chatRooms.add(new PublicChat(new Station(), new Station()));
		when(mockedRepo.getByTravelerId(anyInt())).thenReturn(chatRooms);

		PrivateChatRoomController privateChat = new PrivateChatRoomController(mockedRepo, mockedTravelerRepo);

		// this should only return private chats
		List<ChatRoom> rooms = privateChat.getPrivateChatsByTraveler(1);

		assertNotNull(rooms);
		assertEquals(rooms.size(), 1);
		assertEquals(rooms.get(0), mockOne);
	}

}