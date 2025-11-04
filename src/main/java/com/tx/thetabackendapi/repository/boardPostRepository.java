package com.tx.thetabackendapi.repository;
import com.tx.thetabackendapi.model.boardPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface boardPostRepository extends JpaRepository<boardPost, Integer> {
    
}