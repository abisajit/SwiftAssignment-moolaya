package com.example;

import com.networknt.handler.LightHttpHandler;
import io.undertow.server.HttpServerExchange;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LoadUsersHandler implements LightHttpHandler {
    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "userdb";
    private static final String COLLECTION_NAME = "users";

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        MongoClient mongoClient = MongoClients.create(MONGO_URI);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

        // Fetch data from JSON 
        String jsonData = fetchDataFromJsonPlaceholder();
        ObjectMapper mapper = new ObjectMapper();
        User[] users = mapper.readValue(jsonData, User[].class);

        // users into MongoDB
        for (User user : users) {
            Document doc = new Document("id", user.getId())
                    .append("name", user.getName())
                    .append("username", user.getUsername())
                    .append("email", user.getEmail());
            collection.insertOne(doc);
        }

        exchange.setStatusCode(200);
        exchange.getResponseSender().send("Users loaded successfully");
    }

    private String fetchDataFromJsonPlaceholder() {
        return ""; // Return the fetched JSON data
    }
}
