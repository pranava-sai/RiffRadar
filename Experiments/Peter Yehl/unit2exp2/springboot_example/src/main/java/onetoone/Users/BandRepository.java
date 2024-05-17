package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 *
 * @author Peter Yehl
 *
 */

public interface BandRepository extends JpaRepository<Band, Long> {

    Band findById(int id);

    @Transactional
    void deleteById(int id);
}
