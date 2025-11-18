package com.tx.thetabackendapi.repository;

import com.tx.thetabackendapi.model.comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface commentRepository extends JpaRepository<comment, Integer> {
    List<comment> findByPostId(Integer postId);
}