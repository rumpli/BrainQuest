package ch.quizinno.brainquest.repositories;

import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing answers.
 */
// Spring annotation to indicate that this interface is a repository.
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    /**
     * Find all answers for a given question.
     *
     * @param question The question to find answers for.
     * @return A list of answers for the given question.
     */
    List<Answer> findByQuestion(Question question);
}
