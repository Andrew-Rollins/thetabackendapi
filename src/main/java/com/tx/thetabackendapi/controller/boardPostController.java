package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.boardPost;
import com.tx.thetabackendapi.service.boardPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
public class boardPostController {
    @Autowired
    private boardPostService postService; 
    @GetMapping
    public ResponseEntity<List<boardPost>> getAllPosts() {
        List<boardPost> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @PostMapping
    public ResponseEntity<boardPost> createPost(@RequestBody boardPost newPost) {
        boardPost savedPost = postService.addPost(newPost);
        return ResponseEntity.ok(savedPost);
    }
    
}