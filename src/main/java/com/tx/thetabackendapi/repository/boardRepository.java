package com.tx.thetabackendapi.repository;

import com.tx.thetabackendapi.model.board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface boardRepository extends JpaRepository<board, Integer> {
}