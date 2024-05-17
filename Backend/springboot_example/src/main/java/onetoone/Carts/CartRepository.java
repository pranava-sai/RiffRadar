package onetoone.Carts;

import onetoone.Concerts.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findById(int id);

    @Transactional
    void deleteById(int id);

}
