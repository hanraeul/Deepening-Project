package com.twelve.challengeapp.service;

import com.twelve.challengeapp.entity.Post;
import com.twelve.challengeapp.entity.Comment;
import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.exception.CommentNotFoundException;
import com.twelve.challengeapp.exception.PostNotFoundException;
import com.twelve.challengeapp.exception.UserNotFoundException;
import com.twelve.challengeapp.repository.LikeRepository;
import com.twelve.challengeapp.repository.PostRepository;
import com.twelve.challengeapp.repository.CommentRepository;
import com.twelve.challengeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (post.getUser().equals(user)) {
            throw new IllegalStateException("You cannot like your own post");
        }

        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new IllegalStateException("You have already liked this post");
        }

        post.addLike(user);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!post.removeLike(user)) {
            throw new IllegalStateException("You haven't liked this post");
        }
    }

    @Transactional
    public void likeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (comment.getUser().equals(user)) {
            throw new IllegalStateException("You cannot like your own comment");
        }

        if (likeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new IllegalStateException("You have already liked this comment");
        }

        comment.addLike(user);
    }

    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        if (!comment.removeLike(user)) {
            throw new IllegalStateException("You haven't liked this comment");
        }
    }
}