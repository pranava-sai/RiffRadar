package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface BandRespository extends JpaRepository<Band, Long> {
    Band findById(int id);

    //Set<Band> findByVenueId(int id);

    @Transactional
    void deleteById(int id);

    Band findByLoginInfoId(int login_info_id);

    List<Band> findAllByAddress_Id(int id);

}
