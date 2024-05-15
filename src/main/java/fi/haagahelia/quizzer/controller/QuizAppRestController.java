package fi.haagahelia.quizzer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import fi.haagahelia.quizzer.model.Review;
import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.repository.ReviewRepository;


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

    @Autowired
    private ReviewRepository reviewRepository;
    
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
    public @ResponseBody ResponseEntity<List<Quiz>> getQuizes(
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
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(quizList);
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
    public ResponseEntity<?> getQuizById(@PathVariable("id") Long id) {
        Optional<Quiz> existingQuizOptional = quizRepository.findById(id);

        if (existingQuizOptional.isPresent()) {
            Quiz existingQuiz = existingQuizOptional.get();
            if (!existingQuiz.getPublished()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(existingQuiz);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Quiz with id: " + id + " not found"
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
    public ResponseEntity<Map<String, String>> getAnswerForQuestion(@PathVariable Long questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            String answerText = question.getCorrectAnswer();
            logger.info(answerText);

            Map<String, String> response = new HashMap<>();
            response.put("answerText", question.getCorrectAnswer());
            return ResponseEntity.ok(response);
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
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody AnswerDto answerDto, BindingResult bindingResult,
                                        @PathVariable("questionId") Long questionId){
        if (bindingResult.hasErrors()) {
        List<String> errorMessages = bindingResult.getAllErrors().stream().map((error) -> error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

        Question question = questionRepository.findById(questionId)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question with ID: " + questionId + " not found"));

        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(answerDto.getAnswerText().trim());
        Answer newAnswer = new Answer(answerDto.getAnswerText(), isCorrect, question);
        answerRepository.save(newAnswer);

        Map<String, Object> response = new HashMap<>();
        response.put("correctness", newAnswer.isCorrectness());
        response.put("answerId", newAnswer.getAnswerId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/quiz/{id}/answers")
    public ResponseEntity<List<QuestionAnswerDto>> getAnswersOfQuiz(
            @PathVariable("id") Long quizId,
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
        List<QuestionAnswerDto> statistics = questions.stream().map(question -> {
        List<Answer> answers = answerRepository.findByQuestion(question);
        int totalAnswers = answers.size();
        int correctAnswers = (int) answers.stream().filter(answer -> answer.isCorrectness()).count();
        int wrongAnswers = totalAnswers - correctAnswers;
        double correctPercentage = totalAnswers > 0 ? (100.0 * correctAnswers / totalAnswers) : 0.0;

        return new QuestionAnswerDto(
                question.getQuestionId(),
                question.getQuestionText(),
                question.getCorrectAnswer(),
                question.getDifficultyLevel(),
                totalAnswers,
                correctAnswers,
                wrongAnswers,
                correctPercentage
        );
    }).collect(Collectors.toList());

    return ResponseEntity.ok(statistics);
}
    // Retrieve all reviews associated with a particular quiz
@GetMapping("/quiz/{quizId}/reviews")
public ResponseEntity<List<Review>> getQuizReviews(@PathVariable Long quizId) {
    // Ensure the quiz exists
    Optional<Quiz> quizOptional = quizRepository.findById(quizId);
    if (!quizOptional.isPresent()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found");
    }
    Quiz quiz = quizOptional.get();

    // Retrieve reviews for the quiz
    List<Review> reviews = reviewRepository.findByQuiz(quiz);
        return ResponseEntity.ok(reviews);
    }

 // Add endpoint to create a review
@PostMapping("/quiz/{quizId}/review")
public ResponseEntity<Review> createReview(@PathVariable Long quizId, @RequestBody Review review) {
    // Ensure the quiz exists
    Optional<Quiz> quizOptional = quizRepository.findById(quizId);
    if (!quizOptional.isPresent()) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found");
    }
    Quiz quiz = quizOptional.get();
    // Set the quiz for the review
    review.setQuiz(quiz);
    // Save the review
    Review savedReview = reviewRepository.save(review);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
 }

// Delete Review by id 
@DeleteMapping("/quiz/{quizId}/reviews/{reviewId}")
public ResponseEntity<String> deleteReview(@PathVariable Long quizId, @PathVariable Long reviewId) {
    try {
        // Ensure the quiz exists
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);
        if (!quizOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found");
        }     
        // Get the review by ID
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (!reviewOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review with id " + reviewId + " not found");
        }
        // Ensure the review is associated with the specified quiz
        Review review = reviewOptional.get();
        if (!review.getQuiz().getId().equals(quizId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review with id " + reviewId + " does not belong to quiz with id " + quizId);
        }

        // Delete the review
        reviewRepository.deleteById(reviewId);

        return ResponseEntity.ok("Review with id " + reviewId + " deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting review: " + e.getMessage());
    }
}

// Delete all reviews associated with a particular quiz
@DeleteMapping("/quiz/{quizId}/reviews")
public ResponseEntity<String> deleteQuizReviews(@PathVariable Long quizId) {
    
    Optional<Quiz> quizOptional = quizRepository.findById(quizId);
    if (!quizOptional.isPresent()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with id " + quizId + " not found");
    }
    Quiz quiz = quizOptional.get();
    // Retrieve reviews for the quiz
    List<Review> reviews = reviewRepository.findByQuiz(quiz);
    if (reviews.isEmpty()) {
        return ResponseEntity.ok("No reviews found for quiz with id " + quizId);
    }
    reviewRepository.deleteAll(reviews);
    return ResponseEntity.ok("All reviews for quiz with id " + quizId + " have been deleted");
}

}


