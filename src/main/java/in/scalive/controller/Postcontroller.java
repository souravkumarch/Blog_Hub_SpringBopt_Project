package in.scalive.controller;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.dto.PostRequestDTO;
import in.scalive.dto.PostResponseDTO;
import in.scalive.dto.PostUpdateDTO;
import in.scalive.entity.Post;
import in.scalive.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class Postcontroller {

	private PostService pserv;

	@Autowired
	public Postcontroller(PostService pserv) {

		this.pserv = pserv;
	}

	@PostMapping
	public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostRequestDTO pdto, HttpSession session) {
		Long currUser = (Long) session.getAttribute("userId");
		pdto.setAuthorId(currUser);
		Post post = pserv.createPost(pdto);
		PostResponseDTO presponse = new PostResponseDTO(post.getId(), post.getTitle(), post.getContent(),
				post.getCategory().getCatName(), post.getCategory().getId(), post.getAuthor().getName(),
				post.getAuthor().getId(), post.getCreatedAt());
		return ResponseEntity.ok(presponse);
	}

//	@GetMapping
//	public ResponseEntity<List<PostResponseDTO>> getAllPost() {
//		List<Post> postlist = pserv.getAll();
//		List<PostResponseDTO> responseList = new ArrayList<>();
//
//		for (Post post : postlist) {
//			PostResponseDTO resdto = new PostResponseDTO();
//			resdto.setId(post.getId());
//			resdto.setTitle(post.getTitle());
//			resdto.setContent(post.getContent());
//			resdto.setCategoryName(post.getCategory().getCatName());
//			resdto.setCategoryId(post.getCategory().getId());
//			resdto.setAuthorName(post.getAuthor().getName());
//			resdto.setAuthorId(post.getAuthor().getId());
//			resdto.setCreateDateTime(post.getCreatedAt());
//			responseList.add(resdto);
//		}
//		return ResponseEntity.ok(responseList);
//	}

	@GetMapping("/{id}")
	public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
		Post post = pserv.getPostById(id);
		PostResponseDTO resdto = new PostResponseDTO();
		resdto.setId(post.getId());
		resdto.setTitle(post.getTitle());
		resdto.setContent(post.getContent());
		resdto.setCategoryName(post.getCategory().getCatName());
		resdto.setCategoryId(post.getCategory().getId());
		resdto.setAuthorName(post.getAuthor().getName());
		resdto.setAuthorId(post.getAuthor().getId());
		resdto.setCreateDateTime(post.getCreatedAt());
		return ResponseEntity.ok(resdto);

	}

	@GetMapping("/getAll")
	public ResponseEntity<List<PostResponseDTO>> getAll(@RequestParam String term) {
		List<Post> postlist = new ArrayList<>();
		List<PostResponseDTO> responseList = new ArrayList<>();
		if (term != null && !term.isBlank()) {
			postlist = pserv.searchPost(term);
		} else {
			postlist = pserv.getAll();
		}
		for (Post post : postlist) {
			PostResponseDTO resdto = new PostResponseDTO();
			resdto.setId(post.getId());
			resdto.setTitle(post.getTitle());
			resdto.setContent(post.getContent());
			resdto.setCategoryName(post.getCategory().getCatName());
			resdto.setCategoryId(post.getCategory().getId());
			resdto.setAuthorName(post.getAuthor().getName());
			resdto.setAuthorId(post.getAuthor().getId());
			resdto.setCreateDateTime(post.getCreatedAt());
			responseList.add(resdto);
		}
		return ResponseEntity.ok(responseList);
	}

	@GetMapping
	public ResponseEntity<Page<PostResponseDTO>> getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") int sortDir) {
		return ResponseEntity.ok(pserv.getAll(page, size, sortBy, sortBy));

	}

	public ResponseEntity<List<PostResponseDTO>> getpostByAuthor(@RequestAttribute("userId") Long userId) {
		List<Post> postlist = pserv.getPostByAuthor(userId);
		List<PostResponseDTO> responseList = new ArrayList<>();
		for (Post post : postlist) {
			PostResponseDTO resdto = new PostResponseDTO();
			resdto.setId(post.getId());
			resdto.setTitle(post.getTitle());
			resdto.setContent(post.getContent());
			resdto.setCategoryName(post.getCategory().getCatName());
			resdto.setCategoryId(post.getCategory().getId());
			resdto.setAuthorName(post.getAuthor().getName());
			resdto.setAuthorId(post.getAuthor().getId());
			resdto.setCreateDateTime(post.getCreatedAt());
			responseList.add(resdto);
		}
		return ResponseEntity.ok(responseList);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostUpdateDTO updatepost,
			@RequestAttribute("userId") Long userId, @RequestAttribute("userRole") String userRole) {
		  Post p = pserv.getPostById(id);
		if (!userId.equals(p.getAuthor().getId()) && !userRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Error:\"you can only update your own post\"}");

		}
		Post post = pserv.updatePost(updatepost, id);
		PostResponseDTO resdto = new PostResponseDTO();
		resdto.setId(post.getId());
		resdto.setTitle(post.getTitle());
		resdto.setContent(post.getContent());
		resdto.setCategoryName(post.getCategory().getCatName());
		resdto.setCategoryId(post.getCategory().getId());
		resdto.setAuthorName(post.getAuthor().getName());
		resdto.setAuthorId(post.getAuthor().getId());
		resdto.setCreateDateTime(post.getCreatedAt());
		return ResponseEntity.ok(resdto);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(
			@PathVariable Long id, 
			@RequestAttribute("userId") Long userId, 
			@RequestAttribute("userRole") String userRole){
		     Post p = pserv.getPostById(id);
			if (!userId.equals(p.getAuthor().getId()) && !userRole.equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Error:\"you can only delete your own post\"}");

		}
		pserv.deletePost(id);
	return	ResponseEntity.ok("Post deleted successfully");
	}
}
