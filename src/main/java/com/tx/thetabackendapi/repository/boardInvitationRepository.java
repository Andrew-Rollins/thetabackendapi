package com.tx.thetabackendapi.repository;

import com.tx.thetabackendapi.model.boardInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface boardInvitationRepository extends JpaRepository<boardInvitation, Integer> {
    Optional<boardInvitation> findByJoinCode(String joinCode);
    List<boardInvitation> findByBoardId(Integer boardId);
    List<boardInvitation> findByInvitedBy(Integer invitedBy);
    boolean existsByJoinCode(String joinCode);
}