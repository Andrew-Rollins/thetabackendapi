package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.post;
import com.tx.thetabackendapi.repository.postRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class postController {
    
    @Autowired
    private postRepository postRepository;
    
    @GetMapping
    public List<post> getAllPosts() {
        return postRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public post getPost(@PathVariable Integer id) {
        return postRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/board/{boardId}")
    public List<post> getPostsByBoard(@PathVariable Integer boardId) {
        return postRepository.findByBoardId(boardId);
    }
    
    @PostMapping
    public post createPost(@RequestBody post post) {
        return postRepository.save(post);
    }
    
    @PutMapping("/{id}")
    public post updatePost(@PathVariable Integer id, @RequestBody post post) {
        post.setPostId(id);
        return postRepository.save(post);
    }
    
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Integer id) {
        postRepository.deleteById(id);
    }
}