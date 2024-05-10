package fi.haagahelia.quizzer.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    //@NotEmpty(message = "Please provide a username")
    //@Length(min = 3, message = "Username must be at least 3 characters")
   // @Column(nullable = false)
    private String username;
    //@NotEmpty(message = "Please provide a rating from 1 to 5")
    private Integer rating;
   // @NotEmpty(message = "Please provide a review")
    //@Length(min = 3, message = "Review must be at least 3 characters")
   // @Column(nullable = false)
    private String review;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @CreationTimestamp
    private Instant createdAt;

    public Review(String username, Integer rating, String review, Quiz quiz, Instant createdAt) {
        this.username = username;
        this.rating = rating;
        this.review = review;
        this.quiz = quiz;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + reviewId +
                ", username='" + username + '\'' +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", quiz=" + quiz +
                ", createdAt=" + createdAt +
                '}';
    }
}
