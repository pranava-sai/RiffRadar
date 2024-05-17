package onetoone.Payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 * @author Vivek Bengre
 * 
 */

@Tag(name = "PaymentInfo", description = "PaymentInfo API")
@RestController
public class PaymentInfoController {

    @Autowired
    PaymentInfoRepository paymentInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all sets of payment info")
    @GetMapping(path = "/paymentinfo")
    List<PaymentInfo> getAllPayments(){
        return paymentInfoRepository.findAll();
    }

    @Operation(summary = "Create new set of payment info")
    @PostMapping(path = "/paymentinfo")
    String createPaymentInfo(@RequestBody PaymentInfo paymentInfo){
        if (paymentInfo == null)
            return failure;
        paymentInfoRepository.save(paymentInfo);
        return success;
    }

    @Operation(summary = "Change a specific set of payment info")
    @PutMapping("/paymentinfo/{id}")
    PaymentInfo updatePaymentInfo(@PathVariable int id, @RequestBody PaymentInfo request){
        PaymentInfo user = paymentInfoRepository.findById(id);
        if(user == null)
            return null;
        paymentInfoRepository.save(request);
        return paymentInfoRepository.findById(id);
    }

    @Operation(summary = "Delete a specific set of payment info")
    @DeleteMapping(path = "/paymentinfo/{id}")
    String deletePaymentInfo(@PathVariable int id){
        paymentInfoRepository.deleteById(id);
        return success;
    }
}
