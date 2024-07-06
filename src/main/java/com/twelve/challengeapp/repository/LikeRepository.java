package com.twelve.challengeapp.repository;

import com.twelve.challengeapp.entity.Like;
import com.twelve.challengeapp.entity.Post;
import com.twelve.challengeapp.entity.Comment;
import com.twelve.challengeapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndComment(User user, Comment comment);
}