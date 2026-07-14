package in.scalive.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorUpdateDTO {
	@Size(min = 1,message = "name should me more than 1")
	 private String name;
	
	@Email(message = "email shoulde be valid")
	 private String email;
	
	@Size(min = 1,message = "About should be more than one")
	 private String about;

}
