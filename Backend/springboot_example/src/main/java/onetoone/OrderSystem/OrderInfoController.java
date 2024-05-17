package onetoone.OrderSystem;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.OTP.OTPInfo;
import onetoone.OTP.OTPServiceImpl;
import onetoone.Users.Band;
import onetoone.Users.BandRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Tag(name = "Orders", description = "Order API")
@RestController
public class OrderInfoController {

    @Autowired
    OrderInfoRepository orderRepository;

    @Autowired
    private OrderInfoServiceImpl orderService;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all Orders")
    @GetMapping(path = "/orders")
    List<OrderInfo> getAllOrders(){
        return orderRepository.findAll();
    }

    @Operation(summary = "Get a specific Order")
    @GetMapping(path = "/orders/{orderNumber}")
    OrderInfo getOrderById( @PathVariable String orderNumber){
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @PostMapping("/generateorder")
    public ResponseEntity<?> generateOrder(@RequestBody OrderInfo order) {

        String email = order.getEmail();
        String name = order.getUsername();
        String concertName = order.getConcertName();
        String orderNumber = String.valueOf(orderService.generateOrderNumber());
        order.setOrderNumber(orderNumber);
        order.setOrderDate(orderService.generateOrderDate());
        order.setOrderTime(orderService.generateOrderTime());

        if(order.getPaymentInfo() == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Payment info was invalid\"}");
        }


        // Send the OTP to the user's email address asynchronously
        CompletableFuture<Boolean> emailSendingFuture = orderService.sendOrderByEmail(email, concertName, name, orderNumber, order.getNumberOfTickets());

        //Save the order to the repository
        orderRepository.save(order);

        // Wait for the email sending process to complete and handle the response
        try {
            boolean otpSent = emailSendingFuture.get(); // This will block until the email sending is complete

            if (otpSent) {
                // Return JSON response with success message
                return ResponseEntity.ok().body("{\"message\": \"Order confirmation sent successfully\",  \"Order Number\": " + orderNumber + " }");
            } else {
                // Return JSON response with error message
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to create order\"}");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Return JSON response with error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to create order\"}");
        }

    }


    @Operation(summary = "Change a specific Order")
    @PutMapping("/orders/{id}")
    OrderInfo updateOrder(@PathVariable int id, @RequestBody OrderInfo request){
        OrderInfo order = orderRepository.findById(id);
        if(order == null)
            return null;
        orderRepository.save(request);
        return orderRepository.findById(id);
    }

    @Operation(summary = "Delete a specific order")
    @DeleteMapping(path = "/orders/{id}")
    String deleteOrder(@PathVariable int id){
        orderRepository.deleteById(id);
        return success;
    }
}
