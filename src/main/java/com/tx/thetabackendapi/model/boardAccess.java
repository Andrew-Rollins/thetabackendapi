package com.tx.thetabackendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "boardaccess")
public class boardAccess {
    /*
    CREATE TABLE `boardaccess` (
    `access_id` int NOT NULL AUTO_INCREMENT,
    `user_id` int NOT NULL,
    `board_id` int NOT NULL,
    `access_level` enum('read','post','moderate') NOT NULL DEFAULT 'read',
    PRIMARY KEY (`access_id`),
    UNIQUE KEY `user_id` (`user_id`,`board_id`),
    KEY `board_id` (`board_id`),
    CONSTRAINT `boardaccess_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
    CONSTRAINT `boardaccess_ibfk_2` FOREIGN KEY (`board_id`) REFERENCES `boards` (`board_id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Integer accessId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "board_id")
    private Integer boardId;
    @Column(name = "access_level")
    private String accessLevel;
    
    public boardAccess() {}
    //get
    public Integer getAccessId() { return accessId; }
    public Integer getUserId() { return userId; }
    public Integer getBoardId() { return boardId; }
    //TODO: enfroce 3 roles
    public String getAccessLevel() { return accessLevel; }
    //set
    public void setAccessId(Integer accessId) { this.accessId = accessId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}