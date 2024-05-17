package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Peter Yehl
 * 
 */ 

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    Attendee findById(int id);

    @Transactional
    void deleteById(int id);
}
