package com.twelve.challengeapp.controller;

import com.twelve.challengeapp.jwt.UserDetailsImpl;
import com.twelve.challengeapp.service.CommentLikeService;
import com.twelve.challengeapp.service.PostLikeService;
import com.twelve.challengeapp.util.SuccessResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final PostLikeService postLikeService;
    private final CommentLikeService commentLikeService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.likePost(postId, userDetails.getUserId());
        return SuccessResponseFactory.ok("Post liked successfully");
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.unlikePost(postId, userDetails.getUserId());
        return SuccessResponseFactory.ok("Post unliked successfully");
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.likeComment(commentId, userDetails.getUserId());
        return SuccessResponseFactory.ok("Comment liked successfully");
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> unlikeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.unlikeComment(commentId, userDetails.getUserId());
        return SuccessResponseFactory.ok("Comment unliked successfully");
    }
}