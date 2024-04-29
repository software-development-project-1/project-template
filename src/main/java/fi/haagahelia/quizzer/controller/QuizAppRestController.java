package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Example of the link with not required published parameter to get non published quizzes
    // http://localhost:8080/api/QuizApp/quizes?published=false
    @GetMapping("/quizes")
    public @ResponseBody List<Quiz> getQuizes(@RequestParam(required = false) Boolean published) {
        List<Quiz> quizList;
        if (published == null) {
            quizList = quizRepository.findAllByOrderByQuizNameAsc();
        } else {
            quizList = quizRepository.findByPublishedOrderByCreatedAtDesc(published);
        }

        if (!quizList.isEmpty()) {
            return quizList;
        }

        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Quizes were not not found"
        );
    }

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
    @GetMapping("/quiz/{id}/answers")
    public @ResponseBody ResponseEntity<List <AnswerDto>> getAnswersOfQuiz(@PathVariable("id") Long quizId) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(quizId);
        
        if (!existingQuizOptional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Quiz with id: " + quizId + " is not found"
            );
        }	
        
        Quiz existingQuiz = existingQuizOptional.get(); 
        if (!existingQuiz.getPublished()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Quiz with id: " + quizId + " is not published"
            );
        } 

        List <Question> questions = questionRepository.findByQuizId(quizId);
        logger.info("Questions fetched: {}", questions);
        List <Answer> answers = answerRepository.findByQuestionIn(questions);
        logger.info("Answers fetched: {}", answers);
        if (answers.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // If no answers, return empty list
        }

        List <AnswerDto> answerDtos = answers.stream()
            .map(answer -> new AnswerDto(answer.getQuestion().getCorrectAnswer(), answer.getQuestion().getQuestionId()))
            .collect(Collectors.toList());

        return ResponseEntity.ok(answerDtos);
    }

}
