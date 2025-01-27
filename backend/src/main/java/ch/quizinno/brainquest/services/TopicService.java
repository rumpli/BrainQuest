package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.dtos.TopicDTO;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.mappers.TopicMapper;
import ch.quizinno.brainquest.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing topics.
 */
// Spring annotation to indicate that this class is a service.
@Service
public class TopicService {

    /**
     * Repository for managing topics.
     */
    private final TopicRepository topicRepository;
    /**
     * Repository for managing questions.
     */
    private final QuestionService questionService;
    /**
     * Service for managing answers.
     */
    private final AnswerService answerService;
    /**
     * Mapper for mapping topics to DTOs.
     */
    private final TopicMapper topicMapper;

    /**
     * Constructor for the TopicService.
     *
     * @param topicRepository the repository for managing topics
     * @param questionService the service for managing questions
     * @param answerService   the service for managing answers
     * @param topicMapper     the mapper for mapping topics to DTOs
     */
    public TopicService(TopicRepository topicRepository, QuestionService questionService, AnswerService answerService, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.questionService = questionService;
        this.answerService = answerService;
        this.topicMapper = topicMapper;
    }

    /**
     * Retrieves a list of all topics.
     *
     * @return a list of all topics
     */
    public List<TopicDTO> getAllTopics() {
        // get all topics
        List<Topic> topics = topicRepository.findAll();
        // initialize a list to store the mapped TopicDTOs
        List<TopicDTO> topicDTOs = new ArrayList<>();

        // map each topic to a TopicDTO
        for (Topic topic : topics) {
            // get all questions for the given topic
            List<Question> questions = questionService.getQuestionsByTopicId(topic.getId());

            // only get valid questions
            // remove questions with less than 4 answers
            questions.removeIf(question -> answerService.getAnswersByQuestion(question).size() < 4);

            // get the difficulties of the questions for the topic
            /*
                .stream is used to convert the list of question to a stream for further operations
                .map is used to map the stream of questions to a stream of difficulties
                .distinct is used to get unique difficulties
                .toList is used to convert the stream of difficulties to a list
             */
            List<Difficulty> difficulties = questions.stream()
                    .map(Question::getDifficulty)
                    .distinct()
                    .toList();

            topicDTOs.add(topicMapper.topicToTopicDTO(topic, difficulties));
        }

        return topicDTOs;
    }

    /**
     * Retrieves a topic by its ID.
     *
     * @param id the ID of the topic to retrieve
     * @return the topic with the specified ID
     */
    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    /**
     * Creates a new topic.
     *
     * @param topic the topic to create
     * @return the created topic
     */
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    /**
     * Updates a topic.
     *
     * @param id           the ID of the topic to update
     * @param topicDetails the details of the topic to update
     * @return the updated topic
     */
    public Topic updateTopic(Long id, Topic topicDetails) {
        // get the topic by its ID
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("Topic not found with id " + id));

        // update the defined topic details
        if (topicDetails.getName() != null) {
            topic.setName(topicDetails.getName());
        }
        if (topicDetails.getDescription() != null) {
            topic.setDescription(topicDetails.getDescription());
        }

        return topicRepository.save(topic);
    }

    /**
     * Deletes a topic by its ID.
     *
     * @param id the ID of the topic to delete
     */
    public void deleteTopic(Long id) {
        if (topicRepository.existsById(id)) {
            topicRepository.deleteById(id);
        } else {
            throw new RuntimeException("Topic not found with id " + id);
        }
    }
}
