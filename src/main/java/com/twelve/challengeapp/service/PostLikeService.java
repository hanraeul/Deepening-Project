package com.twelve.challengeapp.service;

import com.twelve.challengeapp.entity.PostLike;
import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.entity.Post;
import com.twelve.challengeapp.exception.PostNotFoundException;
import com.twelve.challengeapp.exception.UserNotFoundException;
import com.twelve.challengeapp.exception.AlreadyLikedException;
import com.twelve.challengeapp.repository.PostLikeRepository;
import com.twelve.challengeapp.repository.PostRepository;
import com.twelve.challengeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다"));

        if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new AlreadyLikedException("이미 좋아요를 누르셨습니다");
        }

        postLikeRepository.save(new PostLike(user, post));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다"));

        postLikeRepository.findByUserAndPost(user, post)
                .ifPresent(postLike -> {
                    postLikeRepository.delete(postLike);
                    post.setLikeCount(post.getLikeCount() - 1);
                    postRepository.save(post);
                });
    }
}