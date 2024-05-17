package onetoone.Genres;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Genre", description = "Genre API")
@RestController
public class GenreController {

    @Autowired
    GenreRepository genreRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(summary = "Get all genres")
    @GetMapping(path = "/genres")
    List<Genre> getAllGenres(){return genreRepository.findAll();}

    @Operation(summary = "Get a specific genre")
    @GetMapping(path = "/genres/{id}")
    Genre getGenreById(@PathVariable int id) {
        return genreRepository.findById(id);
    }

    @Operation(summary = "Create a new genre")
    @PostMapping(path = "/genres")
    String createGenre(@RequestBody Genre genre){
        if(genre == null)
            return failure;
        genreRepository.save(genre);
        return success;
    }

    @Operation(summary = "Change a specific genre")
    @PutMapping(path = "/genres/{id}")
    Genre updateGenre(@PathVariable int id, @RequestBody Genre request){
        Genre genre = genreRepository.findById(id);
        if(genre == null)
            return genre;
        genre.setGenre(request.getGenre());
        genreRepository.save(genre);
        return genreRepository.findById(id);
    }

    @Operation(summary = "delete a specific genre")
    @DeleteMapping(path = "/genres/{id}")
    String deleteGenre(@PathVariable int id){
        genreRepository.deleteById(id);
        return success;
    }
}
