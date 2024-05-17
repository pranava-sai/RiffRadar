package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface BandRespository extends JpaRepository<Band, Long> {
    Band findById(int id);

    @Transactional
    void deleteById(int id);
}
