package onetoone.Carts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import onetoone.OrderSystem.OrderInfo;
import onetoone.OrderSystem.OrderInfoRepository;
import onetoone.OrderSystem.OrderInfoServiceImpl;
import onetoone.Users.Attendee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import onetoone.Users.*;

@Tag(name = "Carts", description = "Cart API")
@RestController
public class CartController {

    @Autowired
   private CartRepository cartRepository;

    @Autowired
   private AttendeeRepository attendeeRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all Carts")
    @GetMapping(path = "/carts")
    List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

    @Operation(summary = "Get a specific Cart")
    @GetMapping(path = "/carts/{id}")
    Cart getCartByUserName( @PathVariable int id){
        Attendee attendee = attendeeRepository.findById(id);
        return attendee.getCart();
    }

    @Operation(summary = "Create a new cart")
    @PostMapping(path = "/carts")
    String createCart(@RequestBody Cart cart){
        if (cart == null)
            return failure;
        cartRepository.save(cart);
        return success;
    }


    @Operation(summary = "Change a specific Cart")
    @PutMapping("/carts/{id}")
    String updateCart(@PathVariable int id, @RequestBody Cart request){
        Attendee attendee = attendeeRepository.findById(id);
        if(attendee == null)
            return failure;
        Cart attendeeCart = attendee.getCart();

        if(request.getConcertName() != null){
            attendeeCart.setConcertName(request.getConcertName());
        }
        if(request.getPricePerTicket() != null){
            attendeeCart.setPricePerTicket(request.getPricePerTicket());
        }
        if(request.getNumberOfTickets() != null){
            attendeeCart.setNumberOfTickets(request.getNumberOfTickets());
        }

        cartRepository.save(attendeeCart);
        return success;
    }

    @Operation(summary = "Empty a specific Cart")
    @DeleteMapping(path = "/carts/{id}")
    String emptyCart(@PathVariable int id){
        Attendee attendee = attendeeRepository.findById(id);
        attendee.setCart(null);
        attendeeRepository.save(attendee);
        return success;
    }
}
