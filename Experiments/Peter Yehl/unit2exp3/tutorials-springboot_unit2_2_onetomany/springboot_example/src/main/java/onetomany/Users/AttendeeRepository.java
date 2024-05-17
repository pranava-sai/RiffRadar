package onetomany.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    Attendee findById(int id);

    @Transactional
    void deleteById(int id);
}
