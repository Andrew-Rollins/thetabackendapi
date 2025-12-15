package com.tx.thetabackendapi.controller;
//Run this by running gradle clean
import com.tx.thetabackendapi.controller.boardApiController.*;
import com.tx.thetabackendapi.model.*;
import com.tx.thetabackendapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardApiControllerTest {
    //mimic repos and controller
    @Mock
    private boardRepository boardRepository;
    @Mock
    private boardAccessRepository boardAccessRepository;
    @Mock
    private boardInvitationRepository boardInvitationRepository;
    @Mock
    private postRepository postRepository;
    @Mock
    private userRepository userRepository;
    @InjectMocks
    private boardApiController controller;
    //test objects
    private user proxyUser;
    //this is to test kicking
    private user proxyUserDupe;
    private UserDetails proxyUserDetails;
    private board proxyBoard;
    private boardAccess proxyBoardAccess;

    @BeforeEach
    void setUp() {
        //higher priv user
        proxyUser = new user();
        proxyUser.setUserId(0);
        proxyUser.setUsername("test");
        proxyUser.setEmail("test@test.com");
        //this is the user that will be kicked
        proxyUserDupe = new user();
        proxyUserDupe.setUserId(1);
        proxyUserDupe.setUsername("dupeuser");
        proxyUserDupe.setEmail("dupe@test.com");
        proxyUserDetails = User.builder().username("test").password("password").authorities(new ArrayList<>()).build();
        //board to test with 
        proxyBoard = new board();
        proxyBoard.setBoardId(0);
        proxyBoard.setBoardName("boardName");
        proxyBoard.setBoardDesc("boardDesc");
        proxyBoard.setCreatorId(0);
        //setup board priv so that proxyUser can kick proxyUserDupe
        proxyBoardAccess = new boardAccess();
        proxyBoardAccess.setUserId(0);
        proxyBoardAccess.setBoardId(0);
        proxyBoardAccess.setAccessLevel("moderate");
    }

    @Test
    void getBoardsUser() {
        //mockito structure
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(proxyBoardAccess));
        when(boardRepository.findById(0)).thenReturn(Optional.of(proxyBoard));
        ResponseEntity<List<boardData>> response = controller.getAllBoardsForUser(proxyUserDetails);
        //check if boards are retrived
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        //sanity check to make sure it did not get every board for all users
        verify(userRepository).findByUsername("test");
        verify(boardAccessRepository).findByUserId(0);
    }

    @Test
    void createBoard() {
        createBoardReq request = new createBoardReq("boardName", "boardDesc");
        board newBoard = new board();
        newBoard.setBoardId(1);
        newBoard.setBoardName("BoardName");
        newBoard.setBoardDesc("boardDesc");
        newBoard.setCreatorId(0);
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardRepository.save(any(board.class))).thenReturn(newBoard);
        when(boardAccessRepository.save(any(boardAccess.class))).thenReturn(new boardAccess());
        var response = controller.createBoard(proxyUserDetails, request);
        //check if board got created
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(boardRepository).save(any(board.class));
        verify(boardAccessRepository).save(any(boardAccess.class));
    }


    @Test
    void postBoardCorrect() {
        postData request = new postData("title", "body");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(proxyBoardAccess));
        when(postRepository.save(any(post.class))).thenReturn(new post());
        var response = controller.postOnBoard(proxyUserDetails, 0, request);
        //check if post is on board
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(postRepository).save(any(post.class));
    }

    @Test
    void postBoardFail() {
        postData request = new postData("title", "body");
        boardAccess testAccess = new boardAccess();
        testAccess.setUserId(0);
        testAccess.setBoardId(0);
        testAccess.setAccessLevel("read");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(testAccess));
        var response = controller.postOnBoard(proxyUserDetails, 0, request);
        //check if post failed
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse(response.getBody());
        verify(postRepository, never()).save(any());
    }

    @Test
    void kickUserCorrect() {
        boardAccess testAccess = new boardAccess();
        testAccess.setUserId(1);
        testAccess.setBoardId(0);
        testAccess.setAccessLevel("post");
        kickUserReq request = new kickUserReq("dupe@test.com");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(proxyBoardAccess));
        when(userRepository.findByEmail("dupe@test.com")).thenReturn(Optional.of(proxyUserDupe));
        when(boardAccessRepository.findByUserId(1)).thenReturn(List.of(testAccess));
        var response = controller.kickUser(proxyUserDetails, 0, request);
        //check if dupe user was kicked
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(boardAccessRepository).delete(testAccess);
        verify(userRepository).findByEmail("dupe@test.com");
    }

    @Test
    void kickSelfIncorrect() {
        kickUserReq request = new kickUserReq("test@test.com");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(proxyBoardAccess));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(proxyUser));
        var response = controller.kickUser(proxyUserDetails, 0, request);
        //make sure you cant kick yourself
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
        verify(boardAccessRepository, never()).delete(any());
    }


    @Test
    void inviteCorrect() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardAccessRepository.findByUserId(0)).thenReturn(List.of(proxyBoardAccess));
        when(boardRepository.existsById(0)).thenReturn(true);
        when(boardInvitationRepository.existsByJoinCode(anyString())).thenReturn(false);
        when(boardInvitationRepository.save(any(boardInvitation.class))).thenReturn(new boardInvitation());
        var response = controller.inviteUser(proxyUserDetails, 0, null);
        //check if invite generated correctly
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().success());
        verify(boardInvitationRepository).save(any(boardInvitation.class));
    }

    @Test
    void joinCorrect() {
        boardInvitation invite = new boardInvitation();
        invite.setBoardId(1);
        invite.setJoinCode("123456");
        invite.setExpiresAt(LocalDateTime.now().plusDays(7));
        joinBoardReq request = new joinBoardReq("123456");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(proxyUser));
        when(boardInvitationRepository.findByJoinCode("123456")).thenReturn(Optional.of(invite));
        when(boardAccessRepository.findByUserId(0)).thenReturn(new ArrayList<>());
        when(boardRepository.existsById(1)).thenReturn(true);
        when(boardAccessRepository.save(any(boardAccess.class))).thenReturn(new boardAccess());
        var response = controller.joinBoard(proxyUserDetails, request);
        //check if board is joined
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(boardAccessRepository).save(any(boardAccess.class));
    }
}