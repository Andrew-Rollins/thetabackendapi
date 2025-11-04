package com.tx.thetabackendapi.service;
import com.tx.thetabackendapi.model.boardPost;
import com.tx.thetabackendapi.repository.boardPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class boardPostService {
    @Autowired
    private boardPostRepository postRepository; 
    //basic testing functions to see if database is connected
    public List<boardPost> getAllPosts() {
        return postRepository.findAll();  
    }
    public boardPost addPost(boardPost post) {
        return postRepository.save(post);
    }
    
}

