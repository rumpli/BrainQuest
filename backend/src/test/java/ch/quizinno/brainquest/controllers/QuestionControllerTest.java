package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.CorrectQuestionDTO;
import ch.quizinno.brainquest.dtos.QuizAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizCorrectAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizQuestionDTO;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
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

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for QuestionController.
 */
// Specifies the controller to be tested.
@WebMvcTest(QuestionController.class)
// Ignore the security configuration for the test.
@AutoConfigureMockMvc(addFilters = false)
public class QuestionControllerTest {
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
    private QuestionService questionService;

    /**
     * Method for testing getting all questions.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAllQuestions() throws Exception {
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
        // mock the getAllTopics method
        when(questionService.getAllQuestions()).thenReturn(List.of(question));

        // perform the get request
        mockMvc.perform(get("/api/questions")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("Question 1"))
                .andExpect(jsonPath("$[0].topic.name").value("Topic 1"))
                .andExpect(jsonPath("$[0].difficulty").value("EASY"));
    }

    /**
     * Method to test get all questions by topic id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllQuestionsByTopicIdAndDifficulty() throws Exception {
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
        // mock the getAllTopics method
        when(questionService.getQuestionsByTopicIdAndDifficulty(1L, Difficulty.EASY)).thenReturn(List.of(question));

        // perform the get request
        mockMvc.perform(get("/api/questions")
                        .param("topicId", "1")
                        .param("difficulty", "EASY")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("Question 1"))
                .andExpect(jsonPath("$[0].topic.name").value("Topic 1"))
                .andExpect(jsonPath("$[0].difficulty").value("EASY"));
    }

    /**
     * Method to test get all questions by topic id and difficulty.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllQuestionsByTopicIdAndDifficulty_ShouldReturnEmpty() throws Exception {
        // mock the getAllTopics method
        when(questionService.getQuestionsByTopicIdAndDifficulty(100L, Difficulty.HARD)).thenReturn(List.of());

        // perform the get request
        mockMvc.perform(get("/api/questions")
                        .param("topicId", "100")
                        .param("difficulty", "HARD")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Method to test get all questions by topic id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllQuestionsByTopicId_ShouldReturnBadRequest() throws Exception {
        // perform the get request
        mockMvc.perform(get("/api/questions")
                        .param("topicId", "1")
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get all questions by difficulty.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllQuestionsByDifficulty_ShouldReturnBadRequest() throws Exception {
        // perform the get request
        mockMvc.perform(get("/api/questions")
                        .param("difficulty", "HARD")
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test create question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateQuestion() throws Exception {
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

        // mock the createQuestion method
        when(questionService.createQuestion(question)).thenReturn(question);

        // perform the post request
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question").value("Question 1"))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"))
                .andExpect(jsonPath("$.difficulty").value("EASY"));
    }

    /**
     * Method to test get question by id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuestionById() throws Exception {
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
        // mock the getAllTopics method
        when(questionService.getQuestionById(1L)).thenReturn(Optional.ofNullable(question));

        // perform the get request
        mockMvc.perform(get("/api/questions/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Question 1"))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"))
                .andExpect(jsonPath("$.difficulty").value("EASY"));
    }

    /**
     * Method to test get question by id.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuestionById_ShouldReturnNotFound() throws Exception {
        // mock the getAllTopics method
        when(questionService.getQuestionById(100L)).thenReturn(Optional.empty());

        // perform the get request
        mockMvc.perform(get("/api/questions/100")
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test update question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateQuestion() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 1 updated")
                .info("Description 1 updated")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // mock the getAllTopics method
        when(questionService.updateQuestion(1L, question)).thenReturn(question);

        // perform the patch request
        mockMvc.perform(patch("/api/questions/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Question 1 updated"))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"))
                .andExpect(jsonPath("$.difficulty").value("EASY"));
    }

    /**
     * Method to test update question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateQuestion_ShouldReturnNotFound() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a question
        Question question = Question.builder()
                .id(1L)
                .question("Question 100 updated")
                .info("Description 100 updated")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .build();
        // mock the getAllTopics method
        when(questionService.updateQuestion(100L, question)).thenThrow(new RuntimeException("Question not found"));

        // perform the patch request
        mockMvc.perform(patch("/api/questions/100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(question))
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test delete question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteQuestion() throws Exception {
        // mock the deleteTopic method
        doNothing().when(questionService).deleteQuestion(1L);

        // perform the delete request
        mockMvc.perform(delete("/api/questions/1")
                )
                .andExpect(status().isNoContent());
    }

    /**
     * Method to test delete question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteQuestion_ShouldReturnNotFound() throws Exception {
        // mock the deleteTopic method
        doThrow(new RuntimeException("Question not found with id")).when(questionService).deleteQuestion(100L);

        // perform the delete request
        mockMvc.perform(delete("/api/questions/100")
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuizQuestion() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a answerDTO
        QuizAnswerDTO quizAnswerDTO1 = QuizAnswerDTO.builder()
                .id(1L)
                .answer("Answer 1")
                .build();
        QuizAnswerDTO quizAnswerDTO2 = QuizAnswerDTO.builder()
                .id(2L)
                .answer("Answer 2")
                .build();
        QuizAnswerDTO quizAnswerDTO3 = QuizAnswerDTO.builder()
                .id(3L)
                .answer("Answer 3")
                .build();
        QuizAnswerDTO quizAnswerDTO4 = QuizAnswerDTO.builder()
                .id(4L)
                .answer("Answer 4")
                .build();
        // build a questionDTP
        QuizQuestionDTO quizQuestionDTO = QuizQuestionDTO.builder()
                .id(1L)
                .question("Question 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .answers(List.of(quizAnswerDTO1, quizAnswerDTO2, quizAnswerDTO3, quizAnswerDTO4))
                .build();
        // mock the getQuizQuestion method
        when(questionService.getQuizQuestion(1L, Difficulty.HARD, List.of(), "Player 1", 100)).thenReturn(quizQuestionDTO);

        // perform the get request
        mockMvc.perform(get("/api/questions/quiz-question")
                        .param("topicId", "1")
                        .param("difficulty", "HARD")
                        .param("excludeIds", "")
                        .param("playerName", "Player 1")
                        .param("score", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Question 1"))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"))
                .andExpect(jsonPath("$.difficulty").value("EASY"))
                .andExpect(jsonPath("$.answers.size()").value(4))
                .andExpect(jsonPath("$.answers[*].answer", hasItem("Answer 1")));
    }

    @Test
    public void testGetQuizQuestionExcludeQuestionIds() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a answerDTO
        QuizAnswerDTO quizAnswerDTO1 = QuizAnswerDTO.builder()
                .id(1L)
                .answer("Answer 1")
                .build();
        QuizAnswerDTO quizAnswerDTO2 = QuizAnswerDTO.builder()
                .id(2L)
                .answer("Answer 2")
                .build();
        QuizAnswerDTO quizAnswerDTO3 = QuizAnswerDTO.builder()
                .id(3L)
                .answer("Answer 3")
                .build();
        QuizAnswerDTO quizAnswerDTO4 = QuizAnswerDTO.builder()
                .id(4L)
                .answer("Answer 4")
                .build();
        // build a questionDTP
        QuizQuestionDTO quizQuestionDTO = QuizQuestionDTO.builder()
                .id(1L)
                .question("Question 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .answers(List.of(quizAnswerDTO1, quizAnswerDTO2, quizAnswerDTO3, quizAnswerDTO4))
                .build();
        // mock the getQuizQuestion method
        when(questionService.getQuizQuestion(1L, Difficulty.HARD, List.of(), "Player 1", 100)).thenReturn(quizQuestionDTO);

        // perform the get request
        mockMvc.perform(get("/api/questions/quiz-question")
                        .param("topicId", "1")
                        .param("difficulty", "HARD")
                        .param("excludeIds", "1")
                        .param("playerName", "Player 1")
                        .param("score", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").doesNotExist());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuizQuestion_ShouldReturnNotFound() throws Exception {
        // mock the getQuizQuestion method
        when(questionService.getQuizQuestion(100L, Difficulty.HARD, List.of(), "Player 1", 100)).thenThrow(new RuntimeException("No questions found"));

        // perform the get request
        mockMvc.perform(get("/api/questions/quiz-question")
                        .param("topicId", "100")
                        .param("difficulty", "HARD")
                        .param("excludeIds", "")
                        .param("playerName", "Player 1")
                        .param("score", "100"))
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuizQuestion_ShouldReturnBadRequestMissingDifficulty() throws Exception {
        // perform the get request
        mockMvc.perform(get("/api/questions/quiz-question")
                        .param("topicId", "1"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetQuizQuestion_ShouldReturnBadRequestMissingTopic() throws Exception {
        mockMvc.perform(get("/api/questions/quiz-question")
                        .param("difficulty", "HARD"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCheckCorrectAnswer() throws Exception {
        // build a correct answer DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = QuizCorrectAnswerDTO.builder()
                .answerId(1L)
                .playerName("Player 1")
                .score(100)
                .build();
        // build a correct question DTO
        CorrectQuestionDTO correctQuestionDTO = CorrectQuestionDTO.builder()
                .correct(true)
                .correctAnswerId(1)
                .build();
        // mock the checkCorrectAnswer method
        when(questionService.checkCorrectAnswer(1L, quizCorrectAnswerDTO)).thenReturn(correctQuestionDTO);

        // perform the post request
        mockMvc.perform(post("/api/questions/1/correct")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(quizCorrectAnswerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.correctAnswerId").value("1"));
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCheckCorrectAnswer_WrongAnswer() throws Exception {
        // build a correct answer DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = QuizCorrectAnswerDTO.builder()
                .answerId(2L)
                .playerName("Player 1")
                .score(100)
                .build();
        // build a correct question DTO
        CorrectQuestionDTO correctQuestionDTO = CorrectQuestionDTO.builder()
                .correct(false)
                .correctAnswerId(1)
                .build();
        // mock the checkCorrectAnswer method
        when(questionService.checkCorrectAnswer(1L, quizCorrectAnswerDTO)).thenReturn(correctQuestionDTO);

        // perform the post request
        mockMvc.perform(post("/api/questions/1/correct")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(quizCorrectAnswerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correct").value(false))
                .andExpect(jsonPath("$.correctAnswerId").value("1"));
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCheckCorrectAnswer_ShouldReturnNotFound() throws Exception {
        // build a correct answer DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = QuizCorrectAnswerDTO.builder()
                .answerId(1L)
                .playerName("Player 1")
                .score(100)
                .build();
        // mock the checkCorrectAnswer method
        when(questionService.checkCorrectAnswer(100L, quizCorrectAnswerDTO)).thenThrow(new RuntimeException("Question not found"));

        // perform the post request
        mockMvc.perform(post("/api/questions/100/correct")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(quizCorrectAnswerDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCheckCorrectAnswer_ShouldReturnBadRequestMissingPlayerName() throws Exception {
        // build a correct answer DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = QuizCorrectAnswerDTO.builder()
                .answerId(1L)
                .score(100)
                .build();
        // mock the checkCorrectAnswer method
        when(questionService.checkCorrectAnswer(100L, quizCorrectAnswerDTO)).thenThrow(new RuntimeException("Answer ID and player name must be defined"));

        // perform the post request
        mockMvc.perform(post("/api/questions/100/correct")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(quizCorrectAnswerDTO)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCheckCorrectAnswer_ShouldReturnBadRequestMissingAnswerId() throws Exception {
        // build a correct answer DTO
        QuizCorrectAnswerDTO quizCorrectAnswerDTO = QuizCorrectAnswerDTO.builder()
                .playerName("Player 1")
                .score(100)
                .build();
        // mock the checkCorrectAnswer method
        when(questionService.checkCorrectAnswer(100L, quizCorrectAnswerDTO)).thenThrow(new RuntimeException("Answer ID and player name must be defined"));

        // perform the post request
        mockMvc.perform(post("/api/questions/100/correct")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(quizCorrectAnswerDTO)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUseJoker() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        // build a answerDTO
        QuizAnswerDTO quizAnswerDTO1 = QuizAnswerDTO.builder()
                .id(1L)
                .answer("Answer 1")
                .build();
        QuizAnswerDTO quizAnswerDTO2 = QuizAnswerDTO.builder()
                .id(2L)
                .answer("Answer 2")
                .build();
        // build a questionDTP
        QuizQuestionDTO quizQuestionDTO = QuizQuestionDTO.builder()
                .id(1L)
                .question("Question 1")
                .difficulty(Difficulty.EASY)
                .topic(topic)
                .answers(List.of(quizAnswerDTO1, quizAnswerDTO2))
                .build();
        // mock the getQuizQuestion method
        when(questionService.fiftyFiftyJoker(1L)).thenReturn(quizQuestionDTO);

        // perform the get request
        mockMvc.perform(get("/api/questions/1/joker")
                        .param("joker", "FIFTY_FIFTY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Question 1"))
                .andExpect(jsonPath("$.answers.size()").value(2))
                .andExpect(jsonPath("$.answers[*].answer", hasItem("Answer 1")));
    }

    /**
     * Method to test get quiz question.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUseJoker_ShouldReturnBadRequest() throws Exception {
        // perform the get request
        mockMvc.perform(get("/api/questions/1/joker"))
                .andExpect(status().isBadRequest());
    }
}
