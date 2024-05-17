package onetoone.Groups;

import onetoone.Concerts.Concert;
import onetoone.Users.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup findById(int id);

    @Transactional
    void deleteById(int id);

    UserGroup findByName(String name);
}
