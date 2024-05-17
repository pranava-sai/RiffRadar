package onetomany.Phones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavoritesController {

    @Autowired
    FavoritesRepository favoritesRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/favorites")
    List<Favorites> getAllFavorites(){
        return favoritesRepository.findAll();
    }

    @GetMapping(path = "/favorites/{id}")
    Favorites getFavoritesById( @PathVariable int id){
        return favoritesRepository.findById(id);
    }

    @PostMapping(path = "/favorites")
    String createFavorites(Favorites favorites){
        if (favorites == null)
            return failure;
        favoritesRepository.save(favorites);
        return success;
    }

    @PutMapping("/favorites/{id}")
    Favorites updateFavorites(@PathVariable int id, @RequestBody Favorites request){
        Favorites favorites = favoritesRepository.findById(id);
        if(favorites == null)
            return null;
        favoritesRepository.save(request);
        return favoritesRepository.findById(id);
    } 
      
}
