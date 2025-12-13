package com.tx.thetabackendapi.controller;

import com.tx.thetabackendapi.model.*;
import com.tx.thetabackendapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;


@RestController
@RequestMapping("/api/board")
@CrossOrigin(origins = "*")
public class boardApiController {
    //glue everything together
    @Autowired
    private boardRepository boardRepository;
    @Autowired
    private boardAccessRepository boardAccessRepository;
    @Autowired
    private boardInvitationRepository boardInvitationRepository;
    @Autowired
    private postRepository postRepository;
    @Autowired
    private userRepository userRepository;

    public record boardData(Integer boardId, String boardName, String boardDescription, String accessLevel, Integer creatorId){}
    public record boardPostData(Integer postId, Integer boardId, Integer posterId, String posterUsername, String title, String body, LocalDateTime datePosted){}
    public record userData(Integer userId, String username, String email, String accessLevel){}
    public record postData(String title, String body){}
    public record createBoardReq(String boardName, String boardDescription){}
    public record kickUserReq(String email){}
    public record inviteUserReq(Integer expirationDays ){}
    public record inviteRes(boolean success, String joinCode, String message){}
    public record joinBoardReq(String joinCode){}
    public record setUsernameReq(String displayName){}

    //helpers
    private user getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("404"));
    }

    private boardAccess getUserBoardAccess(Integer userId, Integer boardId) {
        return boardAccessRepository.findByUserId(userId).stream().filter(a -> a.getBoardId().equals(boardId)).findFirst().orElse(null);
    }
    
    //need to tie invites into this privs levels
    private boolean getPriv(String curPriv, String needPriv) {
        if (needPriv.equals("read")) {
            return curPriv != null;
        }
        if (needPriv.equals("post")) {
            return curPriv != null && (curPriv.equals("post") || curPriv.equals("moderate"));
        }
        if (needPriv.equals("moderate")) {
            return curPriv != null && curPriv.equals("moderate");
        }
        return false;
    }

    private String generateUniqueJoinCode() {
        SecureRandom random = new SecureRandom();
        String code = null;
        int attempts = 0;
        int maxAttempts = 100;
    
        while (code == null || boardInvitationRepository.existsByJoinCode(code)) {
            int number = 100000 + random.nextInt(900000);
            code = String.valueOf(number);
            attempts++;
            if (attempts >= maxAttempts) {
                throw new RuntimeException("Could not gen join code");
            }
        }
        return code;
    }

    //API STARTS
    //get
    @GetMapping("/all")
    public ResponseEntity<List<boardData>> getAllBoardsForUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        user currentUser = getCurrentUser(userDetails);
        List<boardAccess> accessList = boardAccessRepository.findByUserId(currentUser.getUserId());
        List<boardData> boards = new ArrayList<>();

        for (boardAccess access : accessList) {
            boardRepository.findById(access.getBoardId()).ifPresent(b -> {
                boards.add(new boardData(b.getBoardId(),b.getBoardName(),b.getBoardDesc(),access.getAccessLevel(),b.getCreatorId()));
             });
        }
        return ResponseEntity.ok(boards);
    }

    /**
     * GET /api/board/{boardId}/posts
     * Retrieves all posts for a board. User must have at least read access.
     */
    @GetMapping("/{boardId}/posts")
    public ResponseEntity<List<boardPostData>> getAllPostsForBoard(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId ) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess priv = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (priv == null) {
            return ResponseEntity.status(403).body(new ArrayList<>());
        }
        List<post> posts = postRepository.findByBoardId(boardId);
        List<boardPostData> postInfoList = new ArrayList<>();
        for (post p : posts) {
            String posterUsername = null;
            if (p.getPosterId() != null) {
                user poster = userRepository.findById(p.getPosterId()).orElse(null);
                if (poster != null) {
                    posterUsername = poster.getUsername();
                }
            }
            String content = p.getContent();
            String title = "";
            String body = content;
            //need to split table so there is a title and content field 
            if (content != null && content.contains("|||")) {
                String[] parts = content.split("\\|\\|\\|", 2);
                title = parts[0];
                body = parts.length > 1 ? parts[1] : "";
            }

            postInfoList.add(new boardPostData( p.getPostId(),p.getBoardId(),p.getPosterId(),posterUsername,title,body,p.getDatePosted()));
        }
        return ResponseEntity.ok(postInfoList);
    }

    @GetMapping("/{boardId}/members")
    public ResponseEntity<List<userData>> getAllMembersForBoard(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess access = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (access == null) {
            return ResponseEntity.status(403).body(new ArrayList<>());
        }
        List<boardAccess> accessList = boardAccessRepository.findByBoardId(boardId);
        List<userData> members = new ArrayList<>();
        for (boardAccess b : accessList) {
            userRepository.findById(b.getUserId()).ifPresent(u -> {members.add(new userData(u.getUserId(),u.getUsername(),u.getEmail(),b.getAccessLevel()));
            });
        }
        return ResponseEntity.ok(members);
    }

    @GetMapping("/username")
    public ResponseEntity<String> getDisplayName(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(getCurrentUser(userDetails).getUsername());
    }
    //set
    @PostMapping("/create")
    public ResponseEntity<boardData> createBoard(@AuthenticationPrincipal UserDetails userDetails, @RequestBody createBoardReq request) {
        user currentUser = getCurrentUser(userDetails);
        board newBoard = new board();
        newBoard.setBoardName(request.boardName());
        newBoard.setBoardDesc(request.boardDescription());
        newBoard.setCreatorId(currentUser.getUserId());
        board savedBoard = boardRepository.save(newBoard);
        boardAccess access = new boardAccess();
        access.setUserId(currentUser.getUserId());
        access.setBoardId(savedBoard.getBoardId());
        access.setAccessLevel("moderate");
        boardAccessRepository.save(access);
        return ResponseEntity.ok(new boardData(savedBoard.getBoardId(), savedBoard.getBoardName(), savedBoard.getBoardDesc(), "moderate",savedBoard.getCreatorId()));
    }


    @PostMapping("/{boardId}/post")
    public ResponseEntity<Boolean> postOnBoard(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId,@RequestBody postData request) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess access = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (access == null || !getPriv(access.getAccessLevel(), "post")) {
            return ResponseEntity.status(403).body(false);
        }
        post newPost = new post();
        newPost.setBoardId(boardId);
        newPost.setPosterId(currentUser.getUserId());
        //again need to split post in db
        newPost.setContent(request.title() + "|||" + request.body());
        newPost.setDatePosted(LocalDateTime.now());
        postRepository.save(newPost);
        return ResponseEntity.ok(true);
    }


    @PostMapping("/{boardId}/kick")
    public ResponseEntity<Boolean> kickUser(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId,@RequestBody kickUserReq request) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess access = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (access == null || !getPriv(access.getAccessLevel(), "moderate")) {
            return ResponseEntity.status(403).body(false);
        }
        user kickUser = userRepository.findByEmail(request.email()).orElse(null);
        if (kickUser == null) {
            return ResponseEntity.ok(false);
        }
        if (kickUser.getUserId().equals(currentUser.getUserId())) {
            return ResponseEntity.ok(false);
        }
        boardAccess targetAccess = getUserBoardAccess(kickUser.getUserId(), boardId);
        if (targetAccess == null) {
            return ResponseEntity.ok(false);
        }
        if (targetAccess.getAccessLevel().equals("moderate")) {
            return ResponseEntity.ok(false);
        }
        boardAccessRepository.delete(targetAccess);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/{boardId}/invite")
    public ResponseEntity<inviteRes> inviteUser(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId,@RequestBody(required = false) inviteUserReq request) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess access = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (access == null || !getPriv(access.getAccessLevel(), "moderate")) {
            return ResponseEntity.status(403).body(new inviteRes(false, null, "Cannot generate not high enough priv level"));
        }
        if (!boardRepository.existsById(boardId)) {
            return ResponseEntity.ok(new inviteRes(false, null, "Invlaid board"));
        }
        String joinCode = generateUniqueJoinCode();
        int expires = 14; 
        boardInvitation invitation = new boardInvitation();
        invitation.setBoardId(boardId);
        invitation.setJoinCode(joinCode);
        invitation.setInvitedBy(currentUser.getUserId());
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(expires));
        boardInvitationRepository.save(invitation);
        return ResponseEntity.ok(new inviteRes(true, joinCode, "Invite code generated successfully. Code expires in " + expires + " days."));
    }
    //this is broken atm on the app side
    @PostMapping("/leave/{boardId}")
    public ResponseEntity<Boolean> leaveBoard(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("boardId") Integer boardId) {
        user currentUser = getCurrentUser(userDetails);
        boardAccess access = getUserBoardAccess(currentUser.getUserId(), boardId);
        if (access == null) {
            return ResponseEntity.ok(false); 
        }
        boardAccessRepository.delete(access);
        return ResponseEntity.ok(true);
    }


    @PostMapping("/join")
    public ResponseEntity<Boolean> joinBoard(@AuthenticationPrincipal UserDetails userDetails,@RequestBody joinBoardReq request) {
        user currentUser = getCurrentUser(userDetails);
        boardInvitation invitation = boardInvitationRepository.findByJoinCode(request.joinCode()).orElse(null);
        if (invitation == null) {
            return ResponseEntity.ok(false);
        }
        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.ok(false); 
        }
        if (getUserBoardAccess(currentUser.getUserId(), invitation.getBoardId()) != null) {
            return ResponseEntity.ok(false); 
        }
        if (!boardRepository.existsById(invitation.getBoardId())) {
            return ResponseEntity.ok(false); 
        }
        boardAccess newAccess = new boardAccess();
        newAccess.setUserId(currentUser.getUserId());
        newAccess.setBoardId(invitation.getBoardId());
        //need to change based of invite you could add a field to invite table
        newAccess.setAccessLevel("post"); 
        boardAccessRepository.save(newAccess);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/username")
    public ResponseEntity<Boolean> setDisplayName(@AuthenticationPrincipal UserDetails userDetails, @RequestBody setUsernameReq request) {
        user currentUser = getCurrentUser(userDetails);
        String newUsername = request.displayName().trim();
        if (userRepository.existsByUsername(newUsername) && !newUsername.equals(currentUser.getUsername())) {
            return ResponseEntity.ok(false); 
        }
        currentUser.setUsername(newUsername);
        userRepository.save(currentUser);
        return ResponseEntity.ok(true);
    }



}