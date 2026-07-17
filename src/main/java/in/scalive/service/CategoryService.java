package in.scalive.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.scalive.dto.CategoryRequestDTO;
import in.scalive.dto.CategoryResponseDTO;
import in.scalive.dto.CategoryUpdateDTO;
import in.scalive.dto.PostUpdateDTO;
import in.scalive.entity.Category;
import in.scalive.exception.ResourceAlreadyExistException;
import in.scalive.exception.ResourceNotFoundException;
import in.scalive.repository.CategoryRepository;

@Service
public class CategoryService {

	private CategoryRepository catrepo;

	@Autowired
	public CategoryService(CategoryRepository catrepo) {

		this.catrepo = catrepo;
	}

	public CategoryResponseDTO createCategory(CategoryRequestDTO crdto) {
		if (catrepo.existsByCatName(crdto.getCatname())) {
			throw new ResourceAlreadyExistException("this category is already exists");
		}
		Category category = new Category();
		category.setCatName(crdto.getCatname());
		category.setDescription(crdto.getCatdesc());

		Category updatedCat = catrepo.save(category);
		return new CategoryResponseDTO(updatedCat.getId(), updatedCat.getCatName(), updatedCat.getDescription());

	}

	public List<CategoryResponseDTO> getAllCategory() {

		List<Category> Catdtolist = catrepo.findAll();
		List<CategoryResponseDTO> resplist = new ArrayList<>();
		for (Category cat : Catdtolist) {
			CategoryResponseDTO resp = new CategoryResponseDTO();
			resp.setId(cat.getId());
			resp.setCatname(cat.getCatName());
			resp.setCatdesc(cat.getDescription());
			resplist.add(resp);
		}
		return resplist;
	}

	public CategoryResponseDTO findById(Long id) {
		Category category = catrepo.findById(id).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("category id is not available " + id);
		}

		CategoryResponseDTO resp = new CategoryResponseDTO();
		resp.setId(category.getId());
		resp.setCatname(category.getCatName());
		resp.setCatdesc(category.getDescription());
		return resp;
	}

	public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO pudto) {
		Category category = catrepo.findById(id).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("category id is not available to upadate " + id);
		}
		if (pudto == null || (pudto.getCatname() == null && pudto.getCatdesc() == null)) {
			throw new IllegalArgumentException("enter atleast one field");
		}
		if (pudto.getCatname() != null) {
			category.setCatName(pudto.getCatname());
		}
		if (pudto.getCatdesc() != null) {
			category.setDescription(pudto.getCatdesc());
		}
		Category updatedcategory = catrepo.save(category);
		CategoryResponseDTO resp = new CategoryResponseDTO();
		resp.setId(updatedcategory.getId());
		resp.setCatname(updatedcategory.getCatName());
		resp.setCatdesc(updatedcategory.getDescription());
		return resp;
	}
	public void deleteCategory(Long id) {
		Category category = catrepo.findById(id).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("category id is not available to delete " + id);
		}
		catrepo.delete(category);   
	}
	
}
