package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    Venue findById(int id);

   // Set<Venue> findByBandId(int id);

    @Transactional
    void deleteById(int id);

    Venue findByAddress_Id(int id);

    List<Venue> findAllByAddress_Id(int id);

    Venue findByLoginInfoId(int login_info_id);

}
