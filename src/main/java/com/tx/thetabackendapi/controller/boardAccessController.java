package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.boardAccess;
import com.tx.thetabackendapi.repository.boardAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/board-access")
@CrossOrigin(origins = "*")
public class boardAccessController {
    
    @Autowired
    private boardAccessRepository boardAccessRepository;
    
    @GetMapping
    public List<boardAccess> getAllAccess() {
        return boardAccessRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public boardAccess getAccess(@PathVariable Integer id) {
        return boardAccessRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/user/{userId}")
    public List<boardAccess> getAccessByUser(@PathVariable Integer userId) {
        return boardAccessRepository.findByUserId(userId);
    }
    
    @GetMapping("/board/{boardId}")
    public List<boardAccess> getAccessByBoard(@PathVariable Integer boardId) {
        return boardAccessRepository.findByBoardId(boardId);
    }
    
    @PostMapping
    public boardAccess createAccess(@RequestBody boardAccess boardAccess) {
        return boardAccessRepository.save(boardAccess);
    }
    
    @PutMapping("/{id}")
    public boardAccess updateAccess(@PathVariable Integer id, @RequestBody boardAccess boardAccess) {
        boardAccess.setAccessId(id);
        return boardAccessRepository.save(boardAccess);
    }
    
    @DeleteMapping("/{id}")
    public void deleteAccess(@PathVariable Integer id) {
        boardAccessRepository.deleteById(id);
    }
}