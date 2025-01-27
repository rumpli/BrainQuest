package ch.quizinno.brainquest.mappers;

import ch.quizinno.brainquest.dtos.TopicDTO;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for mapping Topic entities to DTOs.
 */
@Mapper(componentModel = "spring")
public interface TopicMapper {
    /**
     * Maps a Topic entity to a TopicDTO.
     *
     * @param topic the Topic entity to map
     * @param difficulty the list of difficulties
     * @return the mapped TopicDTO
     */
    // map Topic and Difficulty to TopicDTO
    @Mapping(source = "topic.id", target = "id")
    @Mapping(source = "topic.name", target = "name")
    @Mapping(source = "topic.description", target = "description")
    @Mapping(source = "difficulty", target = "difficulty")
    TopicDTO topicToTopicDTO(Topic topic, List<Difficulty> difficulty);
}
