package in.scalive.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
	private Long id;
	private String title;
	private String content;
	private String categoryName;
	private Long categoryId;
	private String authorName;
	private Long authorId;
	private LocalDateTime createDateTime;
}
