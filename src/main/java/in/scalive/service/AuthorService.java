package in.scalive.service;

import java.security.PublicKey;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.scalive.dto.AuthorUpdateDTO;
import in.scalive.entity.Author;
import in.scalive.exception.ResourceNotFoundException;
import in.scalive.repository.AuthorRepository;

@Service
public class AuthorService {

	private AuthorRepository repo;

	@Autowired
	public AuthorService(AuthorRepository repo) {

		this.repo = repo;
	}
    public List<Author> findAllUsers(){
    	return repo.findAll();
    }
    public Author findById(Long id) {
    Author author =	repo.findById(id).orElse(null);
    if(author == null) {
    	throw new ResourceNotFoundException("Author id "+id+" is not valid");
    }
    return author;
    }
    public Author updateUser(Long id,AuthorUpdateDTO updateuser) {
        Author author =	repo.findById(id).orElse(null);
        if(author == null) {
        	throw new ResourceNotFoundException("Author id "+id+" is not valid");
        }
        if(updateuser.getName()==null && updateuser.getAbout()==null && updateuser.getEmail()==null) {
        	throw new RuntimeException("empty objects are not allowed");
        }
        if(updateuser.getName() !=null && updateuser.getName().isBlank()) {
        	throw new RuntimeException("Name Cannot be  not Blank");
        }
        if(updateuser.getAbout() !=null && updateuser.getAbout().isBlank()) {
        	throw new RuntimeException("About Cannot be  not Blank");
        }
        if(updateuser.getName() !=null) {
        	author.setName(updateuser.getName());
        }
        if(updateuser.getEmail() !=null) {
        	author.setEmail(updateuser.getEmail());
        }
        if(updateuser.getAbout() !=null) {
        	author.setAbout(updateuser.getAbout());
        }
        return repo.save(author);
    }
    public void deleteUser(Long id) {
    Author author =	findById(id);
    repo.delete(author);
    }
}
