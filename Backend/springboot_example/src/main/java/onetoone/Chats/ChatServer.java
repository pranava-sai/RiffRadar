package onetoone.Chats;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.Id;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import netscape.javascript.JSObject;
import onetoone.Reviews.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 *
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 *
 * Example URL: ws://localhost:8080/chat/username
 *
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.*/

@ServerEndpoint("/chat/{username1}/{username2}")
@Component
public class ChatServer {

    private static MessageRepository messageRepo;

    private static ConversationRepository convoRepo;

    @Autowired
    public void setMessageRepository(MessageRepository repo){messageRepo = repo;}

    @Autowired
    public void setConversationRepository(ConversationRepository conversationRepo){convoRepo = conversationRepo;}

    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    private static Map < Session, String > sessionUsernameMap = new Hashtable < > ();
    private static Map < String, Session > usernameSessionMap = new Hashtable < > ();

    private static Map < Session, Integer > sessionConversationMap = new Hashtable < > ();
    // server side logger
    private final Logger logger = LoggerFactory.getLogger(ChatServer.class);

/**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username1 username specified in path parameter.*/


    @OnOpen
    public void onOpen(Session session, @PathParam("username1") String username1, @PathParam("username2") String username2) throws IOException {

        Conversation convo;
        // server side log
        logger.info("[onOpen] " + username1);

        if(convoRepo.findConversationByUsername1AndUsername2(username1, username2) == null){
            if(convoRepo.findConversationByUsername1AndUsername2(username2, username1) == null){
                convo = new Conversation(username1, username2);
            }
            else{
                convo = convoRepo.findConversationByUsername1AndUsername2(username2, username1);
            }
        }
        else{
            convo = convoRepo.findConversationByUsername1AndUsername2(username1, username2);
        }

        convoRepo.save(convo);

        //map current session with conversation
        sessionConversationMap.put(session, convo.getId());

        // map current session with username
        sessionUsernameMap.put(session, username1);

        // map current username with session
        usernameSessionMap.put(username1, session);

        sendMessageToParticularUser(username1, getChatHistory(convo));
    }

/**
     * Handles incoming WebSocket messages from a client.
     *
     * @param session The WebSocket session representing the client's connection.
     * @param message The message received from the client.*/


    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // get the username by session
        String username = sessionUsernameMap.get(session);

        // get the conversation by username
        int currConvoId = sessionConversationMap.get(session);

        Conversation convo = convoRepo.findById(currConvoId);
        // server side log
        logger.info("[onMessage] " + username + ": " + message);

        String actualMessage = null;
        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {

            // split by space
            String[] split_msg =  message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            actualMessage = actualMessageBuilder.toString();
            sendMessageToParticularUser(destUserName,username + ": " + actualMessage);
            sendMessageToParticularUser(username, username + ": " + actualMessage);

            Message newMessage = new Message(username, actualMessage, destUserName, convo);
            convo.addMessage(newMessage);
            newMessage.setConversation(convo);
            messageRepo.save(newMessage);
            convoRepo.save(convo);

        } else {
            // Handle the case where Conversation with the given ID is not found
            logger.error("Conversation not found for ID: " + currConvoId);
        }
    }

/**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.*/


    @OnClose
    public void onClose(Session session) throws IOException {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // server side log
        logger.info("[onClose] " + username);

        // remove user from memory mappings
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        sessionConversationMap.remove(session);
    }

/**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.*/


    @OnError
    public void onError(Session session, Throwable throwable) {

        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);

        // do error handling here
        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

/**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.*/


private void sendMessageToParticularUser(String username, String message) {
    try {
        //get session from a particular user
        Session destSession = usernameSessionMap.get(username);
        if (destSession != null) {
            destSession.getBasicRemote().sendText(message);
        } else {
            logger.info("User session not found for username: " + username);
        }
    } catch (IOException e) {
        logger.error("Error sending message to user: " + e.getMessage());
    }
}

    /**
     * Gets history specific to a conversation
     *
     * @param convo
     * @return
     */
    private String getChatHistory(Conversation convo) {
        List<Message> messages = convo.getMessages();
        if (messages == null) {
            return "";
        }

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        sb.append("History\n");
            for (Message message : messages) {
                if(message.getContent() == null){
                    continue;
                }
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }
        return sb.toString();
    }
}
