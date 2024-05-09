package fi.haagahelia.quizzer.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private String username;
    private Integer rating;
    private String review;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

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
