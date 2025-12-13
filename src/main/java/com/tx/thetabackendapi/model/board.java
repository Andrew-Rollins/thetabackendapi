package com.tx.thetabackendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "boards")
public class board {
    /*
    CREATE TABLE `boards` (
    `board_id` int NOT NULL AUTO_INCREMENT,
    `board_name` varchar(50) NOT NULL,
    `board_desc` text,
    `creator_id` int DEFAULT NULL,
    PRIMARY KEY (`board_id`),
    KEY `creator_id` (`creator_id`),
    CONSTRAINT `boards_ibfk_1` FOREIGN KEY (`creator_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;
    @Column(name = "board_name")
    private String boardName;
    @Column(name = "board_desc")
    private String boardDesc;
    @Column(name = "creator_id")
    private Integer creatorId;
    
    public board() {}
    //get
    public Integer getBoardId() { return boardId; }
    public String getBoardName() { return boardName; }
    public String getBoardDesc() { return boardDesc; }
    public Integer getCreatorId() { return creatorId; }
    //set
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public void setBoardName(String boardName) { this.boardName = boardName; }
    public void setBoardDesc(String boardDesc) { this.boardDesc = boardDesc; }
    public void setCreatorId(Integer creatorId) { this.creatorId = creatorId; }
}