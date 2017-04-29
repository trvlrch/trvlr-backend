package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.ChatRoom;

import java.util.ArrayList;

public class ChatRoomRepository extends Repository<ChatRoom> {

    private static ChatRoomRepository instance = new ChatRoomRepository();

    protected ChatRoomRepository() {
        super("chat_room", new String[] {
                "id", "from", "to", "created_on"
        });
    }

    public static ChatRoomRepository getInstance() {
        return ChatRoomRepository.instance;
    }

    @Override
    protected ChatRoom getBusinessObject() {
        return null;
    }

    @Override
    public boolean add(ChatRoom o) {
        return false;
    }

    @Override
    public boolean update(ChatRoom o) {
        return false;
    }

    @Override
    public boolean save(ChatRoom o) {
        return false;
    }

    @Override
    public ChatRoom getById(int id) {
        return null;
    }

    @Override
    public ArrayList<ChatRoom> getAll() {
        return null;
    }
}
