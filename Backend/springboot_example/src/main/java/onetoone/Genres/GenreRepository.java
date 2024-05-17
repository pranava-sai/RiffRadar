package onetoone.Genres;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findById(int id);

    @Transactional
    void deleteById(int id);
}
