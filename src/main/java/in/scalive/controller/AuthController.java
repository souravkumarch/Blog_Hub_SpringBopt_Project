package in.scalive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.dto.AuthResponseDTO;
import in.scalive.dto.LoginRequestDTO;
import in.scalive.dto.RegisterRequestDTO;
import in.scalive.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	AuthService serv;
    @Autowired
	public AuthController(AuthService serv) {
		
		this.serv = serv;
	}
    @PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request){
	AuthResponseDTO  adto =	serv.register(request);
	return new ResponseEntity<>(adto,HttpStatus.CREATED);
	}
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request,HttpSession session){
		AuthResponseDTO  adto = serv.login(request, session);
		System.out.println("login controller called" +request.getPassword()+"   "+request.getEmail());
		return ResponseEntity.ok(adto);
	}
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session){
    	serv.logout(session);
    	return ResponseEntity.ok("Logout successful");
    }
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDTO> getCurrUser(HttpSession session){
    	AuthResponseDTO adto =serv.getCurrentUser(session);
    	return  ResponseEntity.ok(adto);
    }
}
