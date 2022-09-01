package com.example.intermediate.service;

import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.controller.request.PostRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
//<<<<<<< HEAD
//import com.example.intermediate.jwt.TokenProvider;
//import com.example.intermediate.repository.CommentRepository;
//import com.example.intermediate.repository.PostRepository;
//=======
import com.example.intermediate.domain.S3Uploader;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
//<<<<<<< HEAD
//
//  private final TokenProvider tokenProvider;
//
//  @Transactional
//  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
//    if (null == request.getHeader("Refresh-Token")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    Member member = validateMember(request);
//    if (null == member) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    Post post = Post.builder()
//        .title(requestDto.getTitle())
//        .content(requestDto.getContent())
//=======
  private final TokenProvider tokenProvider;
  private final S3Uploader s3Uploader;


  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request, MultipartFile multipartFile) throws IOException {
    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    String imgUrl = s3Uploader.upload(multipartFile, "static");

    Post post = Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .imgUrl(imgUrl)
        .member(member)
        .build();
    postRepository.save(post);
    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .author(post.getMember().getNickname())
            .imgUrl(post.getImgUrl())
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
//<<<<<<< HEAD
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//=======

      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();


    for (Comment comment : commentList) {
      commentResponseDtoList.add(
              CommentResponseDto.builder()
                      .id(comment.getId())
                      .author(comment.getMember().getNickname())
                      .content(comment.getContent())
                      .createdAt(comment.getCreatedAt())
                      .modifiedAt(comment.getModifiedAt())
                      .build()
      );
    }


    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .commentResponseDtoList(commentResponseDtoList)
                    .TotalCommentCount(post.getTotalCommentCount())
                    .TotalHeartCount(post.getTotalHeartCount())
                    .imgUrl(post.getImgUrl())
                    .author(post.getMember().getNickname())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
//=======
//    for (Comment comment : commentList) {
//      commentResponseDtoList.add(
//          CommentResponseDto.builder()
//              .id(comment.getId())
//              .author(comment.getMember().getNickname())
//              .content(comment.getContent())
//              .createdAt(comment.getCreatedAt())
//              .modifiedAt(comment.getModifiedAt())
//              .build()
//      );
//    }
//
//    return ResponseDto.success(
//        PostResponseDto.builder()
//            .id(post.getId())
//            .title(post.getTitle())
//            .content(post.getContent())
//            .commentResponseDtoList(commentResponseDtoList)
//            .author(post.getMember().getNickname())
//            .imgUrl(post.getImgUrl())
//            .createdAt(post.getCreatedAt())
//            .modifiedAt(post.getModifiedAt())
//            .build()
//>>>>>>> 93b3553a138c386df57e8418da661997334c5180
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
  }

  @Transactional
  public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
//<<<<<<< HEAD
//    if (null == request.getHeader("Refresh-Token")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    Member member = validateMember(request);
//    if (null == member) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//=======
    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    Post post = isPresentPost(id);
    if (null == post) {
//<<<<<<< HEAD
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//    }
//
//    if (post.validateMember(member)) {
//      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
//=======
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "only author can update");
    }

    post.update(requestDto);
    return ResponseDto.success(post);
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
//<<<<<<< HEAD
//    if (null == request.getHeader("Refresh-Token")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    if (null == request.getHeader("Authorization")) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "로그인이 필요합니다.");
//    }
//
//    Member member = validateMember(request);
//    if (null == member) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//=======
    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    Post post = isPresentPost(id);
    if (null == post) {
//<<<<<<< HEAD
//      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
//    }
//
//    if (post.validateMember(member)) {
//      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
//=======
      return ResponseDto.fail("NOT_FOUND", "post id is not exist");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "only author can delete");
    }

    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }


  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }

}
