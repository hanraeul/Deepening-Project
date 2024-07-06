package com.twelve.challengeapp.repository;

import com.twelve.challengeapp.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN p.postLikes pl WHERE pl.user.id = :userId")
    Page<Post> findLikedPostsByUserId(Long userId, Pageable pageable);
}