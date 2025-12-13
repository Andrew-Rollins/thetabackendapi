package com.tx.thetabackendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class comment {
    /*
    CREATE TABLE `comments` (
    `comment_id` int NOT NULL AUTO_INCREMENT,
    `post_id` int NOT NULL,
    `poster_id` int DEFAULT NULL,
    `content` text NOT NULL,
    `date_posted` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`comment_id`),
    KEY `post_id` (`post_id`),
    KEY `poster_id` (`poster_id`),
    CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE,
    CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`poster_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;
    @Column(name = "post_id")
    private Integer postId;
    @Column(name = "poster_id")
    private Integer posterId;
    private String content;
    @Column(name = "date_posted")
    private LocalDateTime datePosted = LocalDateTime.now();
    //TODO: connect to boardapi
    public comment() {}
    //get
    public Integer getCommentId() { return commentId; }
    public Integer getPostId() { return postId; }
    public Integer getPosterId() { return posterId; }
    public String getContent() { return content; }
    public LocalDateTime getDatePosted() { return datePosted; }
    //set
    public void setCommentId(Integer commentId) { this.commentId = commentId; }
    public void setPostId(Integer postId) { this.postId = postId; }
    public void setPosterId(Integer posterId) { this.posterId = posterId; }
    public void setContent(String content) { this.content = content; }
    public void setDatePosted(LocalDateTime datePosted) { this.datePosted = datePosted; }
}