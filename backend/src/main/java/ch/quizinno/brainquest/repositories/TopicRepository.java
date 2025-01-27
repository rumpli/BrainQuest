package ch.quizinno.brainquest.repositories;

import ch.quizinno.brainquest.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing topics.
 */
// Spring annotation to indicate that this interface is a repository.
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
