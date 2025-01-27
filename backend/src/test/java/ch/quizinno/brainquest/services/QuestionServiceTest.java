package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.dtos.CorrectQuestionDTO;
import ch.quizinno.brainquest.dtos.QuizCorrectAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizQuestionDTO;
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

// Create application context for testing
@SpringBootTest
// Single database transaction for all tests
@Transactional
// Create a new instance of the test class for each test method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Reset the context after each test class
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class QuestionServiceTest {
    /**
     * TopicRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private TopicRepository topicRepository;
    /**
     * QuestionRepository for testing.
     */
    @Autowired
    private QuestionRepository questionRepository;
    /**
     * AnswerRepository for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;

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

        // used for get questions
        Question question1 = new Question();
        question1.setQuestion("Question 1");
        question1.setInfo("Info 1");
        question1.setDifficulty(Difficulty.EASY);
        question1.setTopic(topic1);
        questionRepository.save(question1);

        // used for update question
        Question question2 = new Question();
        question2.setQuestion("Question 2");
        question2.setInfo("Info 2");
        question2.setDifficulty(Difficulty.EASY);
        question2.setTopic(topic1);
        questionRepository.save(question2);

        // used for delete question
        Question question3 = new Question();
        question3.setQuestion("Question 3");
        question3.setInfo("Info 3");
        question3.setDifficulty(Difficulty.EASY);
        question3.setTopic(topic1);
        questionRepository.save(question3);

        // used for get quiz question
        Question question10 = new Question();
        question10.setQuestion("Question 10");
        question10.setInfo("Info 10");
        question10.setDifficulty(Difficulty.HARD);
        question10.setTopic(topic1);
        questionRepository.save(question10);

        Answer answer1 = new Answer();
        answer1.setAnswer("Answer 1");
        answer1.setCorrect(true);
        answer1.setQuestion(question10);
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setAnswer("Answer 2");
        answer2.setCorrect(false);
        answer2.setQuestion(question10);
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setAnswer("Answer 2");
        answer3.setCorrect(false);
        answer3.setQuestion(question10);
        answerRepository.save(answer3);

        Answer answer4 = new Answer();
        answer4.setAnswer("Answer 2");
        answer4.setCorrect(false);
        answer4.setQuestion(question10);
        answerRepository.save(answer4);

    }

    /**
     * Test for getting all questions.
     */
    @Test
    public void testGetQuestions() {
        // Call the method to be tested
        List<Question> questions = questionService.getAllQuestions();

        // Check the result
        assertNotNull(questions);
        assertEquals("Question 1", questions.getFirst().getQuestion());
        assertEquals("Info 1", questions.getFirst().getInfo());
    }

    /**
     * Test for getting a question by its ID.
     */
    @Test
    public void testGetQuestionById() {
        // Call the method to be tested
        Question questions = questionService.getQuestionById(1L).get();

        // Check the result
        assertNotNull(questions);
        assertEquals("Question 1", questions.getQuestion());
        assertEquals("Info 1", questions.getInfo());
    }

    /**
     * Test for creating a new question.
     */
    @Test
    public void testCreateQuestion() {
        // Create a new question
        Question question = new Question();
        question.setQuestion("Question 4");
        question.setInfo("Info 4");

        // Call the method to be tested
        Question createdQuestion = questionService.createQuestion(question);

        // Check the result
        assertNotNull(createdQuestion);
        assertEquals("Question 4", createdQuestion.getQuestion());
        assertEquals("Info 4", createdQuestion.getInfo());
    }

    /**
     * Test for updating a question.
     */
    @Test
    public void testUpdateQuestion() {
        // Create a new question
        Question question = new Question();
        question.setQuestion("Question 2 updated");
        question.setInfo("Info 2 updated");

        // Call the method to be tested
        Question updatedQuestion = questionService.updateQuestion(2L, question);

        // Check the result
        assertNotNull(updatedQuestion);
        assertEquals("Question 2 updated", updatedQuestion.getQuestion());
        assertEquals("Info 2 updated", updatedQuestion.getInfo());
    }

    /**
     * Test for updating a question.
     */
    @Test
    public void testUpdateQuestion_ShouldThrowException() {
        // Create a new question
        Question question = new Question();
        question.setQuestion("Question 100 updated");
        question.setInfo("Info 100 updated");

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> questionService.updateQuestion(100L, question));
    }

    /**
     * Test for deleting a question.
     */
    @Test
    public void testDeleteQuestion() {
        // Get the size of the question list
        int size = questionRepository.findAll().size();

        // Call the method to be tested
        questionService.deleteQuestion(3L);

        // Check the result
        assertEquals(size - 1, questionRepository.findAll().size());
    }

    /**
     * Test for deleting a question.
     */
    @Test
    public void testDeleteQuestion_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> questionService.deleteQuestion(100L));
    }

    /**
     * Test for getting a quiz question by its topic ID.
     */
    @Test
    public void testGetQuizQuestionByTopicId() {
        // Call the method to be tested
        List<Question> questions = questionService.getQuestionsByTopicId(1L);

        // Check the result
        // Check the result
        assertNotNull(questions);
        assertEquals("Question 1", questions.getFirst().getQuestion());
        assertEquals("Info 1", questions.getFirst().getInfo());
    }

    /**
     * Test for getting a quiz question by its topic ID and difficulty.
     */
    @Test
    public void testGetQuizQuestionByTopicIdAndDifficulty() {
        // Call the method to be tested
        List<Question> questions = questionService.getQuestionsByTopicIdAndDifficulty(1L, Difficulty.HARD);

        // Check the result
        assertNotNull(questions);
        assertEquals("Question 10", questions.getFirst().getQuestion());
        assertEquals("Info 10", questions.getFirst().getInfo());
    }

    /**
     * Test for getting quiz questions.
     */
    @Test
    public void testGetQuizQuestions() {
        // Call the method to be tested
        QuizQuestionDTO quizQuestionDTO = questionService.getQuizQuestion(1L, Difficulty.HARD, List.of(1L), "Player 1", 100);

        // Check the result
        assertNotNull(quizQuestionDTO);
        assertEquals("Question 10", quizQuestionDTO.getQuestion());
        assertEquals("Topic 1", quizQuestionDTO.getTopic().getName());
        assertEquals(Difficulty.HARD, quizQuestionDTO.getDifficulty());
        assertEquals(4, quizQuestionDTO.getAnswers().size());
    }

    /**
     * Test for getting quiz questions.
     */
    @Test
    public void testGetQuizQuestions_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> questionService.getQuizQuestion(null, Difficulty.HARD, List.of(1L), "Player 1", 100));
        assertThrows(RuntimeException.class, () -> questionService.getQuizQuestion(1L, null, List.of(1L), "Player 1", 100));
    }

    /**
     * Test for correcting the answer.
     */
    @Test
    public void testCheckCorrectAnswer() {
        // build DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = new QuizCorrectAnswerDTO();
        quizCorrectAnswerDTO.setAnswerId(1L);
        quizCorrectAnswerDTO.setPlayerName("test");
        quizCorrectAnswerDTO.setScore(100);

        // get question id
        Long questionId = answerRepository.findById(1L).get().getQuestion().getId();

        // Call the method to be tested
        CorrectQuestionDTO correctQuestionDTO = questionService.checkCorrectAnswer(questionId, quizCorrectAnswerDTO);

        // Check the result
        assertEquals("Info 10", correctQuestionDTO.getInfo());
        assertTrue(correctQuestionDTO.isCorrect());
        assertEquals(1, correctQuestionDTO.getCorrectAnswerId());
    }

    /**
     * Test for correcting the answer.
     */
    @Test
    public void testCheckCorrectAnswer_WrongAnswer() {
        // build DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = new QuizCorrectAnswerDTO();
        quizCorrectAnswerDTO.setAnswerId(2L);
        quizCorrectAnswerDTO.setPlayerName("test");
        quizCorrectAnswerDTO.setScore(100);

        // get question id
        Long questionId = answerRepository.findById(1L).get().getQuestion().getId();

        // Call the method to be tested
        CorrectQuestionDTO correctQuestionDTO = questionService.checkCorrectAnswer(questionId, quizCorrectAnswerDTO);

        // Check the result
        assertEquals("Info 10", correctQuestionDTO.getInfo());
        assertFalse(correctQuestionDTO.isCorrect());
        assertEquals(1, correctQuestionDTO.getCorrectAnswerId());
    }

    /**
     * Test for correcting the answer.
     */
    @Test
    public void testCheckCorrectAnswer_ShouldThrowException() {
        // build DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = new QuizCorrectAnswerDTO();
        quizCorrectAnswerDTO.setAnswerId(2L);
        quizCorrectAnswerDTO.setPlayerName("test");
        quizCorrectAnswerDTO.setScore(100);

        // get question id
        Long questionId = answerRepository.findById(1L).get().getQuestion().getId();

        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> questionService.checkCorrectAnswer(null, quizCorrectAnswerDTO));
        assertThrows(RuntimeException.class, () -> questionService.checkCorrectAnswer(questionId, null));
    }
    /**
     * Test for using joker.
     */
    @Test
    public void testFiftyFiftyJoker() {
        // get question id
        Long questionId = answerRepository.findById(1L).get().getQuestion().getId();

        // Call the method to be tested
        QuizQuestionDTO quizQuestionDTO = questionService.fiftyFiftyJoker(questionId);

        // Check the result
        assertNotNull(quizQuestionDTO);
        assertEquals("Question 10", quizQuestionDTO.getQuestion());
        assertEquals("Topic 1", quizQuestionDTO.getTopic().getName());
        assertEquals(Difficulty.HARD, quizQuestionDTO.getDifficulty());
        assertEquals(2, quizQuestionDTO.getAnswers().size());
    }

    /**
     * Test for using joker.
     */
    @Test
    public void testFiftyFiftyJoker_ShouldThrowException() {
        // Call the method to be tested
        // Check the result
        assertThrows(RuntimeException.class, () -> questionService.fiftyFiftyJoker(100L));
    }
}
