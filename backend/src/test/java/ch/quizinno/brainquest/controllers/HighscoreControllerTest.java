package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.enums.SortBy;
import ch.quizinno.brainquest.enums.SortDir;
import ch.quizinno.brainquest.services.HighscoreService;
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
 * Class to test the HighscoreController.
 */
// Specifies the controller to be tested.
@WebMvcTest(HighscoreController.class)
// Ignore the security configuration for the test.
@AutoConfigureMockMvc(addFilters = false)
public class HighscoreControllerTest {
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
    private HighscoreService highscoreService;

    /**
     * Method to test the get all highscores.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetAllHighscores() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();

        // mock the get all highscores method
        when(highscoreService.getAllHighscores()).thenReturn(List.of(highscore));
        // mock the getHighscoresByTopicIdAndDifficulty method
        when(highscoreService.getHighscoresByTopicIdAndDifficulty(1L, Difficulty.EASY)).thenReturn(List.of(highscore));
        // mock the sortHighscores method
        when(highscoreService.sortHighscores(List.of(highscore), SortDir.ASC, SortBy.ID)).thenReturn(List.of(highscore));
        // mock the limitHighscores method
        when(highscoreService.limitHighscores(List.of(highscore), null)).thenReturn(List.of(highscore));

        // perform the get request
        mockMvc.perform(get("/api/highscores")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].playerName").value("Player 1"))
                .andExpect(jsonPath("$.[0].score").value(100))
                .andExpect(jsonPath("$.[0].topic.name").value("Topic 1"));
    }

    /**
     * Method to test the get highscores by topic ID and difficulty.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetHighscoresByTopicIdAndDifficulty() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();

        // mock the get all highscores method
        when(highscoreService.getAllHighscores()).thenReturn(List.of(highscore));
        // mock the getHighscoresByTopicIdAndDifficulty method
        when(highscoreService.getHighscoresByTopicIdAndDifficulty(1L, Difficulty.EASY)).thenReturn(List.of(highscore));
        // mock the sortHighscores method
        when(highscoreService.sortHighscores(List.of(highscore), SortDir.ASC, SortBy.ID)).thenReturn(List.of(highscore));
        // mock the limitHighscores method
        when(highscoreService.limitHighscores(List.of(highscore), null)).thenReturn(List.of(highscore));

        // perform the get request
        mockMvc.perform(get("/api/highscores")
                        .param("topicId", "1")
                        .param("difficulty", "EASY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].playerName").value("Player 1"))
                .andExpect(jsonPath("$.[0].score").value(100))
                .andExpect(jsonPath("$.[0].topic.name").value("Topic 1"));
    }

    /**
     * Method to test the get highscores by topic ID and difficulty.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetHighscoresWithLimit() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();

        // mock the get all highscores method
        when(highscoreService.getAllHighscores()).thenReturn(List.of(highscore));
        // mock the getHighscoresByTopicIdAndDifficulty method
        when(highscoreService.getHighscoresByTopicIdAndDifficulty(1L, Difficulty.EASY)).thenReturn(List.of(highscore));
        // mock the sortHighscores method
        when(highscoreService.sortHighscores(List.of(highscore), SortDir.ASC, SortBy.ID)).thenReturn(List.of(highscore));
        // mock the limitHighscores method
        when(highscoreService.limitHighscores(List.of(highscore), 1)).thenReturn(List.of(highscore));

        // perform the get request
        mockMvc.perform(get("/api/highscores")
                        .param("limit", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].playerName").value("Player 1"))
                .andExpect(jsonPath("$.[0].score").value(100))
                .andExpect(jsonPath("$.[0].topic.name").value("Topic 1"));
    }

    /**
     * Method to test the get highscores by topic ID and difficulty.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetHighscoresWithSortBy() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();

        // mock the get all highscores method
        when(highscoreService.getAllHighscores()).thenReturn(List.of(highscore));
        // mock the getHighscoresByTopicIdAndDifficulty method
        when(highscoreService.getHighscoresByTopicIdAndDifficulty(1L, Difficulty.EASY)).thenReturn(List.of(highscore));
        // mock the sortHighscores method
        when(highscoreService.sortHighscores(List.of(highscore), SortDir.ASC, SortBy.SCORE)).thenReturn(List.of(highscore));
        // mock the limitHighscores method
        when(highscoreService.limitHighscores(List.of(highscore), null)).thenReturn(List.of(highscore));

        // perform the get request
        mockMvc.perform(get("/api/highscores")
                        .param("sortBy", "SCORE")
                        .param("sortDir", "ASC")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].playerName").value("Player 1"))
                .andExpect(jsonPath("$.[0].score").value(100))
                .andExpect(jsonPath("$.[0].topic.name").value("Topic 1"));
    }

    /**
     * Method to test the create highscore.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testCreateHighscore() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();

        // mock the createHighscore method
        when(highscoreService.createHighscore(highscore)).thenReturn(highscore);

        // perform the post request
        mockMvc.perform(post("/api/highscores")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(highscore))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerName").value("Player 1"))
                .andExpect(jsonPath("$.score").value(100))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"));
    }

    /**
     * Method to test the get highscore by ID.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetHighscoreById() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();
        // mock the getHighscoreById method
        when(highscoreService.getHighscoreById(1L)).thenReturn(Optional.ofNullable(highscore));

        // perform the get request
        mockMvc.perform(get("/api/highscores/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("Player 1"))
                .andExpect(jsonPath("$.score").value(100))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"));
    }

    /**
     * Method to test the get highscore by ID.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testGetHighscoreById_ShouldReturnNotFound() throws Exception {
        // mock the getHighscoreById method
        when(highscoreService.getHighscoreById(100L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/highscores/100")
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test the update highscore.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateHighscore() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1 updated")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();
        // mock the updateHighscore method
        when(highscoreService.updateHighscore(1L, highscore)).thenReturn(highscore);

        // perform the patch request
        mockMvc.perform(patch("/api/highscores/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(highscore))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("Player 1 updated"))
                .andExpect(jsonPath("$.score").value(100))
                .andExpect(jsonPath("$.topic.name").value("Topic 1"));
    }

    /**
     * Method to test the update highscore.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testUpdateHighscore_ShouldReturnNotFound() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .id(1L)
                .name("Topic 1")
                .description("Description 1")
                .build();
        Highscore highscore = Highscore.builder()
                .id(1L)
                .playerName("Player 1 updated")
                .difficulty(Difficulty.EASY)
                .score(100)
                .topic(topic)
                .build();
        // mock the updateHighscore method
        when(highscoreService.updateHighscore(100L, highscore)).thenThrow(new RuntimeException(""));

        // perform the patch request
        mockMvc.perform(patch("/api/highscores/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(highscore))
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method to test the delete highscore.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteHighscore() throws Exception {
        // mock the deleteAnswer method
        doNothing().when(highscoreService).deleteHighscore(1L);

        // perform the delete request
        mockMvc.perform(delete("/api/highscores/1")
                )
                .andExpect(status().isNoContent());
    }

    /**
     * Method to delete users for testing.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testDeleteHighscore_ShouldReturnNotFound() throws Exception {
        // mock the deleteTopic method
        doThrow(new RuntimeException("Highscore not found")).when(highscoreService).deleteHighscore(100L);

        // perform the delete request
        mockMvc.perform(delete("/api/highscores/100")
                )
                .andExpect(status().isNotFound());
    }

}
