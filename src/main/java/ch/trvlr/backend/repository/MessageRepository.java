package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Message;

import java.util.ArrayList;

public class MessageRepository extends Repository<Message> {

    private static MessageRepository instance = new MessageRepository();

    protected MessageRepository() {
        super("message", new String[] {
                "id", "author", "text", "timestamp", "chat_room_id"
        });
    }

    public static MessageRepository getInstance() {
        return MessageRepository.instance;
    }

    @Override
    protected Message getBusinessObject() {
        return null;
    }

    @Override
    public boolean add(Message o) {
        return false;
    }

    @Override
    public boolean update(Message o) {
        return false;
    }

    @Override
    public boolean save(Message o) {
        return false;
    }

    @Override
    public Message getById(int id) {
        return null;
    }

    @Override
    public ArrayList<Message> getAll() {
        return null;
    }
}
