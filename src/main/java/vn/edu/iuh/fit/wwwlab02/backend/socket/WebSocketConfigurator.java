package vn.edu.iuh.fit.wwwlab02.backend.socket;

import org.glassfish.tyrus.core.HandshakeException;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // Extract the value of the "token" query parameter
        String token = getParameter(request, "token");

        // Perform authentication based on the token (add your authentication logic here)
        if (!isValidToken(token)) {
            // Authentication failed, reject the WebSocket connection
            try {
                throw new HandshakeException("Unauthorized");
            } catch (HandshakeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getParameter(HandshakeRequest request, String paramName) {
        // Check if the parameter exists before attempting to retrieve its value
        if (request.getParameterMap().containsKey(paramName)) {
            return request.getParameterMap().get(paramName).get(0);
        } else {
            return null;
        }
    }

    private boolean isValidToken(String token) {
        // Add your authentication logic here
        // Check if the token is valid (e.g., compare with a database, validate against a token service)
        // Return true if the token is valid, false otherwise
        // This is a placeholder, and you should implement a more secure authentication mechanism
        return "your_secret_token".equals(token);
    }
}
