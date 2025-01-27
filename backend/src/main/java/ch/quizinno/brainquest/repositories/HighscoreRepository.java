package ch.quizinno.brainquest.repositories;

import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing highscores.
 */
// Spring annotation to indicate that this interface is a repository.
@Repository
public interface HighscoreRepository extends JpaRepository<Highscore, Long> {
    /**
     * Find highscores by topic id and difficulty.
     *
     * @param topicId    the topic id
     * @param difficulty the difficulty
     * @return the list of highscores
     */
    List<Highscore> findByTopicIdAndDifficulty(Long topicId, Difficulty difficulty);
}
