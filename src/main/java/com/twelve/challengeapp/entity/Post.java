package com.twelve.challengeapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    @Column(name = "like_count")
    private int likeCount; // 좋아요 개수 나타내는 필드

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.getPosts().add(this);
        }
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }

    public void addLike(User user) {
        this.postLikes.add(new PostLike(user, this));
        this.likeCount++; // 좋아요 추가 시 좋아요 개수 증가
    }

    public boolean removeLike(User user) {
        boolean removed = postLikes.removeIf(like -> like.getUser().equals(user));
        if (removed) {
            this.likeCount--; // 좋아요 삭제 시 좋아요 개수 감소
        }
        return removed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post post = (Post) obj;
        return Objects.equals(id, post.id) &&
                Objects.equals(title, post.title) &&
                Objects.equals(content, post.content) &&
                Objects.equals(user, post.user);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, user);
    }
}