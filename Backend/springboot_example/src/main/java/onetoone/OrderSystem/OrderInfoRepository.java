package onetoone.OrderSystem;

import onetoone.OrderSystem.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    OrderInfo findById(int id);

    OrderInfo findByOrderNumber(String orderNumber);

    OrderInfo findByusername(String username);
    @Transactional
    void deleteById(int id);


}
