package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.enums.SortBy;
import ch.quizinno.brainquest.enums.SortDir;
import ch.quizinno.brainquest.services.HighscoreService;
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
 * Controller for managing highscores.
 */
// Spring annotation to indicate that this class is a REST controller.
@RestController
// Spring annotation to map HTTP requests to /api/highscores.
@RequestMapping("/api/highscores")
// Swagger annotation to describe the API endpoints for highscores.
@Tag(name = "Highscores", description = "Highscores Endpoints")
public class HighscoreController {

    /**
     * Service for managing highscores.
     */
    private final HighscoreService highscoreService;

    /**
     * Constructs a new HighscoreController with the specified HighscoreService.
     *
     * @param highscoreService the service to manage highscores
     */
    public HighscoreController(HighscoreService highscoreService) {
        this.highscoreService = highscoreService;
    }

    /**
     * Retrieves a list of all highscores.
     *
     * @param topicId    the ID of the topic to filter by
     * @param difficulty the difficulty to filter by
     * @param sortDir    the direction to sort by
     * @param sortBy     the field to sort by
     * @param limit      the maximum number of highscores to retrieve
     * @return a list of all highscores
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping
    // Swagger annotation to describe the API endpoint for getting all highscores.
    @Operation(summary = "Get all highscores", description = "Retrieve a list of all highscores")
    // Swagger annotation to describe the API response for getting all highscores.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all highscores",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Highscore.class))),
                    }),
    })
    public ResponseEntity<List<Highscore>> getAllHighscores(@RequestParam(required = false) Long topicId, @RequestParam(required = false) Difficulty difficulty,
                                                            @RequestParam(required = false, defaultValue = "ID") SortBy sortBy,
                                                            @RequestParam(required = false, defaultValue = "ASC") SortDir sortDir,
                                                            @RequestParam(required = false) Integer limit) {
        // initialize return variable
        List<Highscore> highscores;

        if (topicId != null && difficulty != null) {
            // return answers by question
            highscores = highscoreService.getHighscoresByTopicIdAndDifficulty(topicId, difficulty);
        } else if (topicId != null || difficulty != null) {
            // either both topicId and difficulty or none of it must be provided
            return ResponseEntity.badRequest().build();
        } else {
            // get all highscores
            highscores = highscoreService.getAllHighscores();
        }

        // sort highscores
        highscores = highscoreService.sortHighscores(highscores, sortDir, sortBy);

        if (limit != null) {
            // limit highscores
            highscores = highscoreService.limitHighscores(highscores, limit);
        }

        return ResponseEntity.ok(highscores);
    }

    /**
     * Creates a new highscore.
     *
     * @param highscore the highscore to create
     * @return the created highscore
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping
    // Swagger annotation to describe the API endpoint for creating a new highscore.
    @Operation(summary = "Create a new highscore", description = "Add a new highscore", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for creating a new highscore.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the created highscore",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Highscore.class))
                    }),
    })
    public ResponseEntity<Highscore> createHighscore(@RequestBody Highscore highscore) {
        return ResponseEntity.status(201).body(highscoreService.createHighscore(highscore));
    }

    /**
     * Retrieves a highscore by its ID.
     *
     * @param id the ID of the highscore to retrieve
     * @return the highscore with the specified ID
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping("/{id}")
    // Swagger annotation to describe the API endpoint for getting a highscore by ID.
    @Operation(summary = "Get a highscore by ID", description = "Retrieve a specific highscore by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting a highscore by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the highscore with the specified ID",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Highscore.class))
                    }),
    })
    public ResponseEntity<Highscore> getHighscoreById(@PathVariable Long id) {
        /*
            .map is used to convert the Optional<Highscore> to a ResponseEntity<Highscore>
                - If the Optional<Highscore> is present, the highscore is returned as a ResponseEntity with status code 200 (OK)
            .orElse is used to return the specified value if the Optional<Highscore> is empty
                - If the Optional<Highscore> is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        return highscoreService.getHighscoreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a highscore by its ID.
     *
     * @param id               the ID of the highscore to update
     * @param highscoreDetails the details of the highscore to update
     * @return the updated highscore
     */
    // Spring annotation to map HTTP PATCH requests to the method.
    @PatchMapping("/{id}")
    // Swagger annotation to describe the API endpoint for updating a highscore by ID.
    @Operation(summary = "Update a highscore", description = "Update an existing highscore by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for updating a highscore by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated highscore",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Highscore.class))
                    }),
    })
    public ResponseEntity<Highscore> updateHighscore(@PathVariable Long id, @RequestBody Highscore highscoreDetails) {
        /*
            try
                - If the Highscore is present, the topic is returned as a ResponseEntity with status code 200 (OK)
            catch
                - If the Highscore is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        try {
            return ResponseEntity.ok(highscoreService.updateHighscore(id, highscoreDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a highscore by its ID.
     *
     * @param id the ID of the highscore to delete
     * @return a response entity with no content
     */
    // Spring annotation to map HTTP DELETE requests to the method.
    @DeleteMapping("/{id}")
    // Swagger annotation to describe the API endpoint for deleting a highscore by ID.
    @Operation(summary = "Delete a highscore", description = "Remove a highscore by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for deleting a highscore by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Highscore deleted"),
    })
    public ResponseEntity<Void> deleteHighscore(@PathVariable Long id) {
        /*
            try
                - If the Highscore is deleted, a response entity with status code 204 (No Content) is returned
            catch
                - If the Highscore is not existing, a response entity with status code 404 (Not Found) is returned
        */
        try {
            highscoreService.deleteHighscore(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Highscore not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }
}