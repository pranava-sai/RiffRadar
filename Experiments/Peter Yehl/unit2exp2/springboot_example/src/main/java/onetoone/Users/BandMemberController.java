package onetoone.Users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Users.Band;

/**
 *
 * @author Peter Yehl
 *
 */

@RestController
public class BandMemberController {

    @Autowired
    BandMemberRepository bandMemberRepository;

    @Autowired
    BandRepository bandRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/bandMembers")
    List<BandMember> getAllBandMembers(){
        return bandMemberRepository.findAll();
    }

    @GetMapping(path = "/bandMembers/{id}")
    BandMember getBandMemberById(@PathVariable int id){
        return bandMemberRepository.findById(id);
    }

    @PostMapping(path = "/bandMembers")
    String createBandMember(@RequestBody BandMember bandMember){
        if (bandMember == null)
            return failure;
        bandMemberRepository.save(bandMember);
        return success;
    }

    @PutMapping("/bandMembers/{id}")
    BandMember updateBandMember(@PathVariable int id, @RequestBody BandMember request){
        BandMember bandMember = bandMemberRepository.findById(id);

        if(bandMember == null) {
            throw new RuntimeException("band member id does not exist");
        }
        else if (bandMember.getId() != id){
            throw new RuntimeException("path variable id does not match band member request id");
        }

        bandMemberRepository.save(request);
        return bandMemberRepository.findById(id);
    }

    @PutMapping("/bandMembers/{bandMemberId}/band/{bandId}")
    String assignBandMemberToBand(@PathVariable int bandMemberId,@PathVariable int bandId){
        BandMember bandMember = bandMemberRepository.findById(bandMemberId);
        Band band = bandRepository.findById(bandId);
        if(bandMember == null || band == null)
            return failure;
        band.setBandMembers(bandMember);
        bandMember.setBand(band);
        bandMemberRepository.save(bandMember);
        return success;
    }

    @DeleteMapping(path = "/bandMembers/{id}")
    String deleteUser(@PathVariable int id){
        bandMemberRepository.deleteById(id);
        return success;
    }
}
