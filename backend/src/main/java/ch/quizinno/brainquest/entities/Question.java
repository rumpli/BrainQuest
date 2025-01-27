package ch.quizinno.brainquest.entities;

import ch.quizinno.brainquest.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a question.
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class (supporting inheritance for class hierarchies)
@SuperBuilder
// JPA annotation to mark this class as a JPA entity.
@Entity
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
public class Question {
    /**
     * The unique identifier of the question.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The question.
     */
    private String question;
    /**
     * Further information about the question.
     */
    @Column(length = 2000)
    private String info;

    /**
     * Difficulty of the question.
     */
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    /**
     * Topic that the question belongs to.
     */
    // fetch the topic entity when loading the question entity.
    @ManyToOne(fetch = FetchType.EAGER)
    private Topic topic;
}
