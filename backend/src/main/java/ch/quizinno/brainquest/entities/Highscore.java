package ch.quizinno.brainquest.entities;

import ch.quizinno.brainquest.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a highscore entry.
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class (supporting inheritance for class hierarchies)
@SuperBuilder
// JPA annotation to mark this class as a JPA entity.
@Entity
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
public class Highscore {
    /**
     * The unique identifier of the highscore entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the player.
     */
    private String playerName;
    /**
     * The score of the player.
     */
    private int score;

    /**
     * The difficulty of the highscore entry.
     */
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    /**
     * The topic of the highscore entry.
     */
    // fetch the question entity when loading the answer entity.
    @ManyToOne(fetch = FetchType.EAGER)
    private Topic topic;
}