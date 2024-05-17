package onetoone.Addresses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findById(int id);

    List<Address> findAllByState(String state);

    @Transactional
    void deleteById(int id);
}
