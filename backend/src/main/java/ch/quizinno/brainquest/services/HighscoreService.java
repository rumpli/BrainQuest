package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.enums.SortBy;
import ch.quizinno.brainquest.enums.SortDir;
import ch.quizinno.brainquest.repositories.HighscoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing highscores.
 */
// Spring annotation to indicate that this class is a service.
@Service
public class HighscoreService {

    /**
     * Repository for managing highscores.
     */
    private final HighscoreRepository highscoreRepository;

    /**
     * Constructs a new HighscoreService with the specified HighscoreRepository.
     *
     * @param highscoreRepository the repository to manage highscores
     */
    public HighscoreService(HighscoreRepository highscoreRepository) {
        this.highscoreRepository = highscoreRepository;
    }

    /**
     * Retrieves a list of all highscores.
     *
     * @return a list of all highscores
     */
    public List<Highscore> getAllHighscores() {
        return highscoreRepository.findAll();
    }

    /**
     * Retrieves a highscore by its ID.
     *
     * @param id the ID of the highscore to retrieve
     * @return the highscore with the specified ID
     */
    public Optional<Highscore> getHighscoreById(Long id) {
        return highscoreRepository.findById(id);
    }

    /**
     * Creates a new highscore.
     *
     * @param highscore the highscore to create
     * @return the created highscore
     */
    public Highscore createHighscore(Highscore highscore) {
        return highscoreRepository.save(highscore);
    }

    /**
     * Updates a highscore.
     *
     * @param id               the ID of the highscore to update
     * @param highscoreDetails the updated highscore details
     * @return the updated highscore
     */
    public Highscore updateHighscore(Long id, Highscore highscoreDetails) {
        // get the highscore by its ID
        Highscore highscore = highscoreRepository.findById(id).orElseThrow(() -> new RuntimeException("Highscore not found with id " + id));

        // update the defined topic details
        if (highscoreDetails.getPlayerName() != null) {
            highscore.setPlayerName(highscoreDetails.getPlayerName());
        }
        if (highscoreDetails.getScore() != 0) {
            highscore.setScore(highscoreDetails.getScore());
        }
        if (highscoreDetails.getDifficulty() != null) {
            highscore.setDifficulty(highscoreDetails.getDifficulty());
        }
        if (highscoreDetails.getTopic() != null) {
            highscore.setTopic(highscoreDetails.getTopic());
        }

        return highscoreRepository.save(highscore);
    }

    /**
     * Deletes a highscore by its ID.
     *
     * @param id the ID of the highscore to delete
     */
    public void deleteHighscore(Long id) {
        if (highscoreRepository.existsById(id)) {
            highscoreRepository.deleteById(id);
        } else {
            throw new RuntimeException("Highscore not found with id " + id);
        }
    }

    /**
     * Retrieves a list of highscores by topic ID and difficulty.
     *
     * @param topicId    the ID of the topic to retrieve highscores for
     * @param difficulty the difficulty of the highscores to retrieve
     * @return a list of highscores by topic ID and difficulty
     */
    public List<Highscore> getHighscoresByTopicIdAndDifficulty(Long topicId, Difficulty difficulty) {
        return highscoreRepository.findByTopicIdAndDifficulty(topicId, difficulty);
    }

    /**
     * Sorts highscores by the specified direction and field.
     *
     * @param highscores the highscores to sort
     * @param sortDir    the direction to sort the highscores
     * @param sortBy     the field to sort the highscores by
     * @return the sorted highscores
     */
    public List<Highscore> sortHighscores(List<Highscore> highscores, SortDir sortDir, SortBy sortBy) {
        // sort highscores based on the sort direction and field
        /*
            lambda expression to sort the highscores based on the sortBy field and sort direction.
         */
        highscores.sort((h1, h2) -> {
            int comparison;
            // Determine the comparison result based on the sortBy field.
            switch (sortBy) {
                case ID -> comparison = Long.compare(h1.getId(), h2.getId());
                case PLAYER_NAME -> comparison = h1.getPlayerName().compareTo(h2.getPlayerName());
                case SCORE -> comparison = Integer.compare(h1.getScore(), h2.getScore());
                case DIFFICULTY -> comparison = h1.getDifficulty().compareTo(h2.getDifficulty());
                case TOPIC -> comparison = h1.getTopic().getName().compareTo(h2.getTopic().getName());
                default -> throw new IllegalArgumentException("Invalid sortBy value");
            }
            // adjust the comparison result based on the sort direction
            return sortDir == SortDir.ASC ? comparison : -comparison;
        });

        return highscores;
    }

    /**
     * Limits the number of highscores to the specified limit.
     *
     * @param highscores the highscores to limit
     * @param limit      the maximum number of highscores to retrieve
     * @return the limited highscores
     */
    public List<Highscore> limitHighscores(List<Highscore> highscores, Integer limit) {
        // limit highscores
        highscores = highscores.subList(0, Math.min(limit, highscores.size()));
        return highscores;
    }
}
