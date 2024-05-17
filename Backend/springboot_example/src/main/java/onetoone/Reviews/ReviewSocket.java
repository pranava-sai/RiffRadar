package onetoone.Reviews;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.compare;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/review/{username}/{chatId}")  // this is Websocket url
public class ReviewSocket {

	// cannot autowire static directly (instead we do it by the below
	// method
	private static ReviewRepository reviewRepo;


	/*   * Grabs the MessageRepository singleton from the Spring Application
	 * Context.  This works because of the @Controller annotation on this
	 * class and because the variable is declared as static.
	 * There are other ways to set this. However, this approach is
	 * easiest.*/

	@Autowired
	public void setReviewRepository(ReviewRepository repo) {
		reviewRepo = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(ReviewSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username, @PathParam("chatId") String chatId)
			throws IOException {

		logger.info("Entered into Open");

		// store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		//Send chat history to the newly connected user
		sendReviewToParticularUser(username, getChatHistory(chatId));

		// Would normally broadcast that A user has joined but we don't want to do that for reviews

		//String message = "User:" + username + " has Joined the Chat";
		//broadcast(message);
	}


	@OnMessage
	public void onMessage(Session session,String message) throws IOException {

		//Initializations
		float rating;
		String chatId = "";

		//Parse the chatId of the message and remove it from the string
		String newMessage;
		chatId = parseConcertId(message);

		newMessage = message.substring((chatId.length() + 5));

		//Parse the float for the rating from the message
		Matcher m = Pattern.compile("\\b\\d+(\\.\\d+)?\\b").matcher(newMessage);
		m.find();
		rating = Float.valueOf(m.group());




		char[] charArray = newMessage.toCharArray();
		String finalMessage = "";

		//Create the final message
		for(int i = 0; i < charArray.length; i++){
			if(!Character.isDigit(charArray[i]) ){
				finalMessage = finalMessage + charArray[i];
			}
		}



		finalMessage = finalMessage.substring(1);
		// Handle new messages
		logger.info("Entered into Message: Got feedback: " + finalMessage + " Rating: " + rating);
		String username = sessionUsernameMap.get(session);

		broadcast("Rating: " + rating + "/5.0" + finalMessage + "\n");


		// Saving chat history to repository
		reviewRepo.save(new Review(username, rating, finalMessage, chatId));
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		// remove the user connection information
		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		// broadcast that the user disconnected
		//Don't need to show this for reviews
		//String message = username + " disconnected";
		//broadcast(message);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendReviewToParticularUser(String username, String review) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(review);
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


	// Gets the Chat history from the repository
	private String getChatHistory(String chatId) {
		List<Review> reviews = reviewRepo.findAll();

		// convert the list to a string
		StringBuilder sb = new StringBuilder();
		if(reviews != null && reviews.size() != 0) {
			for (Review review : reviews) {
				if(review.getChatId().equals(chatId)) {
					sb.append("Rating: " + review.getRating() + "/5.0"  + review.getContent() + "\n");
				}
			}
		}
		return sb.toString();
	}

	private String parseConcertId(String message){
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
