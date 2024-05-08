package fi.haagahelia.quizzer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import fi.haagahelia.quizzer.dto.UserRegistrationDto;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.model.AppUser;
import fi.haagahelia.quizzer.repository.AppUserRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;



@Controller
public class UserRegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //show login page
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    //show registration form
    @GetMapping("/registration")
    public String showRegisForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    //save new registered user
    @PostMapping("/saveUser")
    public String addNewUser(@Valid @ModelAttribute("user") UserRegistrationDto userRegistrationDto, BindingResult bindingResult, Model model) {
        logger.info("user registered: ", userRegistrationDto);

        if(bindingResult.hasErrors()){
            return "registration"; 
        }
        if(userRegistrationDto.getPassword().equals(userRegistrationDto.getPasswordCheck()) ==false){
            model.addAttribute("errorMessage", "Password does not match!");
            return "registration";
        }
        AppUser newUser = new AppUser();
        newUser.setUserName(userRegistrationDto.getUsername());
        newUser.setPassword(userRegistrationDto.getPassword());
        if(userRepository.findByUserName(userRegistrationDto.getUsername()) == null){
            userRepository.save(newUser);
            return "redirect:/registration.success";
        }else{
            model.addAttribute("errorMessage", "Username has already existed!");
            return "registration";
        }
    }
}
