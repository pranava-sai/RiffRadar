package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;
@RestController
public class BandController {

    HashMap<String, Band> bandList = new HashMap<>();

    //LIST OPERATION
    //gets all bands in the list and returns it in JSON format
    @GetMapping("/bands")
    public @ResponseBody HashMap<String, Band> getAllBands(){return bandList; }

    //CREATE OPERATION
    //enters the band into the list
    @PostMapping("/bands")
    public @ResponseBody String createBand(@RequestBody Band band){
        System.out.println(band);
        bandList.put(band.getBandName(), band);
        return "New band " + band.getBandName() + " Saved";
    }

    //READ OPERATION
    //extracts the person from the HashMap
    @GetMapping("/bands/{bandName}")
    public @ResponseBody Band getBand(@PathVariable String bandName){
        Band b = bandList.get(bandName);
        return b;
    }

    //UPDATE OPERATION
    //extracts the person from the HashMap and modifies it
    @PutMapping("/bands/{bandName}")
    public @ResponseBody Band updateBand(@PathVariable String bandName, @RequestBody Band b){
        bandList.replace(bandName, b);
        return bandList.get(bandName);
    }

    //DELETE OPERATION
    //deletes desired band from HashMap, returns updated list
    @DeleteMapping("/bands/{bandName}")
    public @ResponseBody HashMap<String, Band> deleteBand(@PathVariable String bandName){
        bandList.remove(bandName);
        return bandList;
    }
}
