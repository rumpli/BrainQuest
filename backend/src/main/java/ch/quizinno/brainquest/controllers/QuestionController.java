package ch.quizinno.brainquest.controllers;

import ch.quizinno.brainquest.dtos.CorrectQuestionDTO;
import ch.quizinno.brainquest.dtos.QuizCorrectAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizQuestionDTO;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.enums.Joker;
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

/**
 * Controller for managing questions.
 */
// Spring annotation to indicate that this class is a REST controller.
@RestController
// Spring annotation to map HTTP requests to /api/questions.
@RequestMapping("/api/questions")
// Swagger annotation to describe the API endpoints for questions.
@Tag(name = "Questions", description = "Questions Endpoints")
public class QuestionController {

    /**
     * Service for managing questions.
     */
    private final QuestionService questionService;

    /**
     * Constructor for the QuestionController.
     *
     * @param questionService the question service
     */
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Retrieves a list of all questions.
     *
     * @param topicId    the ID of the topic
     * @param difficulty the difficulty of the question
     * @return a list of all questions
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping
    // Swagger annotation to describe the API endpoint for getting all questions.
    @Operation(summary = "Get all questions", description = "Retrieve a list of all questions", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting all questions.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all questions",
                    content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Question.class)))
                    }),
    })
    public ResponseEntity<List<Question>> getAllQuestions(@RequestParam(required = false) Long topicId, @RequestParam(required = false) Difficulty difficulty) {
        // return questionService.getAllQuestions();

        if (topicId != null && difficulty != null) {
            // return answers by question
            return ResponseEntity.ok(questionService.getQuestionsByTopicIdAndDifficulty(topicId, difficulty));
        } else if (topicId != null || difficulty != null) {
            // either both topicId and difficulty or none of it must be provided
            return ResponseEntity.badRequest().build();
        } else {
            // return all answers
            return ResponseEntity.ok(questionService.getAllQuestions());
        }
    }

    /**
     * Creates a new question.
     *
     * @param question the question to create
     * @return the created question
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping
    // Swagger annotation to describe the API endpoint for creating a new question.
    @Operation(summary = "Create a new question", description = "Add a new question", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for creating a new question.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return saved question",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))
                    }),
    })
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.status(201).body(questionService.createQuestion(question));
    }

    /**
     * Retrieves a question by ID.
     *
     * @param id the ID of the question
     * @return the question with the specified ID
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping("/{id}")
    // Swagger annotation to describe the API endpoint for getting a question by ID.
    @Operation(summary = "Get a question by ID", description = "Retrieve a specific question by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for getting a question by ID.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the question with the specified ID",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))
                    })
    })
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        /*
            .map is used to convert the Optional<Question> to a ResponseEntity<Question>
                - If the Optional<Question> is present, the question is returned as a ResponseEntity with status code 200 (OK)
            .orElse is used to return the specified value if the Optional<Question> is empty
                - If the Optional<Question> is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates a question.
     *
     * @param id              the ID of the question to update
     * @param questionDetails the details of the question to update
     * @return the updated question
     */
    // Spring annotation to map HTTP PATCH requests to the method.
    @PatchMapping("/{id}")
    // Swagger annotation to describe the API endpoint for updating a question.
    @Operation(summary = "Update a question", description = "Update an existing question by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for updating a question.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated question",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Question.class))
                    })
    })
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        /*
            try
                - If the Question is present, the Question is returned as a ResponseEntity with status code 200 (OK)
            catch
                - If the Question is empty, a ResponseEntity with status code 404 (Not Found) is returned
        */
        try {
            return ResponseEntity.ok(questionService.updateQuestion(id, questionDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a question.
     *
     * @param id the ID of the question to delete
     * @return a response entity with no content
     */
    // Spring annotation to map HTTP DELETE requests to the method.
    @DeleteMapping("/{id}")
    // Swagger annotation to describe the API endpoint for deleting a question.
    @Operation(summary = "Delete a question", description = "Remove a question by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    // Swagger annotation to describe the API response for deleting a question.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
    })
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        /*
            try
                - If the Question is deleted, a response entity with status code 204 (No Content) is returned
            catch
                - If the Question is can not be deleted due to dependencies, a response entity with status code 404 (Not Found) is returned
                - If the Question is not existing, a response entity with status code 404 (Not Found) is returned
        */
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Question not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    /**
     * Retrieves a random quiz question.
     *
     * @param topicId    the ID of the topic
     * @param difficulty the difficulty of the question
     * @param excludeIds the IDs of the questions to exclude
     * @param playerName the name of the player
     * @param score      the score of the player
     * @return a random quiz question
     */
    // Spring annotation to map HTTP GET requests to the method.
    @GetMapping("/quiz-question")
    // Swagger annotation to describe the API endpoint for getting a random quiz question.
    @Operation(summary = "Get a random quiz question", description = "Get a random question to play the game")
    // Swagger annotation to describe the API response for getting a random quiz question.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a random quiz question",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = QuizQuestionDTO.class))
                    }),
    })
    public ResponseEntity<QuizQuestionDTO> getQuizQuestion(@RequestParam Long topicId, @RequestParam Difficulty difficulty, @RequestParam List<Long> excludeIds,
                                                           @RequestParam String playerName, @RequestParam int score) {
        // get all questions for the given topic and difficulty
        try {
            return ResponseEntity.ok(questionService.getQuizQuestion(topicId, difficulty, excludeIds, playerName, score));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No questions found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    /**
     * Checks if the given answer is correct.
     *
     * @param id                   the ID of the question
     * @param quizCorrectAnswerDTO the answer to check
     * @return the correct answer
     */
    // Spring annotation to map HTTP POST requests to the method.
    @PostMapping("/{id}/correct")
    // Swagger annotation to describe the API endpoint for checking the correct answer.
    @Operation(summary = "Check answer", description = "Check if the given answer is correct")
    // Swagger annotation to describe the API response for checking the correct answer.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the correct answer",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CorrectQuestionDTO.class))
                    }),
    })
    public ResponseEntity<CorrectQuestionDTO> checkCorrectAnswer(@PathVariable Long id, @RequestBody QuizCorrectAnswerDTO quizCorrectAnswerDTO) {
        try {
            return ResponseEntity.ok(questionService.checkCorrectAnswer(id, quizCorrectAnswerDTO));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Answer ID and player name must be defined")) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    /**
     * Uses a joker to help get the correct answer.
     *
     * @param id    the ID of the question
     * @param joker the joker to use
     * @return the correct answer
     */
    @GetMapping("/{id}/joker")
    // Swagger annotation to describe the API endpoint for checking the correct answer.
    @Operation(summary = "Use joker", description = "Use a joker to help get the correct answer")
    // Swagger annotation to describe the API response for checking the correct answer.
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the correct answer",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = QuizQuestionDTO.class))
                    }),
    })
    public ResponseEntity<QuizQuestionDTO> useJoker(@PathVariable Long id, @RequestParam Joker joker) {
        // all attributes must be defined
        if (joker == Joker.FIFTY_FIFTY) {
            return ResponseEntity.ok(questionService.fiftyFiftyJoker(id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
