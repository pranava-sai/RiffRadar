package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 *
 * @author Peter Yehl
 *
 */

public interface BandMemberRepository extends JpaRepository<BandMember, Long> {

    BandMember findById(int id);

    @Transactional
    void deleteById(int id);

}
