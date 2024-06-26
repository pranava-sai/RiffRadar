package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    LoginInfo findById(int id);

    @Transactional
    void deleteById(int id);

    LoginInfo findByEmailId(String emailId);

}
