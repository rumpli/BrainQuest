package ch.quizinno.brainquest.repositories;

import ch.quizinno.brainquest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing users.
 */
// Spring annotation to indicate that this interface is a repository.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by their username.
     *
     * @param username The username of the user to find.
     * @return The user with the given username, if it exists.
     */
    Optional<User> findByUsername(String username);
}