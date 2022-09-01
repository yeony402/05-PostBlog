package com.example.intermediate.service;


import com.example.intermediate.controller.request.HeartCommentDto;
import com.example.intermediate.controller.request.HeartDto;
import com.example.intermediate.controller.response.HeartResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.*;
import com.example.intermediate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final HeartCommentRepository heartcommentRepository;

    //게시글 좋아요 등록
    @Transactional
    public ResponseDto<?> postheart(HeartDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Optional<Heart> postheart = heartRepository.findHeartByMemberAndPost(member, post);

        if (postheart.isPresent()) {
            throw new RuntimeException("이미 게시글 좋아요를 눌렀습니다.");
        }

        Heart heart = Heart.builder()
                .post(post)
                .member(member)
                .build();

        // 게시글 좋아요 정보 저장
        heartRepository.save(heart);
        // 좋아요 수 반환
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .totalHeartcount(post.getTotalHeartCount()+1)
                        .build()
        );
    }

    // 댓글 좋아요 등록
    @Transactional
    public ResponseDto<?> commentheart(HeartCommentDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Comment comment = commentRepository.findById(heartDto.getCommentId()).get();
        Optional<HeartComment> commentheart = heartcommentRepository.findHeartByMemberAndPostAndComment(member, post, comment);

        if (commentheart.isPresent()) {
            throw new RuntimeException("이미 댓글 좋아요를 눌렀습니다.");
        }

        HeartComment heart = HeartComment.builder()
                .post(post)
                .member(member)
                .comment(comment)
                .build();

        // 댓글 좋아요 정보 저장
        heartcommentRepository.save(heart);
        // 댓글 좋아요 수 집계 안됨
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .totalHeartcount(comment.getTotalHeartCount())
                        .build()
        );
    }

    // 게시글 좋아요 삭제
    @Transactional
    public ResponseDto<?> postunHeart(HeartDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Optional<Heart> postheart = heartRepository.findHeartByMemberAndPost(member, post);

        if (postheart.isEmpty()) {
            throw new RuntimeException("게시글 좋아요가 없습니다.");
        }

        heartRepository.delete(postheart.get());

        return ResponseDto.success(
                HeartResponseDto.builder()
                        .totalHeartcount(post.getTotalHeartCount()-1)
                        .build()
        );
    }

    // 댓글 좋아요 삭제
    @Transactional
    public ResponseDto<?> commentunHeart(HeartCommentDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Comment comment = commentRepository.findById(heartDto.getCommentId()).get();
        Optional<HeartComment> commentheart = heartcommentRepository.findHeartByMemberAndPostAndComment(member, post, comment);

        if (commentheart.isEmpty()) {
            throw new RuntimeException("게시글 좋아요가 없습니다.");
        }

        heartcommentRepository.delete(commentheart.get());

        // 댓글 좋아요 수 집계 안됨
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .totalHeartcount(comment.getTotalHeartCount())
                        .build()
        );
    }

}
