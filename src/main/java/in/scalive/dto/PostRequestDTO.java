package in.scalive.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequestDTO {
	@NotBlank(message = "title required")
	private String title;

	@NotBlank(message = "content required")
	private String content;

	@NotNull(message = "Category is required")
	private Long categoryId;
	
	
	private Long authorId;
}
