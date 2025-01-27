package ch.quizinno.brainquest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for QuizAnswer
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class.
@Builder
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class QuizAnswerDTO {
    /**
     * The id of the answer.
     */
    private Long id;
    /**
     * The answer.
     */
    private String answer;
}