package com.izakaya.website.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.izakaya.website.entity.PostEntity;
import com.izakaya.website.entity.UserEntity;
import com.izakaya.website.model.Post;
import com.izakaya.website.model.User;
import com.izakaya.website.repository.PostRepository;
import com.izakaya.website.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	
	/*게시글 생성*/
	@Transactional
	public void create(Post post) {
			Optional<UserEntity> userById = userRepository.findById(post.getUser().getId());
			
			if(userById.isPresent()) {
				UserEntity userEntity = userById.get();
				
				PostEntity postEntity = PostEntity.builder()
						.id(post.getId())
						.title(post.getTitle())
						.content(post.getContent())
						.createTime(post.getCreateTime())
						.user(userEntity)
						.build();
				
		    	postRepository.save(postEntity);
			}
		
		    
	}
	/*게시글 목록*/
	public List<Post> findAll() {
		List<PostEntity> postEntityList = postRepository.findAll();
		List<Post> postList = new ArrayList<>();
		for(PostEntity postEntity : postEntityList) {
			postList.add(postEntity.toPost());
		}
		return postList;
	}
	
	/*게시글 조회*/
	public Post findById(Long id) {
		Optional<PostEntity> postById = postRepository.findById(id);
		
		if(postById.isPresent()) {
			return postById.get().toPost();
		}
		return null;
	}
	
	/*게시글 삭제*/
	@Transactional
	public void removePost(Long postId, User logUser) {
		Optional<PostEntity> postById = postRepository.findById(postId);
		
		if(postById.isPresent()) {
			PostEntity postEntity = postById.get();
			postRepository.delete(postEntity);
		}	
	}
	
	/*게시글 수정*/
	@Transactional
	public void editPost(Post post, Long postId) {
		Optional<PostEntity> postById = postRepository.findById(postId);
		
		if(postById.isPresent()) {
			PostEntity postEntity = postById.get();
			postEntity.update(post.getTitle(), post.getContent());
			
			postRepository.save(postEntity);
		}
	}
}
