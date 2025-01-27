package ch.quizinno.brainquest.dtos;

import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for QuizQuestion
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class.
@Builder
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class QuizQuestionDTO {
    /**
     * The id of the question.
     */
    private Long id;
    /**
     * The question.
     */
    private String question;
    /**
     * The difficulty of the question.
     */
    private Difficulty difficulty;
    /**
     * The topic of the question.
     */
    private Topic topic;
    /**
     * The answers of the question.
     */
    private List<QuizAnswerDTO> answers;
}
