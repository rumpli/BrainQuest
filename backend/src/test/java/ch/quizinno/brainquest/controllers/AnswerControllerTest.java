package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.services.AnswerService;
import ch.quizinno.brainquest.services.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class to test the AnswerController.
 */
// Specifies the controller to be tested.
@WebMvcTest(AnswerController.class)
// Ignore the security configuration for the test.
@AutoConfigureMockMvc(addFilters = false)
public class AnswerControllerTest {
    /**
     * MockMvc for testing.
     */
    // Injected required dependency into the bean.
    @Autowired
    private MockMvc mockMvc;
    /**
     * ObjectMapper for testing.
     */
    @Autowired
    // Injected required dependency into the bean.
    private ObjectMapper objectMapper;
    /**
     * MockBean for testing.
     */
    // Mock required dependency.
    @MockBean
    private AnswerService answerService;
    /**
     * MockBean for testing.
     */
    // Mock required dependency.
    @MockBean
    private QuestionService questionService;

    /**
     * Method for testing getting all questions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAllAnswers() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the getQuestionById method
        when(questionService.getQuestionById(1L)).thenReturn(Optional.of(question));
        // mock the getAllTopics method
        when(answerService.getAllAnswers()).thenReturn(List.of(answer));

        // perform the get request
        mockMvc.perform(get("/api/answers")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].answer").value("Answer 1"))
                .andExpect(jsonPath("$.[0].correct").value(true))
                .andExpect(jsonPath("$.[0].question.question").value("Question 1"));
    }

    /**
     * Method to test get all answers by question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllAnswersByQuestion() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the getQuestionById method
        when(questionService.getQuestionById(1L)).thenReturn(Optional.of(question));
        // mock the getAllTopics method
        when(answerService.getAnswersByQuestion(question)).thenReturn(List.of(answer));

        // perform the get request
        mockMvc.perform(get("/api/answers")
                        .param("questionId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].answer").value("Answer 1"))
                .andExpect(jsonPath("$.[0].correct").value(true))
                .andExpect(jsonPath("$.[0].question.question").value("Question 1"));
    }

    /**
     * Method to test get all answers by question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllAnswersByQuestion_ShouldReturnNotFound() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the getQuestionById method
        when(questionService.getQuestionById(1L)).thenReturn(Optional.of(question));
        // mock the getAllTopics method
        when(answerService.getAnswersByQuestion(question)).thenReturn(List.of(answer));

        // perform the get request
        mockMvc.perform(get("/api/answers")
                        .param("questionId", "100"))
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test get all answers by question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateAnswer() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the createAnswer method
        when(answerService.createAnswer(answer)).thenReturn((answer));

        // perform the post request
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.answer").value("Answer 1"))
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.question.question").value("Question 1"));
    }

    /**
     * Method to test create answer.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateAnswer_ShouldReturnBadRequest() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the createAnswer method
        when(answerService.createAnswer(answer)).thenThrow(new RuntimeException());

        // perform the post request
        mockMvc.perform(post("/api/answers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get answer by id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAnswerById() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1")
                .correct(true)
                .question(question)
                .build();
        // mock the getAnswerById method
        when(answerService.getAnswerById(1L)).thenReturn(Optional.of(answer));

        // perform the get request
        mockMvc.perform(get("/api/answers/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Answer 1"))
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.question.question").value("Question 1"));
    }

    /**
     * Method to test get answer by id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAnswerById_ShouldReturnNotFound() throws Exception {
        // mock the getAnswerById method
        when(answerService.getAnswerById(1L)).thenReturn(Optional.empty());

        // perform the get request
        mockMvc.perform(get("/api/answers/100")
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test update answer.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateAnswer() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1 updated")
                .correct(true)
                .question(question)
                .build();
        // mock the getAnswerById method
        when(answerService.updateAnswer(1L, answer)).thenReturn(answer);

        // perform the patch request
        mockMvc.perform(patch("/api/answers/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Answer 1 updated"))
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.question.question").value("Question 1"));
    }

    /**
     * Method to test update answer.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateAnswer_ShouldReturnNotFound() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1")
                .info("Description 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // build an answer
        Answer answer = Answer.builder()
                .id(1L)
                .answer("Answer 1 updated")
                .correct(true)
                .question(question)
                .build();
        // mock the getAnswerById method
        when(answerService.updateAnswer(100L, answer)).thenThrow(new RuntimeException("Answer not found"));

        // perform the patch request
        mockMvc.perform(patch("/api/answers/100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(answer)))
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test delete answer.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteAnswer() throws Exception {
        // mock the deleteAnswer method
        doNothing().when(answerService).deleteAnswer(1L);

        // perform the delete request
        mockMvc.perform(delete("/api/answers/1")
                        )
                .andExpect(status().isNoContent());
    }

    /**
     * Method to test delete answer.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteAnswer_ShouldReturnNotFound() throws Exception {
        // mock the deleteTopic method
        doThrow(new RuntimeException("Answer not found")).when(answerService).deleteAnswer(100L);

        // perform the delete request
        mockMvc.perform(delete("/api/answers/100")
                        )
                .andExpect(status().isNotFound());
    }

}
