package com.tx.thetabackendapi.repository;

import com.tx.thetabackendapi.model.boardAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface boardAccessRepository extends JpaRepository<boardAccess, Integer> {
    List<boardAccess> findByUserId(Integer userId);
    List<boardAccess> findByBoardId(Integer boardId);
}