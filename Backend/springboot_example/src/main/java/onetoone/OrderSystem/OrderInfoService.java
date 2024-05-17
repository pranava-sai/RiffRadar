package onetoone.OrderSystem;

import java.util.concurrent.CompletableFuture;

public interface OrderInfoService {
    String generateOrderNumber();
    CompletableFuture<Boolean> sendOrderByEmail(String email, String concertName, String name, String orderNumber, String numTickets);
}
