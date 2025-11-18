package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.board;
import com.tx.thetabackendapi.repository.boardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "*")
public class boardController {
    
    @Autowired
    private boardRepository boardRepository;
    
    @GetMapping
    public List<board> getAllBoards() {
        return boardRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public board getBoard(@PathVariable Integer id) {
        return boardRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public board createBoard(@RequestBody board board) {
        return boardRepository.save(board);
    }
    
    @PutMapping("/{id}")
    public board updateBoard(@PathVariable Integer id, @RequestBody board board) {
        board.setBoardId(id);
        return boardRepository.save(board);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Integer id) {
        boardRepository.deleteById(id);
    }
}