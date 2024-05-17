package onetoone.Concerts;

import onetoone.Addresses.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Concert findById(int id);

    List<Concert> findAllByVenue_Id(int id);

    List<Concert> findAllByDate(String date);

    Concert findByName(String name);

    @Transactional
    void deleteById(int id);

    Concert findByVenue_Id(int id);
}
