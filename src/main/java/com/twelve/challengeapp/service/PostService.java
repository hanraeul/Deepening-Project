package com.twelve.challengeapp.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.twelve.challengeapp.dto.PostRequestDto;
import com.twelve.challengeapp.dto.PostResponseDto;
public interface PostService {
	PostResponseDto createPost(PostRequestDto requestDto, Long userId);
	Page<PostResponseDto> getPosts(Pageable pageable);
	PostResponseDto getPost(Long id);
	PostResponseDto updatePost(Long id, PostRequestDto requestDto, Long userId);

	void deletePost(Long id, Long userId);

	Page<PostResponseDto> getLikedPostsByUserId(Long userId, int page);
}