package com.tx.thetabackendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "boards")
public class board {
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
    
    public Integer getBoardId() { return boardId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public String getBoardName() { return boardName; }
    public void setBoardName(String boardName) { this.boardName = boardName; }
    public String getBoardDesc() { return boardDesc; }
    public void setBoardDesc(String boardDesc) { this.boardDesc = boardDesc; }
    public Integer getCreatorId() { return creatorId; }
    public void setCreatorId(Integer creatorId) { this.creatorId = creatorId; }
}