package in.scalive.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "name is required")
	private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message="Email should be valid")
	private String email;
	
    @NotBlank(message = "Password is required")
    @Size(min=6,message="Password should be atleast 6 character")
	private String password;
    
    @NotBlank(message = "About is required")
	private String about;
}
