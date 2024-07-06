package com.twelve.challengeapp.service;

import com.twelve.challengeapp.entity.CommentLike;
import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.entity.Comment;
import com.twelve.challengeapp.exception.CommentNotFoundException;
import com.twelve.challengeapp.exception.UserNotFoundException;
import com.twelve.challengeapp.exception.AlreadyLikedException;
import com.twelve.challengeapp.repository.CommentLikeRepository;
import com.twelve.challengeapp.repository.CommentRepository;
import com.twelve.challengeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다"));

        if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new AlreadyLikedException("이미 좋아요를 누르셨습니다");
        }

        commentLikeRepository.save(new CommentLike(user, comment));
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다"));

        commentLikeRepository.findByUserAndComment(user, comment)
                .ifPresent(commentLike -> {
                    commentLikeRepository.delete(commentLike);
                    comment.setLikeCount(comment.getLikeCount() - 1);
                    commentRepository.save(comment);
                });    }
}