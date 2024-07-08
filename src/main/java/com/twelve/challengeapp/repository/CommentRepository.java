package com.twelve.challengeapp.repository;

import com.twelve.challengeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//import javax.xml.stream.events.Comment;
import com.twelve.challengeapp.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Query("SELECT c FROM Comment c JOIN c.commentLikes cl WHERE cl.user = :user")
    Page<Comment> findLikedCommentsByUser(@Param("user") User user, Pageable pageable);}