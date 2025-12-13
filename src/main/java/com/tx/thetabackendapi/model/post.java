package com.tx.thetabackendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class post {
    /*
    CREATE TABLE `posts` (
    `post_id` int NOT NULL AUTO_INCREMENT,
    `board_id` int NOT NULL,
    `poster_id` int DEFAULT NULL,
    `content` text NOT NULL,
    `date_posted` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`post_id`),
    KEY `board_id` (`board_id`),
    KEY `poster_id` (`poster_id`),
    CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `boards` (`board_id`) ON DELETE CASCADE,
    CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`poster_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    */
    
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
    //get
    public Integer getPostId() { return postId; }
    public Integer getBoardId() { return boardId; }
    public Integer getPosterId() { return posterId; }
    public String getContent() { return content; }
    public LocalDateTime getDatePosted() { return datePosted; }
    //set
    public void setPostId(Integer postId) { this.postId = postId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public void setPosterId(Integer posterId) { this.posterId = posterId; }
    public void setContent(String content) { this.content = content; }
    public void setDatePosted(LocalDateTime datePosted) { this.datePosted = datePosted; }
}