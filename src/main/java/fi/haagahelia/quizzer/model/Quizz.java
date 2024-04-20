package fi.haagahelia.quizzer.model;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Quizz {
    @Id
    @GeneratedValue
    private Long quizzId;

    // time when the quizz is created
    @CreationTimestamp
    private Instant creationTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizz")
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "statusId")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    public Quizz() {
    }

    public Quizz(String name, String description, Status status, Category category) {
        super();
        this.name = name;
        this.description = description;
        this.status = status;
        this.category = category;
    }

    // getter
    public Long getQuizzId() {
        return quizzId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public String getCreationTimeFormatted() {
        ZonedDateTime zdt = creationTime.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm");
        return formatter.format(zdt);
    }
    public List<Question> getQuestion() {
        return questions;
    }

    public Status getStatus() {
        return status;
    }

    public Category getCategory() {
        return category;
    }

    // setter
    public void setQuizzId(Long quizzId) {
        this.quizzId = quizzId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatetionTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        if (this.status == null && this.category == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + " ]";
        } else if (this.status == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Category = " + getCategory() + " ]";
        } else if (this.category == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Status = " + getStatus() + " ]";
        } else {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreationTime() + ", Category = " + getCategory() + ", Status = " + getStatus() + " ]";
        }
    }
}
