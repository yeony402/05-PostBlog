package com.example.intermediate.service;


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

    @Transactional
    public ResponseDto<?> heart(HeartDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();

        //게시글 좋아요 등록
        if(heartDto.getCommentId() == null) {
            Optional<Heart> postheart = heartRepository.findHeartByMemberAndPostAndComment(member, post, null);

            if (postheart.isPresent()) {
                throw new RuntimeException("이미 좋아요를 눌렀습니다.");
            }

            Heart heart = Heart.builder()
                    .post(post)
                    .member(member)
                    .comment(null)
                    .build();

            // 게시글 좋아요 정보 저장
            heartRepository.save(heart);
            // 게시글 전체 좋아요 수 반환
            return ResponseDto.success(
                    HeartResponseDto.builder()
                            .totalHeartcount(post.getTotalHeartCount()+1)
                            .build()
            );
        } else {
            // 댓글 좋아요 등록
            Comment comment = commentRepository.findById(heartDto.getCommentId()).get();
            Optional<Heart> postheart = heartRepository.findHeartByMemberAndPostAndComment(member, post, comment);

            if (postheart.isPresent()) {
                throw new RuntimeException("이미 좋아요를 눌렀습니다.");
            }

            Heart heart = Heart.builder()
                    .post(post)
                    .member(member)
                    .comment(comment)
                    .build();

            //댓글 좋아요 정보 저장
            heartRepository.save(heart);
            // 댓글 전체 좋아요 수 반환
            return ResponseDto.success(
                    HeartResponseDto.builder()
                            .totalHeartcount(comment.getTotalHeartCount()+1)
                            .build()
            );
        }
    }

    @Transactional
    public ResponseDto<?> unHeart(HeartDto heartDto, HttpServletRequest request) {

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();

        // 게시글 좋아요 삭제
        if (heartDto.getCommentId() == null) {
            Optional<Heart> postheart = heartRepository.findHeartByMemberAndPostAndComment(member, post, null);

            if (postheart.isEmpty()) {
                throw new RuntimeException("삭제할 좋아요가 없습니다.");
            }

            heartRepository.delete(postheart.get());

            // 게시글 전체 좋아요 수 반환
            return ResponseDto.success(
                    HeartResponseDto.builder()
                            .totalHeartcount(post.getTotalHeartCount() - 1)
                            .build()
            );
        } else {
            // 댓글 좋아요 삭제
            Comment comment = commentRepository.findById(heartDto.getCommentId()).get();
            Optional<Heart> postheart = heartRepository.findHeartByMemberAndPostAndComment(member, post, comment);

            if (postheart.isEmpty()) {
                throw new RuntimeException("삭제할 좋아요가 없습니다.");
            }

            heartRepository.delete(postheart.get());

            // 댓글 전체 좋아요 수 반환
            return ResponseDto.success(
                    HeartResponseDto.builder()
                            .totalHeartcount(comment.getTotalHeartCount() - 1)
                            .build()
            );
        }
    }
}
