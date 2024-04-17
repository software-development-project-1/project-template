package fi.haagahelia.quizzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.haagahelia.quizzer.controller.QuizzerController;
import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Difficulty;
import fi.haagahelia.quizzer.model.Quizz;
import fi.haagahelia.quizzer.model.Status;
import fi.haagahelia.quizzer.repository.CategoryRepository;
import fi.haagahelia.quizzer.repository.DifficultyRepository;
import fi.haagahelia.quizzer.repository.QuestionRepository;
import fi.haagahelia.quizzer.repository.QuizzRepository;
import fi.haagahelia.quizzer.repository.StatusRepository;

@SpringBootApplication
public class QuizzerApplication {
	private static final Logger log = LoggerFactory.getLogger(QuizzerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(QuizzerApplication.class, args);
	}

	@Bean
	public CommandLineRunner DBlinerunner(CategoryRepository categoryRepository,
			DifficultyRepository difficultyRepository, QuestionRepository questionRepository,
			QuizzRepository quizzRepository, StatusRepository statusRepository) {
		return (args) -> {
			log.info("save a couple of quizz");

			// Category example data
			Category category1 = new Category("History", "Knowledge about world's history");
			Category category2 = new Category("Culinary", "Knowledge about world's culinary");
			Category category3 = new Category("Sports", "Knowledge about world's sport");

			// save example data to category repository
			categoryRepository.save(category1);
			categoryRepository.save(category2);
			categoryRepository.save(category3);

			// Status data
			Status status1 = new Status(true);
			Status status2 = new Status(false);

			// save example data to status repository
			statusRepository.save(status1);
			statusRepository.save(status2);

			// Quiz example data
			Quizz quizz1 = new Quizz("Asian Food and Cuisine",
					"A delicious exploration of Asian cuisines and gastronomic knowledge. Perfect for foodies!",
					status1, category2);
			Quizz quizz2 = new Quizz("World Sports Trivia",
					"A challenging quiz about various sports around the world. Perfect for sports enthusiasts!",
					status1, category3);
			Quizz quizz3 = new Quizz("History of Vietnam",
					"A comprehensive quiz about the rich and diverse history of Vietnam. Perfect for history buffs!",
					status2, category1);

			// save example data to the db
			quizzRepository.save(quizz1);
			quizzRepository.save(quizz2);
			quizzRepository.save(quizz3);

			// Difficulty example data
			Difficulty difficulty1 = new Difficulty("Easy");
			Difficulty difficulty2 = new Difficulty("Noemal");
			Difficulty difficulty3 = new Difficulty("Hard");
			// save example data to db
			difficultyRepository.save(difficulty1);
			difficultyRepository.save(difficulty2);
			difficultyRepository.save(difficulty3);

		};
	}

}
