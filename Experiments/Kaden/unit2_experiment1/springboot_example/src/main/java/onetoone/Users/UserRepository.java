package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface UserRepository extends JpaRepository<User, String> {
    
    User findByUserName(String userName);

    void deleteByUserName(String userName);

    //User findByLaptop_Id(int id);


}
