package ch.quizinno.brainquest.mappers;

import ch.quizinno.brainquest.dtos.CorrectQuestionDTO;
import ch.quizinno.brainquest.dtos.QuizAnswerDTO;
import ch.quizinno.brainquest.dtos.QuizQuestionDTO;
import ch.quizinno.brainquest.entities.Answer;
import ch.quizinno.brainquest.entities.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for mapping Question and Answer entities to DTOs.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper {
    /**
     * Maps a Question entity to a QuizQuestionDTO.
     * @param question The Question entity to map.
     * @param answers The List of QuizAnswerDTOs to map.
     * @return The mapped QuizQuestionDTO.
     */
    // map Question and List<QuizAnswerDTO> to QuizQuestionDTO
    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.question", target = "question")
    @Mapping(source = "question.difficulty", target = "difficulty")
    @Mapping(source = "question.topic", target = "topic")
    @Mapping(source = "answers", target = "answers")
    QuizQuestionDTO questionToQuizQuestionDTO(Question question, List<QuizAnswerDTO> answers);

    /**
     * Maps a Answer entity to a QuizAnswerDTO.
     * @param answer The Answer entity to map.
     * @return The mapped QuizAnswerDTO.
     */
    // map Answer to QuizAnswerDTO
    @Mapping(source = "id", target = "id")
    @Mapping(source = "answer", target = "answer")
    QuizAnswerDTO answerToQuizAnswerDTO(Answer answer);

    /**
     * Maps a Question entity to a CorrectQuestionDTO.
     * @param question The Question entity to map.
     * @param correct Whether the question was answered correctly.
     * @param correctAnswer The Answer entity that was the correct answer.
     * @return The mapped CorrectQuestionDTO.
     */
    // map Question and Answer to CorrectQuestionDTO
    @Mapping(source = "question.id", target = "id")
    @Mapping(source = "question.info", target = "info")
    @Mapping(source = "correct", target = "correct")
    @Mapping(source = "correctAnswer.id", target = "correctAnswerId")
    CorrectQuestionDTO questionToCorrectQuestionDTO(Question question, boolean correct, Answer correctAnswer);
}
