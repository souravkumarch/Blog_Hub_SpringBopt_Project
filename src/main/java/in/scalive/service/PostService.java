package in.scalive.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.scalive.dto.PostRequestDTO;
import in.scalive.dto.PostResponseDTO;
import in.scalive.dto.PostUpdateDTO;
import in.scalive.entity.Author;
import in.scalive.entity.Category;
import in.scalive.entity.Post;
import in.scalive.exception.ResourceNotFoundException;
import in.scalive.repository.AuthorRepository;
import in.scalive.repository.CategoryRepository;
import in.scalive.repository.PostRepository;

@Service
public class PostService {
	private AuthorRepository authrepo;
	private CategoryRepository catrepo;
	private PostRepository postrepo;

	public PostService(AuthorRepository authrepo, CategoryRepository catrepo, PostRepository postrepo) {
		this.authrepo = authrepo;
		this.catrepo = catrepo;
		this.postrepo = postrepo;
	}

	public Post createPost(PostRequestDTO pdto) {
		if (pdto.getAuthorId() == null) {
			throw new RuntimeException("AuthorId is Required");
		}
		Author author = authrepo.findById(pdto.getAuthorId()).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("no author found based on given id :" + pdto.getAuthorId());
		}
		Category category = catrepo.findById(pdto.getCategoryId()).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("no category found based on given id :" + pdto.getCategoryId());
		}

		Post post = new Post();
		post.setContent(pdto.getContent());
		post.setTitle(pdto.getTitle());
		post.setAuthor(author);
		post.setCategory(category);
		post.setCreatedAt(LocalDateTime.now());
		return postrepo.save(post);
	}

	public List<Post> getAll() {
		return postrepo.findAll();
	}

	public Page<PostResponseDTO> getAll(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		PageRequest pageable = PageRequest.of(page, size, sort);
		Page<Post> postpage = postrepo.findAll(pageable);
		List<PostResponseDTO> pageList = new ArrayList<>();
		for (Post post : postpage) {
			PostResponseDTO prdto = new PostResponseDTO();
			prdto.setId(post.getId());
			prdto.setContent(post.getContent());
			prdto.setCategoryId(post.getCategory().getId());
			prdto.setCategoryName(post.getCategory().getCatName());
			prdto.setAuthorId(post.getAuthor().getId());
			prdto.setAuthorName(post.getAuthor().getName());
			prdto.setTitle(post.getTitle());
			prdto.setCreateDateTime(post.getCreatedAt());
			pageList.add(prdto);
		}
		Page<PostResponseDTO> pagelist = new PageImpl<>(pageList, pageable, postpage.getTotalElements());
		return pagelist;

	}

	public Post getPostById(Long id) {
		Post post = postrepo.findById(id).orElse(null);
		if (post == null) {
			throw new ResourceNotFoundException("Post id is not valid : " + id);
		}
		return post;
	}

	public List<Post> searchPost(String term) {
		return postrepo.findByTitleContainingOrContentContaining(term.toLowerCase(), term.toLowerCase());
   
	}

	public List<Post> getPostByAuthor(Long id) {
		Author author = authrepo.findById(id).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("author is not available :" + id);
		}
		return postrepo.findByAuthor(author);
	}

	public Post updatePost(PostUpdateDTO updated, Long id) {
		Post post = getPostById(id);
		if (updated.getTitle() == null && updated.getContent() == null && updated.getAuthorId() == null
				&& updated.getCategoryId() == null) {
			throw new RuntimeException("Object can  not be null");
		}
		Author author = authrepo.findById(updated.getAuthorId()).orElse(null);
		if (author == null) {
			throw new ResourceNotFoundException("Author id is not found");
		}
		post.setAuthor(author);

		Category category = catrepo.findById(updated.getCategoryId()).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("Category id  is not found");
		}
		post.setCategory(category);
		if (updated.getTitle() != null) {
			post.setTitle(updated.getTitle());
		}
		if (updated.getContent() != null) {
			post.setTitle(updated.getTitle());
		}
		return postrepo.save(post);
	}
	public void deletePost(Long id) {
		Post post = getPostById(id);
		postrepo.delete(post);
	}
}
