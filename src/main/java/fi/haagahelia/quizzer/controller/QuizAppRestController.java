package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fi.haagahelia.quizzer.dto.QuestionAnswerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;

import fi.haagahelia.quizzer.repository.AnswerRepository;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizRepository;
import groovyjarjarantlr4.v4.parse.ANTLRParser.labeledAlt_return;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.dto.AnswerDto;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.model.Question;


@RestController
@RequestMapping("/api/QuizApp")
@CrossOrigin(origins = "*")
@Tag(name = "Quizzes", description = "Operations for accessing quizzes questions and for collecting and analyzing questions answers")
public class QuizAppRestController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
    
    @Operation(
        summary = "Get all categories",
        description = "Returns all existing saved categories")
    @ApiResponses(value = {
                // The responseCode property defines the HTTP status code of the response
                @ApiResponse(responseCode = "200", description = "Successful operation"),
                @ApiResponse(responseCode = "404", description = "Categories do not exist")
            })
    @GetMapping("/categories")
    public @ResponseBody List<Category> getCategories() {
        List<Category> categoryList = categoryRepository.findAllByOrderByNameAsc();

        if (!categoryList.isEmpty()) {
             return categoryList;
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Categories were not not found"
        );
       
    }

    @Operation(
        summary = "Get all published quizes",
        description = "Returns all quizes where published parameter set as true")
    @ApiResponses(value = {
                // The responseCode property defines the HTTP status code of the response
                @ApiResponse(responseCode = "200", description = "Successful operation"),
                @ApiResponse(responseCode = "404", description = "There is no published quizes")
            })
    // Example of the link with not required published parameter to get non published quizzes
    // http://localhost:8080/api/QuizApp/quizes?published=false
    @GetMapping("/quizes")
    public @ResponseBody List<Quiz> getQuizes(
            @RequestParam(required = false) Boolean published, 
            @RequestParam(required = false) Long categoryId) {
        List<Quiz> quizList;
        if (published == null) {
            quizList = quizRepository.findAllByOrderByQuizNameAsc();
        } else {
            quizList = quizRepository.findByPublishedOrderByCreatedAtDesc(published);
        }

        if (categoryId != null) {
            quizList = quizList.stream()
                .filter(quiz -> {
                    if (quiz.getCategory() != null) {
                        return quiz.getCategory().getId().equals(categoryId);
                    }
                    return false;
                })
                .collect(Collectors.toList());

            // Check if there are quizzes for the selected category
            if (quizList.isEmpty()) {
                return Collections.emptyList();
            }
        }

        return quizList;
}

    @Operation(
        summary = "Get a quiz by id",
        description = "Returns the quiz with the provided id")

		@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Quiz with the provided id does not exist")
})

    @GetMapping("/quiz/{id}")
    public @ResponseBody Quiz getQuizById(@PathVariable("id") Long id) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(id);

        if (existingQuizOptional.isPresent()) {
            Quiz existingQuiz = existingQuizOptional.get();
            return existingQuiz;
        }	

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quiz with id: "+ id + " not found"
        );
    }

    @Operation(
        summary = "Get all questions of the quiz by quiz id",
        description = "Returns all questions related to a special quiz by the provided quiz id")
    @ApiResponses(value = {
                // The responseCode property defines the HTTP status code of the response
                @ApiResponse(responseCode = "200", description = "Successful operation"),
                @ApiResponse(responseCode = "404", description = "Quiz with the provided id does not exist or there are no questions related to this quiz")
            })

    @GetMapping("/quiz/{id}/questions")
    public @ResponseBody List<Question> getQuestionsOfQuiz(@PathVariable("id") Long id) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(id);
        if (existingQuizOptional.isPresent()) {
            Quiz existingQuiz = existingQuizOptional.get();
            List <Question> questionListOfQuiz = questionRepository.findByQuiz(existingQuiz);
            if (!questionListOfQuiz.isEmpty()) {
                return questionListOfQuiz;
            } else {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Questions for Quiz with id: " + id + " were not found"
                );
            }
        }	

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quiz with id: "+ id + " not found"
        );
    }
    @Operation(
        summary = "Get all answers for the question by question id",
        description = "Returns all stored answers related to a special question by the provided question id")

    @ApiResponses(value = {
        // The responseCode property defines the HTTP status code of the response
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Question with the provided id does not exist")
    })
        
    @GetMapping("/questions/{questionId}/answer")
    public ResponseEntity<String> getAnswerForQuestion(@PathVariable Long questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            String answerText = question.getCorrectAnswer();
            return ResponseEntity.ok(answerText);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with ID " + questionId + " not found");
        }
    }


    @Operation(
        summary = "Save answer for the question",
        description = "Stores answers given by users to related question and assigns question id to the answer.")

    @ApiResponses(value = {
        // The responseCode property defines the HTTP status code of the response
        @ApiResponse(responseCode = "200", description = "Answer saved"),
        @ApiResponse(responseCode = "404", description = "Question with the provided id does not exist")
    })
    //Create an answer for a quiz’s question
    @PostMapping("/quiz/{id}/questions/answer")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody AnswerDto answerDto, BindingResult bindingResult,
                                        @PathVariable("id") Long quizId){
        if (bindingResult.hasErrors()) {
        List<String> errorMessages = bindingResult.getAllErrors().stream().map((error) -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

        Optional<Quiz> existingQuizOptional = quizRepository.findById(quizId);
        if (existingQuizOptional.isPresent()) {
            Optional<Question> existingQuestionOptional = questionRepository.findById(answerDto.getQuestionId());
            Question existingQuestion = existingQuestionOptional.get();

            if (existingQuestion != null) {
                Answer newAnswer = new Answer(answerDto.getAnswerText(), existingQuestion);
                answerRepository.save(newAnswer);
                return ResponseEntity.status(HttpStatus.CREATED).body(newAnswer);
            } else {
                throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Answer for Question with id: " + answerDto.getQuestionId() + " was not found"
                );
            }
        }	
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quiz with id: "+ quizId + " not found"
        );
    }

    //getting all answers of a published quiz
//    @GetMapping("/quiz/{id}/answers")
//    public @ResponseBody ResponseEntity<List <AnswerDto>> getAnswersOfQuiz(@PathVariable("id") Long quizId) {
//        Optional<Quiz> existingQuizOptional = quizRepository.findById(quizId);
//        if (!existingQuizOptional.isPresent()) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, "Quiz with id: " + quizId + " is not found"
//            );
//        }
//        Quiz existingQuiz = existingQuizOptional.get();
//        if (!existingQuiz.getPublished()) {
//            throw new ResponseStatusException(
//                HttpStatus.FORBIDDEN, "Quiz with id: " + quizId + " is not published"
//            );
//        }
//
//        List <Question> questions = questionRepository.findByQuizId(quizId);
//        logger.info("Questions fetched: {}", questions);
//        List <Answer> answers = answerRepository.findByQuestionIn(questions);
//        logger.info("Answers fetched: {}", answers);
//        if (answers.isEmpty()) {
//            return ResponseEntity.ok(Collections.emptyList()); // If no answers, return empty list
//        }
//
//        List <AnswerDto> answerDtos = answers.stream()
//            .map(answer -> new AnswerDto(answer.getQuestion().getCorrectAnswer(), answer.getQuestion().getQuestionId()))
//            .collect(Collectors.toList());
//
//        return ResponseEntity.ok(answerDtos);
//    }
    // Get all questions in a quiz, displaying its questionText, answer, id, difficulty
    @GetMapping("/{quizId}/answers")
    public ResponseEntity<List<QuestionAnswerDto>> getAnswersOfQuiz(
            @PathVariable("quizId") Long quizId,
            @RequestParam(name = "difficultyLevel", required = false) String difficultyLevel
            ) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (!quizOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found");
        }
        Quiz quiz = quizOptional.get();
        List<Question> questions;
        if (difficultyLevel != null) {
            questions = questionRepository.findByQuiz(quiz).stream()
                    .filter(question -> question.getDifficultyLevel().equalsIgnoreCase(difficultyLevel))
                    .collect(Collectors.toList());
        } else {
            questions = questionRepository.findByQuiz(quiz);
        }
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No questions found for quiz with id " + quizId);
        }
        List<QuestionAnswerDto> questionAnswerDtos = new ArrayList<>();
        for (Question question : questions) {
            QuestionAnswerDto dto = new QuestionAnswerDto(
                    question.getQuestionId(),
                    question.getQuestionText(),
                    question.getCorrectAnswer(),
                    question.getDifficultyLevel()
            );
            questionAnswerDtos.add(dto);
        }
        return ResponseEntity.ok(questionAnswerDtos);
    }
    
}
