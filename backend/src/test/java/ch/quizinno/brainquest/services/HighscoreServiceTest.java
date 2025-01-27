package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.repositories.HighscoreRepository;
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
 * Test class for HighscoreService.
 */
// Create application context for testing
@SpringBootTest
// Single database transaction for all tests
@Transactional
// Create a new instance of the test class for each test method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Reset the context after each test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HighscoreServiceTest {
    /**
     * AnswerService for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private HighscoreService highscoreService;
    /**
     * AnswerRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private HighscoreRepository highscoreRepository;
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
        Topic topic1 = new Topic();
        topic1.setName("Topic 1");
        topic1.setDescription("Description 1");
        topicRepository.save(topic1);

        // used for get highscores
        Highscore highscore1 = new Highscore();
        highscore1.setScore(100);
        highscore1.setTopic(topic1);
        highscore1.setPlayerName("Player 1");
        highscore1.setDifficulty(Difficulty.EASY);
        highscoreRepository.save(highscore1);

        // used for update highscores
        Highscore highscore2 = new Highscore();
        highscore2.setScore(200);
        highscore2.setTopic(topic1);
        highscore2.setPlayerName("Player 2");
        highscore2.setDifficulty(Difficulty.EASY);
        highscoreRepository.save(highscore2);

        // used for delete highscores
        Highscore highscore3 = new Highscore();
        highscore3.setScore(300);
        highscore3.setTopic(topic1);
        highscore3.setPlayerName("Player 3");
        highscore3.setDifficulty(Difficulty.EASY);
        highscoreRepository.save(highscore3);

        Highscore highscore4 = new Highscore();
        highscore4.setScore(400);
        highscore4.setTopic(topic1);
        highscore4.setPlayerName("Player 4");
        highscore4.setDifficulty(Difficulty.EASY);
        highscoreRepository.save(highscore4);
    }

    /**
     * Test for getting all highscores.
     */
    @Test
    public void testGetAllHighscores() {
        // Call the method to be tested
        List<Highscore> highscores = highscoreService.getAllHighscores();

        // Check the result
        assertNotNull(highscores);
        assertEquals("Player 1", highscores.getFirst().getPlayerName());
        assertEquals(100, highscores.getFirst().getScore());
        assertEquals(Difficulty.EASY, highscores.getFirst().getDifficulty());
    }

    /**
     * Test for getting a highscore by its ID.
     */
    @Test
    public void testGetHighscoreById() {
        // Call the method to be tested
        Highscore highscore = highscoreService.getHighscoreById(1L).get();

        // Check the result
        assertNotNull(highscore);
        assertEquals("Player 1", highscore.getPlayerName());
        assertEquals(100, highscore.getScore());
        assertEquals(Difficulty.EASY, highscore.getDifficulty());
    }

    /**
     * Test for creating a new highscore.
     */
    @Test
    public void testCreateHighscore() {
        // Create a new highscore
        Highscore highscore = new Highscore();
        highscore.setScore(500);
        highscore.setTopic(topicRepository.findById(1L).get());
        highscore.setPlayerName("Player 5");
        highscore.setDifficulty(Difficulty.HARD);


        // Call the method to be tested
        Highscore createdHighscore = highscoreService.createHighscore(highscore);

        // Check the result
        assertNotNull(highscore);
        assertEquals("Player 5", createdHighscore.getPlayerName());
        assertEquals(500, createdHighscore.getScore());
        assertEquals(Difficulty.HARD, createdHighscore.getDifficulty());
    }

    /**
     * Test for updating a highscore.
     */
    @Test
    public void testUpdateHighscore() {
        // Update a highscore
        Highscore highscore = new Highscore();
        highscore.setScore(250);
        highscore.setPlayerName("Player 2 updated");

        // Call the method to be tested
        Highscore updateHighscore = highscoreService.updateHighscore(2L, highscore);

        // Check the result
        assertNotNull(highscore);
        assertEquals("Player 2 updated", updateHighscore.getPlayerName());
        assertEquals(250, updateHighscore.getScore());
        assertEquals(Difficulty.EASY, updateHighscore.getDifficulty());
    }

    /**
     * Test for updating a highscore.
     */
    @Test
    public void testUpdateHighscore_ShouldThrowException() {
        // Update a highscore
        Highscore highscore = new Highscore();
        highscore.setScore(100);
        highscore.setPlayerName("Player 100 updated");

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> highscoreService.updateHighscore(100L, highscore));
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteHighscore() {
        // Get the size of the topic list
        int size = highscoreRepository.findAll().size();

        // Call the method to be tested
        highscoreRepository.deleteById(4L);

        // Check the result
        assertEquals(size - 1, highscoreRepository.findAll().size());
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteHighscore_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> highscoreService.deleteHighscore(100L));
    }
}
