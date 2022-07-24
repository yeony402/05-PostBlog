package com.example.intermediate.service;

import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.dto.PostRequestDto;
import com.example.intermediate.domain.dto.ResponseDto;
import com.example.intermediate.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto) {
    Post post = new Post(requestDto, passwordEncoder);
    postRepository.save(post);
    return ResponseDto.success(post);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    return ResponseDto.success(post);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
  }

  @Transactional
  public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    post.update(requestDto);
    return ResponseDto.success(post);
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> isAuthor(Long id, String password) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    if (post.validatePassword(password, passwordEncoder)) {
      return ResponseDto.fail("PASSWORD_NOT_CORRECT", "password is not correct");
    }
    return ResponseDto.success(true);
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

}
