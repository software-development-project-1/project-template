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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Quiz {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = true)
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

    public Quiz(Category category, Instant createdAt, String quizName, String quizDescription, Boolean published, User user) {
        super();
        this.category = category;
        this.createdAt = createdAt;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.published = published;
        this.user= user;
    }

    public String getPublishedDisplay() {
        return published ? "Published" : "Not Published";
    }
}
