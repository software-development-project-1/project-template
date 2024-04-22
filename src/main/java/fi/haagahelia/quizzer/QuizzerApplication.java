package fi.haagahelia.quizzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import fi.haagahelia.quizzer.model.Category;
import fi.haagahelia.quizzer.model.Difficulty;
import fi.haagahelia.quizzer.model.Question;
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
			log.info(category1.toString());
			categoryRepository.save(category2);
			categoryRepository.save(category3);

			// Status data
			Status status1 = new Status(true);
			Status status2 = new Status(false);

			// save example data to status repository
			statusRepository.save(status1);
			log.info(status1.toString());
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
			log.info(quizz1.toString());
			quizzRepository.save(quizz2);
			quizzRepository.save(quizz3);

			// Difficulty example data
			Difficulty difficulty1 = new Difficulty("Easy");
			Difficulty difficulty2 = new Difficulty("Normal");
			Difficulty difficulty3 = new Difficulty("Hard");
			// save example data to db
			difficultyRepository.save(difficulty1);
			log.info(difficulty1.toString());
			difficultyRepository.save(difficulty2);
			difficultyRepository.save(difficulty3);

			// Question example data
			// question about quizz1
			Question question1 = new Question("What is the staple food in most Asian countries?", "Rice", difficulty1,
					quizz1);
			Question question2 = new Question("Which Asian country is famous for its sushi?", "Japan", difficulty1,
					quizz1);
			Question question3 = new Question("What is a common method of cooking in Chinese cuisine?", "Stir-frying",
					difficulty2, quizz1);
			// question about quizz2
			Question question4 = new Question("Which country has won the most FIFA World Cup titles?", "Brazil",
					difficulty2, quizz2);
			Question question5 = new Question("Who holds the record for the most home runs in Major League Baseball?",
					"Barry Bonds", difficulty3, quizz2);
			Question question6 = new Question("What is the distance of a marathon race?", "42 kilometers", difficulty3,
					quizz2);
			// question about quizz3
			Question question7 = new Question(
					"What is the name of the famous war that took place in Vietnam from 1955 to 1975?",
					"The Vietnam War", difficulty3, quizz3);
			Question question8 = new Question("Which foreign powers colonized Vietnam in the 19th and 20th centuries?",
					"France and Japan", difficulty2, quizz3);
			Question question9 = new Question("When did Vietnam gain full independence from France? ",
					"September 2, 1945", difficulty3, quizz3);

			// Saving questions about quizz1
			questionRepository.save(question1);
			log.info(question1.toString());
			questionRepository.save(question2);
			questionRepository.save(question3);

			// Saving questions about quizz2
			questionRepository.save(question4);
			questionRepository.save(question5);
			questionRepository.save(question6);

			// Saving questions about quizz3
			questionRepository.save(question7);
			questionRepository.save(question8);
			questionRepository.save(question9);

		};
	}

}
