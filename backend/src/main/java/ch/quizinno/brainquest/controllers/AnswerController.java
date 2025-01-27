package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.services.AnswerService;
import ch.quizinno.brainquest.services.QuestionService;
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
import java.util.Optional;

/**
 * Controller for managing answers.
 */
// Spring annotation to indicate that this class is a REST controller.
@RestController
// Spring annotation to map HTTP requests to /api/answers.
@RequestMapping("/api/answers")
// Swagger annotation to describe the API endpoints for answers.
@Tag(name = "Answers", description = "Answers Endpoints")
public class AnswerController {

    /**
     * Service for managing answers.
     */
    private final AnswerService answerService;
    /**
     * Service for managing questions.
     */
    private final QuestionService questionService;

    /**
     * Constructs a new AnswerController with the specified AnswerService.
     *
     * @param answerService   the service to manage answers
     * @param questionService the service to manage questions
     */
    public AnswerController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    /**
     * Retrieves all answers.
     *
     * @param questionId the ID of the question to retrieve answers for
     * @return a list of all answers
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping
    // Swagger annotation to describe the API endpoint for getting all answers.
    @Operation(summary = "Get all answers", description = "Retrieve a list of all answers", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting all answers.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all answers",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Answer.class))),
                    }),
    })
    public ResponseEntity<List<Answer>> getAllAnswers(@RequestParam(required = false) Long questionId) {
        if (questionId != null) {
            // return answers by question

            // get question by ID
            Optional<Question> questionOpt = questionService.getQuestionById(questionId);

            if (questionOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // question must not be type Optional<Question>
            return ResponseEntity.ok(answerService.getAnswersByQuestion(questionOpt.get()));
        } else {
            // return all answers
            return ResponseEntity.ok(answerService.getAllAnswers());
        }
    }

    /**
     * Creates a new answer.
     *
     * @param answer the answer to create
     * @return the created answer
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping
    // Swagger annotation to describe the API endpoint for creating a new answer.
    @Operation(summary = "Create a new answer", description = "Add a new answer", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for creating a new answer.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the created answer",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Answer.class))
                    }),
    })
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer) {
        /*
            try
                - If the Answer correctly created, the Answer is returned as a ResponseEntity with status code 200 (OK)
            catch
                - If more than 4 Answer for the Question exists, a ResponseEntity with status code 404 (Not Found) is returned
                - If none of the 4 Answer of a Question is correct, a ResponseEntity with status code 404 (Not Found) is returned
        */
        try {
            return ResponseEntity.status(201).body(answerService.createAnswer(answer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves an answer by its ID.
     *
     * @param id the ID of the answer to retrieve
     * @return the answer with the specified ID
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping("/{id}")
    // Swagger annotation to describe the API endpoint for getting an answer by ID.
    @Operation(summary = "Get a answer by ID", description = "Retrieve a specific answer by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting an answer by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the answer with the specified ID",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Answer.class))
                    }),
    })
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        /*
            .map is used to convert the Optional<Answer> to a ResponseEntity<Answer>
                - If the Optional<Answer> is present, the answer is returned as a ResponseEntity with status code 200 (OK)
            .orElse is used to return the specified value if the Optional<Answer> is empty
                - If the Optional<Answer> is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        return answerService.getAnswerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an answer by its ID.
     *
     * @param id            the ID of the answer to update
     * @param answerDetails the updated answer details
     * @return the updated answer
     */
    // Spring annotation to map HTTP PATCH requests to the method.
    @PatchMapping("/{id}")
    // Swagger annotation to describe the API endpoint for updating an answer.
    @Operation(summary = "Update a answer", description = "Update an existing answer by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for updating an answer.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated answer",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Answer.class))
                    }),
    })
    public ResponseEntity<Answer> updateAnswer(@PathVariable Long id, @RequestBody Answer answerDetails) {
        /*
            try
                - If the Answer is present, the answer is returned as a ResponseEntity with status code 200 (OK)
            catch
                - If the Answer is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        try {
            return ResponseEntity.ok(answerService.updateAnswer(id, answerDetails));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Answer not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    /**
     * Deletes an answer by its ID.
     *
     * @param id the ID of the answer to delete
     * @return a response entity with status code 204 (No Content)
     */
    // Spring annotation to map HTTP DELETE requests to the method.
    @DeleteMapping("/{id}")
    // Swagger annotation to describe the API endpoint for deleting an answer.
    @Operation(summary = "Delete a answer", description = "Remove a answer by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for deleting an answer.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Answer deleted"),
    })
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        /*
            try
                - If the Answer is deleted, a response entity with status code 204 (No Content) is returned
            catch
                - If the Answer is not existing, a response entity with status code 404 (Not Found) is returned
        */
        try {
            answerService.deleteAnswer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Answer not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }
}
