package ch.quizinno.brainquest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a user.
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class (supporting inheritance for class hierarchies)
@SuperBuilder
// JPA annotation to mark this class as a JPA entity.
@Entity
// JPA annotation to specify the table name since user is a reserved keyword in DB.
@Table(name = "brainquest_user")
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class User {
    /**
     * The user's unique identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user's username.
     */
    @Column(unique = true)
    private String username;
    /**
     * The user's password.
     */
    private String password;
}
