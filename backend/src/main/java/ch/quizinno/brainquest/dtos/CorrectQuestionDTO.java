package ch.quizinno.brainquest.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for CorrectQuestion
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class.
@Builder
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class CorrectQuestionDTO {
    /**
     * The id of the question.
     */
    private Long id;
    /**
     * The question.
     */
    private String info;
    /**
     * The first answer.
     */
    private boolean correct;
    /**
     * The second answer.
     */
    private int correctAnswerId;
}