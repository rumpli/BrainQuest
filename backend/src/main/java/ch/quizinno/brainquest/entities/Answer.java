package ch.quizinno.brainquest.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents an answer to a question.
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class (supporting inheritance for class hierarchies)
@SuperBuilder
// JPA annotation to mark this class as a JPA entity.
@Entity
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
public class Answer {
    /**
     * The unique identifier of the answer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The answer to the question.
     */
    private String answer;
    /**
     * Whether the answer is correct.
     */
    private boolean correct;

    /**
     * The question this answer belongs to.
     */
    // fetch the question entity when loading the answer entity.
    @ManyToOne(fetch = FetchType.EAGER)
    private Question question;
}