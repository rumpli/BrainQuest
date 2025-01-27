package ch.quizinno.brainquest.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for QuizCorrectAnswer
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class.
@Builder
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class QuizCorrectAnswerDTO {
    /**
     * The id of the correct answer.
     */
    private Long answerId;
    /**
     * The name of the player.
     */
    private String playerName;
    /**
     * The score of the player.
     */
    private int score;
}