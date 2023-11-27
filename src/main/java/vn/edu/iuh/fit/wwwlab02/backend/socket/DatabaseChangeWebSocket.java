package vn.edu.iuh.fit.wwwlab02.backend.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/websocket")
public class DatabaseChangeWebSocket {

    private static final Logger LOGGER = Logger.getLogger(DatabaseChangeWebSocket.class.getName());

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        // Handle WebSocket connection open
        sessions.add(session);
        LOGGER.log(Level.INFO, "WebSocket connection opened. Session ID: {0}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle incoming messages from clients if needed
    }

    @OnClose
    public void onClose(Session session) {
        // Handle WebSocket connection close
        sessions.remove(session);
        LOGGER.log(Level.INFO, "WebSocket connection closed. Session ID: {0}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Handle WebSocket errors
        LOGGER.log(Level.SEVERE, "WebSocket error in session ID: " + session.getId(), throwable);
        throwable.printStackTrace();
        // Optionally, you can remove the session from the sessions set in case of an error
        sessions.remove(session);
    }

    // Method to send a message to all connected clients
    public static void sendMessageToClients(String message) {
        // Use an iterator to avoid ConcurrentModificationException
        for (Session session : new HashSet<>(sessions)) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                // Handle exceptions when sending messages
                LOGGER.log(Level.SEVERE, "Error sending message to session ID: " + session.getId(), e);
                e.printStackTrace();
            }
        }
    }

    // Method to handle database changes and notify clients
    public static void handleDatabaseChange() {
        sendMessageToClients("ReloadFetchAPI");
        LOGGER.log(Level.INFO, "Database change detected. Notifying clients.");
    }
}
