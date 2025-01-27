package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.dtos.TopicDTO;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.repositories.TopicRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TopicService.
 */
// Create application context for testing
@SpringBootTest
// Single database transaction for all tests
@Transactional
// Create a new instance of the test class for each test method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Reset the context after each test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TopicServiceTest {
    /**
     * TopicService for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private TopicService topicService;
    /**
     * TopicRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private TopicRepository topicRepository;

    /**
     * Method to setup data for testing.
     */
    // Run before all tests in the class
    @BeforeAll
    public void setup() {
        // used for get topics
        Topic topic1 = new Topic();
        topic1.setName("Topic 1");
        topic1.setDescription("Description 1");
        topicRepository.save(topic1);

        // used for update topic
        Topic topic2 = new Topic();
        topic2.setName("Topic 2");
        topic2.setDescription("Description 2");
        topicRepository.save(topic2);

        // used for delete topic
        Topic topic3 = new Topic();
        topic3.setName("Topic 3");
        topic3.setDescription("Description 3");
        topicRepository.save(topic3);
    }

    /**
     * Test for getting all topics.
     */
    @Test
    public void testGetTopics() {
        // Call the method to be tested
        List<TopicDTO> topics = topicService.getAllTopics();

        // Check the result
        assertNotNull(topics);
        assertEquals("Topic 1", topics.getFirst().getName());
        assertEquals(0, topics.getFirst().getDifficulty().size());
    }

    /**
     * Test for getting a topic by its ID.
     */
    @Test
    public void testGetTopicById() {
        // Call the method to be tested
        Topic topic = topicService.getTopicById(1L).get();

        // Check the result
        assertNotNull(topic);
        assertEquals("Topic 1", topic.getName());
        assertEquals("Description 1", topic.getDescription());
    }

    /**
     * Test for creating a new topic.
     */
    @Test
    public void testCreateTopic() {
        // Create a new topic
        Topic topic = new Topic();
        topic.setName("Topic 4");
        topic.setDescription("Description 4");

        // Call the method to be tested
        Topic createdTopic = topicService.createTopic(topic);

        // Check the result
        assertNotNull(createdTopic);
        assertEquals("Topic 4", createdTopic.getName());
        assertEquals("Description 4", createdTopic.getDescription());
    }

    /**
     * Test for updating a topic.
     */
    @Test
    public void testUpdateTopic() {
        // Create a new topic
        Topic topic = new Topic();
        topic.setName("Topic 2 updated");
        topic.setDescription("Description 2 updated");

        // Call the method to be tested
        Topic updatedTopic = topicService.updateTopic(2L, topic);

        // Check the result
        assertNotNull(updatedTopic);
        assertEquals("Topic 2 updated", updatedTopic.getName());
        assertEquals("Description 2 updated", updatedTopic.getDescription());
    }

    /**
     * Test for updating a topic.
     */
    @Test
    public void testUpdateTopic_ShouldThrowException() {
        // Create a new topic
        Topic topic = new Topic();
        topic.setName("Topic 100 updated");
        topic.setDescription("Description 100 updated");

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> topicService.updateTopic(100L, topic));
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteTopic() {
        // Get the size of the topic list
        int size = topicRepository.findAll().size();

        // Call the method to be tested
        topicService.deleteTopic(3L);

        // Check the result
        assertEquals(size - 1, topicRepository.findAll().size());
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteTopic_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> topicService.deleteTopic(100L));
    }
}