package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.TopicDTO;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.services.TopicService;
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
 * Test class for TopicController.
 */
// Specifies the controller to be tested.
@WebMvcTest(TopicController.class)
// Ignore the security configuration for the test.
@AutoConfigureMockMvc(addFilters = false)
public class TopicControllerTest {
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
    private TopicService topicService;

    /**
     * Method for testing getting all topics.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAllTopics() throws Exception {
        // build a topic DTO
        TopicDTO topicDTO = TopicDTO.builder()
                .name("Topic 1")
                .description("Description 1")
                .build();
        // mock the getAllTopics method
        when(topicService.getAllTopics()).thenReturn(List.of(topicDTO));

        // perform the get request
        mockMvc.perform(get("/api/topics")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Topic 1"));
    }

    /**
     * Method for testing creating a topic.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateTopic() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .name("Topic 1")
                .description("Description 1")
                .build();
        // mock the createTopic method
        when(topicService.createTopic(topic)).thenReturn(topic);

        // perform the post request
        mockMvc.perform(post("/api/topics")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(topic)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Topic 1"));
    }

    /**
     * Method for testing getting a topic by id.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetTopicById() throws Exception {
        Topic topic = Topic.builder()
                .name("Topic 1")
                .description("Description 1")
                .build();
        // mock the getTopicById method
        when(topicService.getTopicById(1L)).thenReturn(Optional.ofNullable(topic));

        // perform the get request
        mockMvc.perform(get("/api/topics/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Topic 1"));
    }

    /**
     * Method for testing getting a topic by id.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetTopicById_ShouldReturnNotFound() throws Exception {
        // mock the getTopicById method
        when(topicService.getTopicById(100L)).thenReturn(Optional.empty());

        // perform the get request
        mockMvc.perform(get("/api/topics/100")
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method for testing updating a topic.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testUpdateTopic() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .name("Topic 1 updated")
                .description("Description 1 updated")
                .build();

        // mock the updateTopic method
        when(topicService.updateTopic(1L, topic)).thenReturn(topic);

        // perform the patch request
        mockMvc.perform(patch("/api/topics/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topic))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Topic 1 updated"));
    }

    /**
     * Method for testing updating a topic.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testUpdateTopic_ShouldReturnNotFound() throws Exception {
        // build a topic
        Topic topic = Topic.builder()
                .name("Topic 100 updated")
                .description("Description 100 updated")
                .build();
        // mock the updateTopic method
        when(topicService.updateTopic(100L, topic)).thenThrow(new RuntimeException("Topic not found"));

        // perform the patch request
        mockMvc.perform(patch("/api/topics/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topic))
                )
                .andExpect(status().isNotFound());
    }

    /**
     * Method for testing deletion of users.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDeleteTopic() throws Exception {
        // mock the deleteTopic method
        doNothing().when(topicService).deleteTopic(1L);

        // perform the delete request
        mockMvc.perform(delete("/api/topics/1")
                )
                .andExpect(status().isNoContent());
    }

    /**
     * Method for testing deletion of users.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDeleteTopic_ShouldReturnNotFound() throws Exception {
        // mock the deleteTopic method
        doThrow(new RuntimeException("Topic not found")).when(topicService).deleteTopic(100L);

        // perform the delete request
        mockMvc.perform(delete("/api/topics/100")
                )
                .andExpect(status().isNotFound());
    }
}