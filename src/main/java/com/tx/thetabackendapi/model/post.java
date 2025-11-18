package com.tx.thetabackendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;
    
    @Column(name = "board_id")
    private Integer boardId;
    
    @Column(name = "poster_id")
    private Integer posterId;
    
    private String content;
    
    @Column(name = "date_posted")
    private LocalDateTime datePosted = LocalDateTime.now();
    
    public post() {}
    
    public Integer getPostId() { return postId; }
    public Integer getBoardId() { return boardId; }
    public Integer getPosterId() { return posterId; }
    public String getContent() { return content; }
    public LocalDateTime getDatePosted() { return datePosted; }
    
    public void setPostId(Integer postId) { this.postId = postId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public void setPosterId(Integer posterId) { this.posterId = posterId; }
    public void setContent(String content) { this.content = content; }
    public void setDatePosted(LocalDateTime datePosted) { this.datePosted = datePosted; }
}