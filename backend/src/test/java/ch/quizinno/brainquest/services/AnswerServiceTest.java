package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.repositories.AnswerRepository;
import ch.quizinno.brainquest.repositories.QuestionRepository;
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
 * Test class for AnswerService.
 */
// Create application context for testing
@SpringBootTest
// Single database transaction for all tests
@Transactional
// Create a new instance of the test class for each test method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Reset the context after each test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AnswerServiceTest {
    /**
     * AnswerService for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private AnswerService answerService;
    /**
     * AnswerRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private AnswerRepository answerRepository;
    /**
     * questionRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private QuestionRepository questionRepository;
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

        // used for get answers
        Question question1 = new Question();
        question1.setQuestion("Question 1");
        question1.setInfo("Info 1");
        question1.setDifficulty(Difficulty.EASY);
        question1.setTopic(topic1);
        questionRepository.save(question1);

        // used for create answers
        Question question2 = new Question();
        question2.setQuestion("Question 2");
        question2.setInfo("Info 2");
        question2.setDifficulty(Difficulty.EASY);
        question2.setTopic(topic1);
        questionRepository.save(question2);

        // used for delete answers
        Question question3 = new Question();
        question3.setQuestion("Question 3");
        question3.setInfo("Info 3");
        question3.setDifficulty(Difficulty.EASY);
        question3.setTopic(topic1);
        questionRepository.save(question3);

        // used for get answers
        Answer answer1 = new Answer();
        answer1.setAnswer("Answer 1");
        answer1.setCorrect(true);
        answer1.setQuestion(question1);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setAnswer("Answer 2");
        answer2.setCorrect(false);
        answer2.setQuestion(question1);
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setAnswer("Answer 3");
        answer3.setCorrect(false);
        answer3.setQuestion(question1);
        answerRepository.save(answer3);

        Answer answer4 = new Answer();
        answer4.setAnswer("Answer 4");
        answer4.setCorrect(false);
        answer4.setQuestion(question1);
        answerRepository.save(answer4);

        // used for update answers
        Answer answer5 = new Answer();
        answer5.setAnswer("Answer 5");
        answer5.setCorrect(true);
        answer5.setQuestion(question2);
        answerRepository.save(answer5);

        // used for delete answer
        Answer answer6 = new Answer();
        answer6.setAnswer("Answer 6");
        answer6.setCorrect(false);
        answer6.setQuestion(question3);
        answerRepository.save(answer6);
    }

    /**
     * Test for getting all answers.
     */
    @Test
    public void testGetAllAnswers() {
        // Call the method to be tested
        List<Answer> answers = answerService.getAllAnswers();

        // Check the result
        assertNotNull(answers);
        assertEquals("Answer 1", answers.getFirst().getAnswer());
        assertTrue(answers.getFirst().isCorrect());
    }

    /**
     * Test for getting a answer by its ID.
     */
    @Test
    public void testGetAnswerById() {
        // Call the method to be tested
        Answer answer = answerService.getAnswerById(1L).get();

        // Check the result
        assertNotNull(answer);
        assertEquals("Answer 1", answer.getAnswer());
        assertTrue(answer.isCorrect());
    }

    /**
     * Test for creating a new answer.
     */
    @Test
    public void testCreateAnswer() {
        // Create a new answer
        Answer answer = new Answer();
        answer.setAnswer("Answer 4");
        answer.setCorrect(false);
        answer.setQuestion(questionRepository.findById(2L).get());

        // Call the method to be tested
        Answer createdAnswer = answerService.createAnswer(answer);

        // Check the result
        assertNotNull(createdAnswer);
        assertEquals("Answer 4", createdAnswer.getAnswer());
        assertFalse(answer.isCorrect());
    }

    /**
     * Test for creating a new answer.
     */
    @Test
    public void testCreateAnswer_ShouldThrowException() {
        // Create a new answer
        Answer answer = new Answer();
        answer.setAnswer("Answer 5");
        answer.setCorrect(false);
        answer.setQuestion(questionRepository.findById(1L).get());

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> answerService.createAnswer(answer));
    }

    /**
     * Test for updating a answer.
     */
    @Test
    public void testUpdateAnswer() {
        // Update a answer
        Answer answer = new Answer();
        answer.setAnswer("Answer 5 updated");
        answer.setCorrect(false);
        answer.setQuestion(questionRepository.findById(2L).get());

        // Call the method to be tested
        Answer updateAnswer = answerService.updateAnswer(1L, answer);

        // Check the result
        assertNotNull(updateAnswer);
        assertEquals("Answer 5 updated", updateAnswer.getAnswer());
        assertFalse(answer.isCorrect());
    }

    /**
     * Test for updating a answer.
     */
    @Test
    public void testUpdateAnswer_ShouldThrowException() {
        // Update a answer
        Answer answer = new Answer();
        answer.setAnswer("Answer 5 updated");
        answer.setCorrect(false);
        answer.setQuestion(questionRepository.findById(2L).get());

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> answerService.updateAnswer(100L, answer));
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteAnswer() {
        // Get the size of the topic list
        int size = answerRepository.findAll().size();

        // Call the method to be tested
        answerService.deleteAnswer(6L);

        // Check the result
        assertEquals(size - 1, answerRepository.findAll().size());
    }

    /**
     * Test for deleting a topic.
     */
    @Test
    public void testDeleteAnswer_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> answerService.deleteAnswer(100L));
    }
}
