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
import fi.haagahelia.quizzer.model.SignupForm;
import fi.haagahelia.quizzer.model.AppUser;
import fi.haagahelia.quizzer.repository.AppUserRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;



@Controller
public class UserRegistrationController {
    @Autowired
	private AppUserRepository repository;

    @Autowired
	private PasswordEncoder passwordEncoder;

	// Signup
	@RequestMapping(value = "/registration")
	public String addUser(Model model) {
		model.addAttribute("signupform", new SignupForm());
		return "registration";
	}

	// Create new user
	@RequestMapping(value = "/saveuser", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
		if (!bindingResult.hasErrors()) {
			if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
				// String pswd = signupForm.getPassword();
				// BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
				// String hashPswd = bc.encode(pswd);

				AppUser newUser = new AppUser();
				newUser.setPassword(passwordEncoder.encode(signupForm.getPassword()));
				newUser.setUserName(signupForm.getUsername());
				newUser.setRole("TEACHER");
				newUser.setFirstName("Maksim");
				newUser.setLastName("Minenko");
				// Check if user already exists
				if (repository.findByUserName(signupForm.getUsername()) == null) {
					repository.save(newUser);
				} else {
					bindingResult.rejectValue("username", "err.username", "Username already exists");
					return "registration";
				}
			} else {
				bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
				return "registration";
			}
		} else {
			return "registration";
		}
		return "redirect:/";
	}

}
