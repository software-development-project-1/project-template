package fi.haagahelia.quizzer.model;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;


@Entity
public class Quiz {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @CreationTimestamp
	private Instant createdAt;
    
    @NotEmpty(message = "Please provide a quiz name")
    @Column(nullable = false)
    private String quizName;

    @NotEmpty(message = "Please provide a quiz description")
    @Column(nullable = false)
    private String quizDescription;

    private Boolean published;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Quiz() {
    }

    public Quiz(Category category, Instant createdAt, String quizName, String quizDescription, Boolean published, User user) {
        super();
        this.category = category;
        this.createdAt = createdAt;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.published = published;
        this.user= user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public Boolean getPublished() {
        return published;
    }

    public String getPublishedDisplay() {
        return published ? "Published" : "Not Published";
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
