package com.tx.thetabackendapi.repository;

import com.tx.thetabackendapi.model.post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface postRepository extends JpaRepository<post, Integer> {
    List<post> findByBoardId(Integer boardId);
}