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

@Entity
public class Quizz {
    @Id
    @GeneratedValue
    private Long quizzId;

    // time when the quizz is created
    @CreationTimestamp
    private Instant createdAt;

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
    public Long getQuizzId(){
        return quizzId;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreateAt() {
        return createdAt;
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
    public void setQuizzId(Long quizzId){
        this.quizzId = quizzId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Instant createAt) {
        this.createdAt = createAt;
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
                    + getCreateAt() + " ]";
        } else if (this.status == null) {
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreateAt() + ", Category = " + getCategory() + " ]";
        }
        else if(this.category == null){
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreateAt() + ", Status = " + getStatus() + " ]";
        }
        else{
            return "Quizz = [ Name = " + getName() + ", Description = " + getDescription() + ", Create at ="
                    + getCreateAt() + ", Category = " + getCategory() + ", Status = " + getStatus() + " ]";
        }
    }
}
