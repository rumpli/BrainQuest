package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.TopicDTO;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing topics.
 */
// Spring annotation to indicate that this class is a REST controller.
@RestController
// Spring annotation to map HTTP requests to /api/topics.
@RequestMapping("/api/topics")
// Swagger annotation to describe the API endpoints for topics.
@Tag(name = "Topics", description = "Topics Endpoints")
public class TopicController {

    /**
     * Service for managing topics.
     */
    private final TopicService topicService;

    /**
     * Constructs a new TopicController with the specified TopicService.
     *
     * @param topicService the service to manage topics
     */
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * Retrieves a list of all topics.
     *
     * @return a list of all topics
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping
    // Swagger annotation to describe the API endpoint for getting all topics.
    @Operation(summary = "Get all topics", description = "Retrieve a list of all topics with difficulties")
    // Swagger annotation to describe the API response for getting all topics.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all topics with difficulties",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TopicDTO.class)))
                    }),
    })
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    /**
     * Creates a new topic.
     *
     * @param topic the topic to create
     * @return the created topic
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping
    // Swagger annotation to describe the API endpoint for creating a new topic.
    @Operation(summary = "Create a new topic", description = "Add a new topic", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for creating a new topic.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the created topic",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Topic.class))
                    }),
    })
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        return ResponseEntity.status(201).body(topicService.createTopic(topic));
    }

    /**
     * Retrieves a topic by its ID.
     *
     * @param id the ID of the topic to retrieve
     * @return the topic with the specified ID
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping("/{id}")
    // Swagger annotation to describe the API endpoint for getting a topic by ID.
    @Operation(summary = "Get a topic by ID", description = "Retrieve a specific topic by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting a topic by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the topic with the specified ID",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Topic.class))
                    }),
    })
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        /*
            .map is used to convert the Optional<Topic> to a ResponseEntity<Topic>
                - If the Optional<Topic> is present, the topic is returned as a ResponseEntity with status code 200 (OK)
            .orElse is used to return the specified value if the Optional<Topic> is empty
                - If the Optional<Topic> is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        return topicService.getTopicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update a topic by its ID.
     *
     * @param id           the ID of the topic to update
     * @param topicDetails the topic to update
     * @return the updated topic
     */
    // Spring annotation to map HTTP PATCH requests to the method.
    @PatchMapping("/{id}")
    // Swagger annotation to describe the API endpoint for updating a topic by ID.
    @Operation(summary = "Update a topic", description = "Update an existing topic by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for updating a topic by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated topic",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Topic.class))
                    }),
    })
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topicDetails) {
        /*
            try
                - If the Topic is present, the topic is returned as a ResponseEntity with status code 200 (OK)
            catch
                - If the Topic is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        try {
            return ResponseEntity.ok(topicService.updateTopic(id, topicDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a topic by its ID.
     *
     * @param id the ID of the topic to delete
     * @return a response entity with no content
     */
    // Spring annotation to map HTTP DELETE requests to the method.
    @DeleteMapping("/{id}")
    // Swagger annotation to describe the API endpoint for deleting a topic by ID.
    @Operation(summary = "Delete a topic", description = "Remove a topic by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for deleting a topic by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
    })
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        /*
            try
                - If the Topic is deleted, a response entity with status code 204 (No Content) is returned
            catch
                - If the Topic is can not be deleted due to dependencies, a response entity with status code 404 (Not Found) is returned
                - If the Topic is not existing, a response entity with status code 404 (Not Found) is returned
        */
        try {
            topicService.deleteTopic(id);
            // topic deleted
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Topic not found")) {
                // topic not found
                return ResponseEntity.notFound().build();
            } else {
                // topic has dependencies
                return ResponseEntity.badRequest().build();
            }
        }
    }
}
