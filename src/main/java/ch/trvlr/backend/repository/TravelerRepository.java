package ch.trvlr.backend.repository;

import ch.trvlr.backend.model.Traveler;

import java.util.ArrayList;

public class TravelerRepository extends Repository<Traveler> {

    private static TravelerRepository instance = new TravelerRepository();

    protected TravelerRepository() {
        super("Traveler", new String[] {
                    "id", "firstname", "lastname", "email", "auth_token"
                });
    }

    public static TravelerRepository getInstance() {
        return TravelerRepository.instance;
    }

    @Override
    protected Traveler getBusinessObject() {
        return null;
    }

    @Override
    public boolean add(Traveler o) {
        return false;
    }

    @Override
    public boolean update(Traveler o) {
        return false;
    }

    @Override
    public boolean save(Traveler o) {
        return false;
    }

    @Override
    public Traveler getById(int id) {
        return null;
    }

    @Override
    public ArrayList<Traveler> getAll() {
        return null;
    }
}
