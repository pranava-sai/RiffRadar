package onetoone.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class BandController {

    @Autowired
    BandRespository bandRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/bands")
    List<Band> getAllBands(){
        return bandRepository.findAll();
    }

    @GetMapping(path = "/bands/{id}")
    Band getBandById( @PathVariable int id){
        return bandRepository.findById(id);
    }

    @PostMapping(path = "/bands")
    String createBand(@RequestBody Band band){
        if (band == null)
            return failure;
        bandRepository.save(band);
        return success;
    }

    @PutMapping("/bands/{id}")
    Band updateBand(@PathVariable int id, @RequestBody Band request){
        Band band = bandRepository.findById(id);
        if(band == null)
            return null;
        bandRepository.save(request);
        return bandRepository.findById(id);
    }

    @DeleteMapping(path = "/bands/{id}")
    String deleteBand(@PathVariable int id){
        bandRepository.deleteById(id);
        return success;
    }
}
