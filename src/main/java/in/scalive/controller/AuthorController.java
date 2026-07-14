package in.scalive.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.dto.AuthResponseDTO;
import in.scalive.dto.AuthorResponseDTO;
import in.scalive.dto.AuthorUpdateDTO;
import in.scalive.entity.Author;
import in.scalive.service.AuthorService;

@RestController
@RequestMapping("/api/user")
public class AuthorController {
	private AuthorService serv;

	public AuthorController(AuthorService serv) {
		this.serv = serv;
	}
   @GetMapping("/{id}")
	public ResponseEntity<AuthorResponseDTO> getUserById(@PathVariable Long id){
		Author author =serv.findById(id);
		AuthorResponseDTO authorResponse = new AuthorResponseDTO(id,author.getName(),author.getEmail(),author.getRole(),author.getAbout());
		return ResponseEntity.ok(authorResponse);
	}
   @PutMapping("/{id}")
   public ResponseEntity<?> updateUserById(@PathVariable Long id,@RequestBody AuthorUpdateDTO authordto,@RequestAttribute("userId") Long userId, @RequestAttribute("userRole") String userRole){
     if(!id.equals(userId) && !userRole.equals("ADMIN")) {
    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":you can update only your profile\"}");
     }
    Author author = serv.updateUser(id, authordto);
     AuthorResponseDTO  updresponse =new AuthorResponseDTO(author.getId(),author.getName(),author.getEmail(),author.getRole(),author.getAbout());
     return ResponseEntity.ok(updresponse);
   }
   @GetMapping()
   public ResponseEntity<List<AuthorResponseDTO>> getallUsers(){
	 List<Author> authorList =  serv.findAllUsers();
	 List<AuthorResponseDTO> responseList = new ArrayList<>();
	 for(Author author: authorList) {
		 AuthorResponseDTO authresponse = new AuthorResponseDTO(author.getId(),author.getName(),author.getEmail(),author.getRole(),author.getAbout());
		 responseList.add(authresponse);
	 }
	 return ResponseEntity.ok(responseList);
   }
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteUserById(@PathVariable Long id,@RequestAttribute("userId") Long userId, @RequestAttribute("userRole") String userRole){
	   if(!id.equals(userId) && !userRole.equals("ADMIN")) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":you can delete only your profile\"}");
	     }
	   serv.deleteUser(userId);
	   return ResponseEntity.ok("user deleted");
   }
   
}
