package fi.haagahelia.quizzer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Category {
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @JsonIgnore 
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Quiz> quizzes;

    @Column(nullable = false, unique=true)
    @NotEmpty(message = "Please provide a category name")
    private String name;

    @Column(nullable = true)
    private String categoryDescription;

    public Category() {
    }

    public Category(List<Quiz> quizzes, String name, String categoryDescription) {
        super();
        this.quizzes = quizzes;
        this.name = name;
        this.categoryDescription = categoryDescription;
    }

    public Category(String name) {
        super();
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    


}
