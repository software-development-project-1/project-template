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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

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
}
