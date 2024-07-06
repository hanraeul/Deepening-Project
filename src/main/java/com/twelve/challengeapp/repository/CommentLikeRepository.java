package com.twelve.challengeapp.repository;

import com.twelve.challengeapp.entity.CommentLike;
import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}