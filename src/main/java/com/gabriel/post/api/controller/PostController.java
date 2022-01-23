package com.gabriel.post.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.post.domain.model.Post;
import com.gabriel.post.repository.PostRepository;


@RestController
@RequestMapping("/posts")
@CrossOrigin("http://localhost:4200")
public class PostController {

	@Autowired
	private PostRepository postRepository;
	

	@GetMapping
	public List<Post> listar() {
		return postRepository.findAll();
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Post> buscar(@PathVariable Long postId) {
		Optional<Post> post = postRepository.findById(postId);
		
		if (!post.isEmpty()) {
			return ResponseEntity.ok(post.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Post adicionar(@RequestBody Post post) {
		return postRepository.save(post);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<Post> atualizar(@PathVariable Long postId, @RequestBody Post post) {
		Post postAtual = postRepository
				.findById(postId)
				.orElseThrow(RuntimeException::new);
		
		if( postAtual != null ) {
			BeanUtils.copyProperties(post, postAtual, "id"); // Neste caso durante a cópia ele irá ignorar o ID			
			postAtual = postRepository.save(postAtual);			
			return ResponseEntity.ok(postAtual);
		}
			
			return ResponseEntity.notFound().build();
	}	
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<Post> remover(@PathVariable Long postId) {
		try {
			Post post = postRepository.getById(postId);
		
			if ( post != null ) {
				postRepository.deleteById(postId);
				 return ResponseEntity.noContent().build();
			}
			return ResponseEntity.notFound().build();	
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}


	} 
	
}
