package fi.haagahelia.quizzer.model;



<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;

=======
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
>>>>>>> f7cc09fc4cb5478cde92f10324e991d23916f76c


@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;
<<<<<<< HEAD

    @NotEmpty(message = "Please provide some text")
    @Column(nullable = false)
    private String questionText;
    @NotEmpty(message = "Please provide some text")
    @Column(nullable = false)
    private String correctAnswer;
    @NotEmpty(message = "Please provide some text")
=======
    @NotEmpty(message = "Please provide a question")
    @Column(nullable = false)
    private String questionText;
    @NotEmpty(message = "Please provide a correct answer")
    @Column(nullable = false)
    private String correctAnswer;
    @NotEmpty(message = "Please provide a difficulty level")
>>>>>>> f7cc09fc4cb5478cde92f10324e991d23916f76c
    @Column(nullable = false)
    private String difficultyLevel;

    @ManyToOne
    @JoinColumn(name = "quizId")
    private Quiz quiz;

    public Question() {

    }

    public Question(String questionText, String correctAnswer, String difficultyLevel, Quiz quiz) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.difficultyLevel = difficultyLevel;
        this.quiz = quiz;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "Question [questionId=" + questionId + ", questionText=" + questionText + ", correctAnswer="
                + correctAnswer + ", difficultyLevel=" + difficultyLevel + ", quiz=" + quiz.getId()+ "]";
    }

}
