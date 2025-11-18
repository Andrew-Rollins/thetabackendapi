package com.tx.thetabackendapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "boardaccess")
public class boardAccess {
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
    
    public Integer getAccessId() { return accessId; }
    public void setAccessId(Integer accessId) { this.accessId = accessId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getBoardId() { return boardId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }
}