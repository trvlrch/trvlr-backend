package ch.trvlr.backend.repository;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by lucac on 29.04.2017.
 */
public abstract class Repository<T> {

    private String tableName;
    private String[] fields;
    private Connection dbConnection;

    protected Repository(String tableName, String[] fields) { }

    protected String getTableTame() {
        return this.tableName;
    }

    protected String[] getFields() {
        return this.fields;
    }

    protected abstract T getBusinessObject();

    public abstract boolean add(T o);

    public abstract boolean update(T o);

    public abstract boolean save(T o);

    public abstract T getById(int id);

    public abstract ArrayList<T> getAll();
}
