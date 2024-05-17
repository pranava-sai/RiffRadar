package onetoone.Groups;

import onetoone.Reviews.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {

}
