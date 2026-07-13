package in.scalive.service;

import org.springframework.stereotype.Service;

import in.scalive.dto.AuthResponseDTO;
import in.scalive.dto.RegisterRequestDTO;
import in.scalive.entity.Author;
import in.scalive.exception.ResourceAlreadyExistException;
import in.scalive.exception.ResourceNotFoundException;
import in.scalive.repository.AuthorRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {
	private AuthorRepository authrepo;

	public AuthService(AuthorRepository authrepo) {

		this.authrepo = authrepo;
	}

	public AuthResponseDTO register(RegisterRequestDTO req) {
		if (authrepo.existsByEmail(req.getEmail())) {
			throw new ResourceAlreadyExistException("Email already exist try another");
		}
		Author author = new Author();
		author.setName(req.getName());
		author.setEmail(req.getEmail());
		author.setPassword(req.getPassword());
		author.setAbout(req.getAbout());
		author.setRole("USER");
		Author auth = authrepo.save(author);
		AuthResponseDTO adto = new AuthResponseDTO();
		adto.setName(auth.getName());
		adto.setEmail(auth.getEmail());
		adto.setId(auth.getId());
		adto.setRole(auth.getRole());
		adto.setMessage("Registration Successful");
		return adto;
	}
	public AuthResponseDTO login(RegisterRequestDTO req ,HttpSession session) {
		  Author author =authrepo.findByEmail(req.getEmail()).orElse(null);
		  if(author == null) {
			  throw new ResourceNotFoundException("Invalid username Or Password");
		  }
		  if(!author.getPassword().equals(req.getPassword())) {
			  throw new ResourceNotFoundException("Invalid username Or Password");
		  }
		  session.setAttribute("userId", author.getId());
		  session.setAttribute("userName", author.getName());
		  session.setAttribute("userEmail", author.getEmail());
		  session.setAttribute("userRole", author.getRole());
		  
		  
		  
		  AuthResponseDTO adto = new AuthResponseDTO();
			adto.setName(author.getName());
			adto.setEmail(author.getEmail());
			adto.setId(author.getId());
			adto.setRole(author.getRole());
			adto.setMessage("login successfull");
			return adto;
		  
	}
	public void logout(HttpSession session) {
		session.invalidate();
	}
	
	public AuthResponseDTO getCurrentUser(HttpSession session) {
		Long userId =(Long) session.getAttribute("userId");
		if(userId == null) {
			throw new ResourceNotFoundException("No User Logged in");
		}
		String userName =(String) session.getAttribute("userName");
		String userEmail =(String) session.getAttribute("userEmail");
		String userRole =(String) session.getAttribute("userRole");
		return new AuthResponseDTO(userId,userName,userEmail,userRole,"Current User Data");
		
	}
  
}
