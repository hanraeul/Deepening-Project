package com.twelve.challengeapp.service;
import java.util.List;
import java.util.stream.Collectors;

import com.twelve.challengeapp.repository.CommentLikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twelve.challengeapp.dto.CommentResponseDto;
import com.twelve.challengeapp.entity.Comment;
import com.twelve.challengeapp.entity.Post;
import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.exception.CommentNotFoundException;
import com.twelve.challengeapp.exception.PostNotFoundException;
import com.twelve.challengeapp.exception.SelfCommentException;
import com.twelve.challengeapp.exception.UnauthorizedException;
import com.twelve.challengeapp.exception.UserNotFoundException;
import com.twelve.challengeapp.repository.CommentRepository;
import com.twelve.challengeapp.repository.PostRepository;
import com.twelve.challengeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentLikeRepository commentLikeRepository;

	@Override
	@Transactional
	public CommentResponseDto createComment(Long postId, String content, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found."));
		if (post.getUser().equals(user)) {
			throw new SelfCommentException("Comments on your own post are not allowed.");
		}
		Comment comment = Comment.builder().content(content).user(user).build();
		user.addComment(comment);
		post.addComment(comment);
		commentRepository.flush();
		return new CommentResponseDto(comment);
	}
	@Override
	@Transactional
	public CommentResponseDto updateComment(Long commentId, String content, User user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment not found"));
		if (!comment.getUser().equals(user)) {
			throw new UnauthorizedException("You are not authorized to update this comment");
		}
		comment.update(content);
		return new CommentResponseDto(comment);
	}
	@Override
	@Transactional
	public void deleteComment(Long commentId, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment not found"));
		if (!comment.getUser().equals(user)) {
			throw new UnauthorizedException("You are not authorized to delete this comment");
		}
		Post post = comment.getPost();
		user.removeComment(comment);
		post.removeComment(comment);
	}
	@Override
	public List<CommentResponseDto> getCommentsByPostId(Long postId) {
		return commentRepository.findByPostId(postId)
				.stream()
				.map(CommentResponseDto::new)
				.collect(Collectors.toList());
	}

	@Override
	public Page<CommentResponseDto> getLikedComments(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found."));

		Page<Comment> likedComments = commentRepository.findLikedCommentsByUser(user, pageable);

		return likedComments.map(CommentResponseDto::new);
	}
}