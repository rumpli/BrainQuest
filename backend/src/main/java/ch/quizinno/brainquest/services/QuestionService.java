package ch.quizinno.brainquest.services;

import ch.quizinno.brainquest.dtos.CorrectQuestionDTO;
import ch.quizinno.brainquest.dtos.QuizAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizCorrectAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizQuestionDTO;
import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Highscore;
import ch.quizinno.brainquest.entities.Question;
import ch.quizinno.brainquest.entities.Topic;
import ch.quizinno.brainquest.enums.Difficulty;
import ch.quizinno.brainquest.mappers.QuestionMapper;
import ch.quizinno.brainquest.repositories.QuestionRepository;
import ch.quizinno.brainquest.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service for managing questions.
 */
// Spring annotation to indicate that this class is a service.
@Service
public class QuestionService {

    /**
     * Repository for managing questions.
     */
    private final QuestionRepository questionRepository;
    /**
     * Service for managing topics.
     */
    private final TopicRepository topicRepository;
    /**
     * Service for managing answers.
     */
    private final AnswerService answerService;
    /**
     * Service for managing highscores.
     */
    private final HighscoreService highscoreService;
    /**
     * Mapper for mapping questions to DTOs.
     */
    private final QuestionMapper questionMapper;

    /**
     * Constructs a new QuestionService with the specified repositories and services.
     *
     * @param questionRepository the repository to manage questions
     * @param topicRepository    the repository to manage topics
     * @param answerService      the service to manage answers
     * @param highscoreService   the service to manage highscores
     * @param questionMapper     the mapper to map questions to DTOs
     */
    public QuestionService(QuestionRepository questionRepository, TopicRepository topicRepository,AnswerService answerService, HighscoreService highscoreService, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
        this.answerService = answerService;
        this.highscoreService = highscoreService;
        this.questionMapper = questionMapper;
    }

    /**
     * Retrieves a list of all questions.
     *
     * @return a list of all questions
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Retrieves a question by its ID.
     *
     * @param id the ID of the question to retrieve
     * @return the question with the specified ID
     */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Creates a new question.
     *
     * @param question the question to create
     * @return the created question
     */
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Updates a question.
     *
     * @param id              the ID of the question to update
     * @param questionDetails the details of the question to update
     * @return the updated question
     */
    public Question updateQuestion(Long id, Question questionDetails) {
        // get the question by its ID
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found with id " + id));

        // update the defined question details
        if (questionDetails.getQuestion() != null) {
            question.setQuestion(questionDetails.getQuestion());
        }
        if (questionDetails.getInfo() != null) {
            question.setInfo(questionDetails.getInfo());
        }
        if (questionDetails.getDifficulty() != null) {
            question.setDifficulty(questionDetails.getDifficulty());
        }
        if (questionDetails.getTopic() != null && !questionDetails.getTopic().getId().equals(question.getTopic().getId())) {
            // only update the topic has changed
            question.setTopic(questionDetails.getTopic());
        }

        return questionRepository.save(question);
    }

    /**
     * Deletes a question by its ID.
     *
     * @param id the ID of the question to delete
     */
    public void deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Question not found with id " + id);
        }
    }

    /**
     * Retrieves a list of questions by topic ID.
     *
     * @param topicId the ID of the topic
     * @return a list of questions with the specified topic ID
     */
    public List<Question> getQuestionsByTopicId(Long topicId) {
        return questionRepository.findByTopicId(topicId);
    }

    /**
     * Retrieves a list of questions by topic ID and difficulty.
     *
     * @param topicId    the ID of the topic
     * @param difficulty the difficulty of the questions
     * @return a list of questions with the specified topic ID and difficulty
     */
    public List<Question> getQuestionsByTopicIdAndDifficulty(Long topicId, Difficulty difficulty) {
        return questionRepository.findByTopicIdAndDifficulty(topicId, difficulty);
    }

    /**
     * Retrieves a random question by topic ID and difficulty.
     *
     * @param topicId    the ID of the topic
     * @param difficulty the difficulty of the questions
     * @return a random question with the specified topic ID and difficulty
     */
    public QuizQuestionDTO getQuizQuestion(Long topicId, Difficulty difficulty, List<Long> excludeIds, String playerName, int score) {
        // both topicId and difficulty must be defined
        if (topicId == null || difficulty == null) {
            throw new RuntimeException("Topic ID and difficulty must be defined");
        }

        // get all questions for the given topic and difficulty
        List<Question> questions = this.getQuestionsByTopicIdAndDifficulty(topicId, difficulty);

        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for topic with id " + topicId + " and difficulty " + difficulty);
        }

        // only get valid questions
        // remove questions with less than 4 answers
        questions.removeIf(question -> answerService.getAnswersByQuestion(question).size() < 4);

        // remove questions with IDs that should be excluded
        questions.removeIf(question -> excludeIds.contains(question.getId()));

        if (questions.isEmpty()) {
            // no more questions available
            // write highscore with current score

            // get topic by ID
            Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found with id " + topicId));

            // build highscore object
            Highscore highscore = Highscore.builder()
                    .playerName(playerName)
                    .score(score)
                    .difficulty(difficulty)
                    .topic(topic)
                    .build();
            highscoreService.createHighscore(highscore);
            return null;
        }

        // Select a random question
        Question randomQuestion = questions.get(new Random().nextInt(questions.size()));
        // get all answers for the selected question
        List<Answer> answers = answerService.getAnswersByQuestion(randomQuestion);
        // shuffle the answers
        answers.sort((a, b) -> new Random().nextInt(3) - 1);
        // map answers to QuizAnswerDTO
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .map is used to convert each answer to a QuizAnswerDTO
            .collect is used to collect the stream of QuizAnswerDTOs to a list
         */
        List<QuizAnswerDTO> answerDTOs = answers.stream()
                .map(questionMapper::answerToQuizAnswerDTO)
                .collect(Collectors.toList());

        // map the question and its answers to QuizQuestionDTO
        return questionMapper.questionToQuizQuestionDTO(randomQuestion, answerDTOs);
    }

    /**
     * Check if the given answer is correct.
     *
     * @param id                   the ID of the question
     * @param quizCorrectAnswerDTO the answer to check
     * @return the question with the given answer and if it is correct
     */
    public CorrectQuestionDTO checkCorrectAnswer(Long id, QuizCorrectAnswerDTO quizCorrectAnswerDTO) {
        // all attributes must be defined
        if (quizCorrectAnswerDTO.getAnswerId() == null || quizCorrectAnswerDTO.getPlayerName() == null) {
            throw new RuntimeException("Answer ID and player name must be defined");
        }

        // get question by ID
        Optional<Question> questionOpt = this.getQuestionById(id);

        if (questionOpt.isEmpty()) {
            throw new RuntimeException("Question not found with id " + id);
        }

        // question must not be type Optional<Question>
        Question question = questionOpt.get();
        // get all answers for the selected question
        List<Answer> answers = answerService.getAnswersByQuestion(question);

        // verify the given answerId is in the list of answers for the question
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .noneMatch is used to check if no answer has the given ID
            answer -> is a lambda expression to check if the answer ID is equal to the given answer ID
         */
        if (quizCorrectAnswerDTO.getAnswerId().equals(0L)) {
            // no answer selected since quiz timer expired
            // question is considered as not correct
            // do not throw an exception
        } else if (answers.stream().noneMatch(answer -> answer.getId().equals(quizCorrectAnswerDTO.getAnswerId()))) {
            throw new RuntimeException("Answer not found with id " + quizCorrectAnswerDTO.getAnswerId());
        }

        // get correct answer for the selected question
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .filter is used to filter the answers that are correct
            .findFirst is used to get the first object in the stream
            .orElseThrow is used to throw an exception if no correct answer is found
         */
        Answer correctAnswer = answers.stream()
                .filter(Answer::isCorrect).findFirst()
                .orElseThrow();
        // check if the given answer is correct
        boolean correct = correctAnswer.getId().equals(quizCorrectAnswerDTO.getAnswerId());
        // map the question and its answers to QuizQuestionDTO
        CorrectQuestionDTO correctQuestionDTO = questionMapper.questionToCorrectQuestionDTO(question, correct, correctAnswer);

        // write highscore if answer is not correct
        if (!correct) {
            // build highscore object
            Highscore highscore = Highscore.builder()
                    .playerName(quizCorrectAnswerDTO.getPlayerName())
                    .score(quizCorrectAnswerDTO.getScore())
                    .difficulty(question.getDifficulty())
                    .topic(question.getTopic())
                    .build();
            highscoreService.createHighscore(highscore);
        }

        return correctQuestionDTO;
    }


    /**
     * Use the 50/50 joker to remove two wrong answers from a question.
     *
     * @param id the ID of the question
     * @return a random question with two wrong answers removed
     */
    public QuizQuestionDTO fiftyFiftyJoker(Long id) {
        // get question by ID
        Optional<Question> questionOpt = this.getQuestionById(id);

        if (questionOpt.isEmpty()) {
            throw new RuntimeException("Question not found with id " + id);
        }

        // question must not be type Optional<Question>
        Question question = questionOpt.get();
        // get all answers for the selected question
        List<Answer> answers = answerService.getAnswersByQuestion(question);

        // get correct and a false answer for the selected question
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .filter is used to filter the answers that are correct
            .skip is used to skip a random number of answers that are not correct
            .findFirst is used to get the first object in the stream
            .orElseThrow is used to throw an exception if no correct answer is found
         */
        Answer correctAnswer = answers.stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow();
        Answer falseAnswer = answers.stream()
                .filter(answer -> !answer.isCorrect())
                .skip(new Random().nextInt((int) answers.stream().filter(answer -> !answer.isCorrect()).count()))
                .findFirst()
                .orElseThrow();

        // randomly order the answers
        List<Answer> answerList;
        if (new Random().nextInt(2) == 0) {
            answerList = List.of(correctAnswer, falseAnswer);
        } else {
            answerList = List.of(falseAnswer, correctAnswer);
        }

        // map answers to QuizAnswerDTO
        /*
            .stream is used to convert the list of answers to a stream for further operations
            .map is used to convert each answer to a QuizAnswerDTO
            .collect is used to collect the stream of QuizAnswerDTOs to a list
         */
        List<QuizAnswerDTO> answerDTOs = answerList.stream()
                .map(questionMapper::answerToQuizAnswerDTO)
                .collect(Collectors.toList());
        // map the question and its answers to QuizQuestionDTO

        return questionMapper.questionToQuizQuestionDTO(question, answerDTOs);
    }
}
