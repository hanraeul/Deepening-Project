package com.twelve.challengeapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Table(name = "comment")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void update(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    public int getLikeCount() {
        return likes.size();
    }

    public boolean addLike(User user) {
        return likes.add(new Like(user, this));
    }

    public boolean removeLike(User user) {
        return likes.removeIf(like -> like.getUser().equals(user));
    }

}
