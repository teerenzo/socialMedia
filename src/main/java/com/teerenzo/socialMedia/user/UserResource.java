package com.teerenzo.socialMedia.user;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import jakarta.validation.Valid;

@RestController
public class UserResource {
	

	private UserRepository repository;
	private PostRepository postRepository;
	
	public UserResource(UserRepository repository,PostRepository postRepository) {
		this.repository=repository;
		this.postRepository=postRepository;
	}
	
	@GetMapping("users")
	public MappingJacksonValue retrieveAllUsers(){
		List<User> all = repository.findAll();
		MappingJacksonValue mapping = new MappingJacksonValue(all);
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name","id");
		FilterProvider filters = new SimpleFilterProvider().addFilter("UsersFilter", filter);
		mapping.setFilters(filters);
				
		
		return mapping;
	}

	@GetMapping("users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id){
		User user = repository.findById(id).get();
		if(user==null)
			 throw new UserNotFoundException("id: "+id);
		
		EntityModel<User> entity = EntityModel.of(user);
		
		 WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers()); 
		
		entity.add(link.withRel("all-users"));
			
		return entity;
	}
	
	@PostMapping("users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		User savedUser = repository.save(user);
		URI location= ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("users/{id}")
	public void deleteUser(@PathVariable int id){
		repository.deleteById(id);
	}
	
	@GetMapping("users/{id}/posts")
	public List<Post> getPosts(@PathVariable int id){
		User user = repository.findById(id).get();
		return user.getPosts();
	}
	
	
	@PostMapping("users/{id}/posts")
	public void createPost(@PathVariable int id, @Valid @RequestBody Post post){
		User user = repository.findById(id).get();
		if(user==null)
			 throw new UserNotFoundException("id: "+id);
		post.setUser(user);
		
		postRepository.save(post);

	}
	
	
	

}
