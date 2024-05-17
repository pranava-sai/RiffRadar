package onetoone.Chats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Conversation findById(long id);

    @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.messages WHERE (c.username1 = :username1 AND c.username2 = :username2) OR (c.username1 = :username2 AND c.username2 = :username1)")
    Conversation findConversationByUsername1AndUsername2(@Param("username1") String username1, @Param("username2") String username2);
}
