package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.comment;
import com.tx.thetabackendapi.repository.commentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class commentController {
    
    @Autowired
    private commentRepository commentRepository;
    
    @GetMapping
    public List<comment> getAllComments() {
        return commentRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public comment getComment(@PathVariable Integer id) {
        return commentRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/post/{postId}")
    public List<comment> getCommentsByPost(@PathVariable Integer postId) {
        return commentRepository.findByPostId(postId);
    }
    
    @PostMapping
    public comment createComment(@RequestBody comment comment) {
        return commentRepository.save(comment);
    }
    
    @PutMapping("/{id}")
    public comment updateComment(@PathVariable Integer id, @RequestBody comment comment) {
        comment.setCommentId(id);
        return commentRepository.save(comment);
    }
    
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
    }
}