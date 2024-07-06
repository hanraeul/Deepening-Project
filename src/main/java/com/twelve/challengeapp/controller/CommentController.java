package com.twelve.challengeapp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.twelve.challengeapp.dto.CommentRequestDto;
import com.twelve.challengeapp.dto.CommentResponseDto;
import com.twelve.challengeapp.jwt.UserDetailsImpl;
import com.twelve.challengeapp.service.CommentService;
import com.twelve.challengeapp.util.SuccessResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto,
										   @AuthenticationPrincipal UserDetailsImpl userDetails) {
		CommentResponseDto responseDto = commentService.createComment(postId, commentRequestDto.getContent(),
				userDetails.getUser().getId());
		return SuccessResponseFactory.ok(responseDto);
	}

	@PutMapping("/comments/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable Long commentId,
										   @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		CommentResponseDto responseDto = commentService.updateComment(commentId, commentRequestDto.getContent(),
				userDetails.getUser());
		return SuccessResponseFactory.ok(responseDto);
	}

	@DeleteMapping("/comments/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
										   @AuthenticationPrincipal UserDetailsImpl userDetails) {
		commentService.deleteComment(commentId, userDetails.getUser().getId());
		return SuccessResponseFactory.noContent();
	}

	@GetMapping("/{postId}/comments")
	public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
		List<CommentResponseDto> responseDtos = commentService.getCommentsByPostId(postId);
		return SuccessResponseFactory.ok(responseDtos);
	}

	@GetMapping("/comments/liked")
	public ResponseEntity<?> getLikedComments(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Page<CommentResponseDto> likedComments = commentService.getLikedComments(
				userDetails.getUser().getId(), PageRequest.of(page, size, Sort.by("createdAt").descending()));
		return SuccessResponseFactory.ok(likedComments);
	}
}