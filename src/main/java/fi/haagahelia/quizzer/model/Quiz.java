package fi.haagahelia.quizzer.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Quiz {
    @Id
	@GeneratedValue
	private Long id;

    @CreationTimestamp
	private Instant createdAt;

    @Column(nullable = false)
    private String quizName;

    @Column(nullable = false)
    private String quizDescription;

    private Boolean publishStatus;

    public Quiz() {
    }

    public Quiz(Instant createdAt, String quizName, String quizDescription, Boolean publishStatus) {
        this.createdAt = createdAt;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.publishStatus = publishStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Boolean publishStatus) {
        this.publishStatus = publishStatus;
    }

    

}
