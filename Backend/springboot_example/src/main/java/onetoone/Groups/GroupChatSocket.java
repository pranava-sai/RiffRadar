package onetoone.Groups;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import onetoone.Chats.ChatServer;
import onetoone.Chats.Message;
import onetoone.Reviews.Review;
import onetoone.Reviews.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/groupchat/{groupId}/username/{username}")  // this is Websocket url
public class GroupChatSocket {

  // cannot autowire static directly (instead we do it by the below
  // method
	private static GroupChatMessageRepository msgRepo;


/*   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.*/

	@Autowired
	public void setGroupChatMessageRepository(GroupChatMessageRepository repo) {
		msgRepo = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	// server side logger
	private final Logger logger = LoggerFactory.getLogger(GroupChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("groupId") String groupId, @PathParam("username") String username)
			throws IOException {

		logger.info("Entered into Open");

		// store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		Long groupIdValue = Long.parseLong(groupId);

		//Send chat history to the newly connected user
		sendMessageToPArticularUser(username, getChatHistory(groupIdValue));

		// broadcast that new user joined
		//String message = "User: " + username + " has Joined the Chat";
		//broadcast(message);
	}


	@OnMessage
	public void onMessage(Session session, String message) throws IOException {

		String groupChatIdString = parseGroupChatId(message);
		Long groupChatId = Long.parseLong(groupChatIdString);

		// Handle new messages
		logger.info("Entered into Message: Got Message: " + message);
		String username = sessionUsernameMap.get(session);

		String newMessage = message.substring(groupChatIdString.length() + 5);

		// Direct message to a user using the format "@username <message>"
		if (message.startsWith("@")) {
			String destUsername = message.split(" ")[0].substring(1);

			// send the message to the sender and receiver
			sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
			sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);

		}
		else { // broadcast
			broadcast(username + ": " + newMessage);
		}

		// Saving chat history to repository
		msgRepo.save(new GroupChatMessage(username, groupChatId, newMessage));
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		// remove the user connection information
		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		// broadcast that the user disconnected
		String message = username + " disconnected";
		broadcast(message);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendMessageToPArticularUser(String username, String message) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(message);
		}
		catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}


	private void broadcast(String message) {
		sessionUsernameMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			}
			catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}

		});

	}

	private String getChatHistory(Long chatId) {
		List<GroupChatMessage> messages = msgRepo.findAll();

		// convert the list to a string
		StringBuilder sb = new StringBuilder();
		if(messages != null && messages.size() != 0) {
			for (GroupChatMessage message : messages) {
				if(message.getGroupId().equals(chatId)) {
					sb.append(message.getUserName() + ": "  + message.getContent() + "\n");
				}
			}
		}
		return sb.toString();
	}

	// Gets the Chat history from the repository
//	private String getChatHistory() {
//		List<GroupChatMessage> messages = msgRepo.findAll();
//
//		// convert the list to a string
//		StringBuilder sb = new StringBuilder();
//		if(messages != null && messages.size() != 0) {
//			for (GroupChatMessage message : messages) {
//				sb.append(message.getUserName() + ": " + message.getContent() + "\n");
//			}
//		}
//		return sb.toString();
//	}


	private String parseGroupChatId(String message){
		String id = "";
		String[] lines = message.split("\n");
		for (String line : lines) {
			if (line.startsWith("id:")) {
				id = line.substring(line.indexOf(":") + 1).trim();
				break;
			}
		}
		return id;
	}

} // end of Class
