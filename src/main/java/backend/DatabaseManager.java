package backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {

    private static DatabaseManager instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private DatabaseManager() {
        Properties props = new Properties();
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/app/config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found in classpath");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage(), e);
        }

        String uri    = props.getProperty("mongodb.uri");
        String dbName = props.getProperty("mongodb.database", "katiba");

        if (uri == null || uri.isBlank())
            throw new RuntimeException("mongodb.uri is not set in config.properties");

        mongoClient = MongoClients.create(uri);
        database    = mongoClient.getDatabase(dbName);
        System.out.println("[DB] Connected to MongoDB Atlas — database: " + dbName);
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public MongoDatabase getDatabase() { return database; }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("[DB] Connection closed.");
        }
    }
}
