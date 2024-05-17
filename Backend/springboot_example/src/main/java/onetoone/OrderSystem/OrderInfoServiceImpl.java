package onetoone.OrderSystem;

import onetoone.Email.EmailServiceImpl;
import onetoone.OrderSystem.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

        @Autowired
        EmailServiceImpl emailService;

        @Autowired
        OrderInfoRepository orderRepository;

        @Override
        public String generateOrderNumber() {

            Random random = new Random();

            String orderNumber = String.valueOf(random.nextInt(9000) + 1000);

            while(orderRepository.findByOrderNumber(orderNumber) != null){
                orderNumber = String.valueOf(random.nextInt(9000) + 1000);
            }

            return orderNumber;
        }

        public String generateOrderTime(){
            LocalTime currentTime = LocalTime.now();
            return currentTime.toString();
        }

        public String generateOrderDate(){
            LocalDate currentDate = LocalDate.now();
            return currentDate.toString();

        }

    @Override
    @Async
    public CompletableFuture<Boolean> sendOrderByEmail(String email, String concertName, String name, String orderNumber, String numTickets) {
        // Compose the email content
        String subject = "Order Confirmation";
        String emailText = "";
        String date;
        String time;

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        date = currentDate.toString();
        time = currentTime.toString();


        emailText = emailService.getOrderEmailTemplate(concertName, name, orderNumber, date, time, numTickets);

        CompletableFuture<Void> emailSendingFuture = emailService.sendEmail(email, subject, emailText);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }
}
