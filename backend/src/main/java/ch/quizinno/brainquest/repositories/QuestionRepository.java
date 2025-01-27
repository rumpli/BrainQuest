package ch.quizinno.brainquest.repositories;

import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.enums.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing questions.
 */
// Spring annotation to indicate that this interface is a repository.
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * Find all questions by topic id and difficulty.
     *
     * @param topicId the topic id
     * @param difficulty the difficulty
     * @return the list of questions
     */
    List<Question> findByTopicIdAndDifficulty(Long topicId, Difficulty difficulty);

    /**
     * Find all questions by topic id.
     *
     * @param topicId the topic id
     * @return the list of questions
     */
    List<Question> findByTopicId(Long topicId);
}
