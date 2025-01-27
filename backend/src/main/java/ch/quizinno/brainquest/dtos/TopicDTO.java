package ch.quizinno.brainquest.dtos;

import ch.quizinno.brainquest.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for Topic
 */
// Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
@Data
// Lombok annotation to generate a builder pattern for the class.
@Builder
// Lombok annotation to generate a no-argument constructor.
@NoArgsConstructor
// Lombok annotation to generate a constructor with all arguments.
@AllArgsConstructor
public class TopicDTO {
    /**
     * The id of the topic.
     */
    private Long id;
    /**
     * The name of the topic.
     */
    private String name;
    /**
     * The description of the topic.
     */
    private String description;
    /**
     * Difficulties implemented for the topic.
     */
    private List<Difficulty> difficulty;
}
