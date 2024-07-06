package com.twelve.challengeapp.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLike> commentLikes = new HashSet<>();

    @Column(name = "like_count")
    private int likeCount;

    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        setUser(user); // User와의 양방향 관계 설정
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    public void addUser(User user) {
        setUser(user); // User와의 양방향 관계 설정
        CommentLike like = new CommentLike(user, this);
        commentLikes.add(like);
        likeCount++;
    }

    public boolean removeUser(User user) {
        CommentLike likeToRemove = commentLikes.stream()
                .filter(like -> like.getUser().equals(user))
                .findFirst()
                .orElse(null);

        if (likeToRemove != null) {
            commentLikes.remove(likeToRemove);
            return true;
        }

        return false;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setPost(Post post) {
        this.post = post;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comment comment = (Comment) obj;
        return Objects.equals(id, comment.id) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(post, comment.post) &&
                Objects.equals(user, comment.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, post, user);
    }
}