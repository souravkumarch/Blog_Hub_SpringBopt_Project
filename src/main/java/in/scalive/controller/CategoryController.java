package in.scalive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.dto.CategoryRequestDTO;
import in.scalive.dto.CategoryResponseDTO;
import in.scalive.dto.CategoryUpdateDTO;
import in.scalive.service.CategoryService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private CategoryService catserv;

	@Autowired
	public CategoryController(CategoryService catserv) {
		this.catserv = catserv;
	}

	@PostMapping
	public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO request) {
		CategoryResponseDTO resp = catserv.createCategory(request);
		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}
    @GetMapping
	public ResponseEntity<List<CategoryResponseDTO>> getAllCategory() {
       return ResponseEntity.ok(catserv.getAllCategory()) ;

	}
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO>  getCategoryById(@PathVariable Long id){
	return 	ResponseEntity.ok(catserv.findById(id));
	}
	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,@RequestBody CategoryUpdateDTO cupdate){
	return ResponseEntity.ok(catserv.updateCategory(id, cupdate));
	
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long id){
		catserv.deleteCategory(id);
		return ResponseEntity.ok("Category deleted succesfully");
	}
}
