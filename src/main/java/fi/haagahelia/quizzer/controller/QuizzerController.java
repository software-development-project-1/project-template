package fi.haagahelia.quizzer.controller;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;
import jakarta.persistence.EntityNotFoundException;





@Controller
public class QuizzerController {

    @Autowired
    private QuizzRepository quizzRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // show all quizzes
    @RequestMapping(value = "/quizzlist")
    public String quizzList(Model model) {
        model.addAttribute("quizzlist", quizzRepository.findAll());
        return "quizzlist";
    }

    // edit quizzes
    // add new quiz with creation date - Hong
    @GetMapping(value = "/addquizz")
    public String addQuizz(Model model) {
        // Instant instant = Instant.now();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // String formattedInstant = formatter.format(instant);
        // // Add formatted instant to the model
        // model.addAttribute("formattedInstant", formattedInstant);
        // Add empty Quizz object to the model
        model.addAttribute("quizz", new Quizz());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "addquizz";
    }

    // filter quiz by date - Hong
    @GetMapping("/filterQuizzesByDate")
    public String filterQuizzesByCreationDate(Model model) {
        List<Quizz> quizzes = quizzRepository.findAll();
        // Sort quizzes by creation time in descending order
        Collections.sort(quizzes, Comparator.comparing(Quizz::getCreationTime).reversed());
        model.addAttribute("quizzlist", quizzes); // Use the correct attribute name
        return "quizzlist"; // Return the name of the Thymeleaf template
    }

    // making the editting quizz page
    @GetMapping(value = "/editquizz/{quizzId}")
    public String editQuizForm(@PathVariable("quizzId") Long quizzId, Model model) {
        model.addAttribute("quizz", quizzRepository.findById(quizzId));
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("quizzId", quizzId);
        return "editquizz.html";
    }

    // filter quizz by published
    @GetMapping("/publishedquizz")
    public String filterpublish(Model model) {
        Status status = statusRepository.findByStatus(true);
        List<Quizz> quizzes = quizzRepository.findByStatus(status);
        model.addAttribute("quizzlist", quizzes);
        return "quizzlist";
    }

    // filter quizz by not published
    @GetMapping("/notpublishedquizz")
    public String filternotpublish(Model model) {
        Status status = statusRepository.findByStatus(false);
        List<Quizz> quizzes = quizzRepository.findByStatus(status);
        model.addAttribute("quizzlist", quizzes);
        return "quizzlist";
    }

    // save quizz for add function
    @PostMapping(value = "/savequizz")
    public String save(Quizz quizz) {
        quizzRepository.save(quizz);
        return "redirect:/quizzlist";
    }

    // save quizz for when updating quizz
    // for some reason the quizz object when passed through here lost the creation
    // value but by finding the quizz with the same id we can set back the creation
    // time
    @PostMapping(value = "/updatequizz/{quizzId}")
    public String update(@PathVariable("quizzId") Long quizzId, Quizz quizz) {
        Quizz Quizz = quizzRepository.findById(quizzId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        quizz.setCreatetionTime(Quizz.getCreationTime());
        quizzRepository.save(quizz);
        return "redirect:/quizzlist";
    }

    // delete quizz
    @GetMapping("/deletequizz/{quizzId}")
    public String deleteQuizz(@PathVariable("quizzId") Long quizzId, Model model) {
        quizzRepository.deleteById(quizzId);
        return "redirect:../quizzlist";
    }

    // delete category
    @GetMapping("/deletecategory/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        categoryRepository.deleteById(categoryId);
        return "redirect:../categorylist";
    }
}