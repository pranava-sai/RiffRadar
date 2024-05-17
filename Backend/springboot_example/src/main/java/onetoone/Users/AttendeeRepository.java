package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    Attendee findById(int id);

    @Transactional
    void deleteById(int id);

    Attendee findByLoginInfoId(int login_info_id);

    Attendee findByName(String name);

}
