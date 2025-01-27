package ch.quizinno.brainquest.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a topic.
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class (supporting inheritance for class hierarchies)
@SuperBuilder
// JPA annotation to mark this class as a JPA entity.
@Entity
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
public class Topic {
    /**
     * The unique identifier of the topic.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the topic.
     */
    @Column(unique = true)
    private String name;
    /**
     * The description of the topic.
     */
    private String description;
}
