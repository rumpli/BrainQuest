package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.repositories.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing answers.
 */
// Spring annotation to indicate that this class is a service.
@Service
public class AnswerService {

    /**
     * Repository for managing answers.
     */
    private final AnswerRepository answerRepository;

    /**
     * Constructs a new AnswerService with the specified AnswerRepository.
     *
     * @param answerRepository the repository to manage answers
     */
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Retrieves a list of all answers.
     *
     * @return a list of all answers
     */
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    /**
     * Retrieves an answer by its ID.
     *
     * @param id the ID of the answer to retrieve
     * @return the answer with the specified ID
     */
    public Optional<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    /**
     * Creates a new answer.
     *
     * @param answer the answer to create
     * @return the created answer
     */
    public Answer createAnswer(Answer answer) {
        // get existing answers for the question
        List<Answer> existingAnswers = answerRepository.findByQuestion(answer.getQuestion());

        // ensure only 4 answers per question
        if (existingAnswers.size() >= 4) {
            throw new RuntimeException("A question can only have 4 answers.");
        }

        // ensure only one correct answer
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .anyMatch is used to check if any of the answers in the list is correct
         */
        if (answer.isCorrect() && existingAnswers.stream().anyMatch(Answer::isCorrect)) {
            throw new RuntimeException("A question can only have one correct answer.");
        }

        // ensure at least one answer is correct
        if (!answer.isCorrect() && existingAnswers.stream().noneMatch(Answer::isCorrect) && existingAnswers.size() == 3) {
            throw new RuntimeException("A question must have at least one correct answer.");
        }

        return answerRepository.save(answer);
    }

    /**
     * Updates an answer.
     *
     * @param id           the ID of the answer to update
     * @param answerDetails the updated answer details
     * @return the updated answer
     */
    public Answer updateAnswer(Long id, Answer answerDetails) {
        // get the answer by its ID
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new RuntimeException("Answer not found with id " + id));
        // get existing answers for the question to check constraints when updating
        List<Answer> existingAnswers = answerRepository.findByQuestion(answer.getQuestion());

        // update the defined answer details
        if (answerDetails.getAnswer() != null) {
            answer.setAnswer(answerDetails.getAnswer());
        }
        answer.setCorrect(answerDetails.isCorrect()); // false if not given
        if (answerDetails.getQuestion() != null && !answerDetails.getQuestion().getId().equals(answer.getQuestion().getId())) {
            // only update the question has changed
            answer.setQuestion(answerDetails.getQuestion());
            // update existing answers for the question since the question has changed
            existingAnswers = answerRepository.findByQuestion(answer.getQuestion());

            // ensure only 4 answers per question
            if (existingAnswers.size() >= 4) {
                throw new RuntimeException("A question can only have 4 answers.");
            }
        }

        // ensure only one correct answer
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .anyMatch is used to check if any of the answers in the list is correct
         */
        if (answer.isCorrect() && existingAnswers.stream().anyMatch(Answer::isCorrect)) {
            throw new RuntimeException("A question can only have one correct answer.");
        }

        return answerRepository.save(answer);
    }

    /**
     * Deletes an answer by its ID.
     *
     * @param id the ID of the answer to delete
     */
    public void deleteAnswer(Long id) {
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Answer not found with id " + id);
        }
    }

    /**
     * Retrieves a list of answers by question.
     *
     * @param question the question to retrieve answers for
     * @return a list of answers by question
     */
    public List<Answer> getAnswersByQuestion(Question question) {
        return answerRepository.findByQuestion(question);
    }
}
