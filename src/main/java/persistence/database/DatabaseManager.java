package persistence.database;

import model.NdoweWord;
import persistence.database.postgresql.PostgresqlTalker;

public class DatabaseManager {
    private static DatabaseManager instance = null;
    private final DatabaseTalker databaseTalker;

    private DatabaseManager() {
        databaseTalker = new PostgresqlTalker();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public synchronized boolean addNewWord(){
        return databaseTalker.addNewWord(new NdoweWord());
    }

    public synchronized boolean editWord(){
        return true;
    }
}
