package fi.haagahelia.quizzer.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupForm {
	@NotEmpty
	@Size(min=3, max=30)
	private String username = "";

	@NotEmpty
    @Size(min=1, max=30)
    private String firstname = "";

	@NotEmpty
    @Size(min=1, max=30)
    private String lastname = "";
	
	@NotEmpty
	@Size(min=9, max=30)
	private String password = "";
	
	@NotEmpty
	@Size(min=9, max=30)
	private String passwordCheck = "";
	
	@NotEmpty
	private String role = "TEACHER";

}

