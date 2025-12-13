package com.tx.thetabackendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_invitations")
public class boardInvitation {
    /*
    CREATE TABLE `board_invitations` (
    `invitation_id` INT NOT NULL AUTO_INCREMENT,
    `board_id` INT NOT NULL,
    `join_code` VARCHAR(6) NOT NULL,
    `invited_by` INT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `expires_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`invitation_id`),
    UNIQUE KEY `join_code` (`join_code`),
    KEY `idx_board_id` (`board_id`),
    KEY `idx_invited_by` (`invited_by`),
    CONSTRAINT `board_invitations_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `boards` (`board_id`) ON DELETE CASCADE,
    CONSTRAINT `board_invitations_ibfk_2` FOREIGN KEY (`invited_by`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    */
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Integer invitationId;
    @Column(name = "board_id", nullable = false)
    private Integer boardId;
    @Column(name = "join_code", nullable = false, length = 6)
    private String joinCode;
    @Column(name = "invited_by", nullable = false)
    private Integer invitedBy;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public boardInvitation() {}
    //get
    public Integer getInvitationId() { return invitationId; }
    public Integer getBoardId() { return boardId; }
    public String getJoinCode() { return joinCode; }
    public Integer getInvitedBy() { return invitedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    //set
    public void setInvitationId(Integer invitationId) { this.invitationId = invitationId; }
    public void setBoardId(Integer boardId) { this.boardId = boardId; }
    public void setJoinCode(String joinCode) { this.joinCode = joinCode; }
    public void setInvitedBy(Integer invitedBy) { this.invitedBy = invitedBy; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}