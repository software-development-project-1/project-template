package fi.haagahelia.quizzer.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fi.haagahelia.quizzer.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.qos.logback.core.model.Model;
import fi.haagahelia.quizzer.dto.CreateMessageDto;
import fi.haagahelia.quizzer.model.Message;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.MessageRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QuizzerRestController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // show all quizzes
    @GetMapping("/quizzlist")
    public List<Quizz> showAllQuizz() {
        return (List<Quizz>) quizzRepository.findAll();
    }

    @GetMapping("/publishedquizz")
    public List<Quizz> getPublishedQuizzNewestToOldest() {

        // Fetch the list of quizzes with a status of true (published)
        Status status = statusRepository.findByStatus(true);
        List<Quizz> publishedQuizzes = quizzRepository.findByStatus(status);

        // Sort the list by creation time in descending order
        Collections.sort(publishedQuizzes, Comparator.comparing(Quizz::getCreationTime).reversed());

        // Return the sorted list
        return publishedQuizzes;
    }
}