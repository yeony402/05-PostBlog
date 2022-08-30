package com.example.intermediate.service;


import com.example.intermediate.controller.request.HeartRequestDto;
import com.example.intermediate.controller.response.HeartResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Heart;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.HeartRepository;
import com.example.intermediate.repository.MemberRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> heart(HeartRequestDto heartDto, HttpServletRequest request) {
        // response 200 응답
        // 실행안됨
        validate(request);

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Optional<Heart> heartobj = heartRepository.findHeartByMemberAndPost(member, post);

        if (heartobj.isPresent()) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        Heart heart = Heart.builder()
                .post(post)
                .member(member)
                .build();

        // 좋아요 정보 저장
        // 1 리턴
        heartRepository.save(heart);
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .heart(1)
                        .build()
        );

    }

    @Transactional
    public ResponseDto<?> unHeart(HeartRequestDto heartDto, HttpServletRequest request) {
        validate(request);

        Post post = postRepository.findById(heartDto.getPostId()).get();
        Member member = memberRepository.findById(heartDto.getMemberId()).get();
        Optional<Heart> heartobj = heartRepository.findHeartByMemberAndPost(member, post);

        if (heartobj.isEmpty()) {
            throw new RuntimeException("좋아요가 없습니다.");
        }

        // 좋아요 삭제
        // 0 리턴
        heartRepository.delete(heartobj.get());
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .heart(0)
                        .build()
        );
    }

    @Transactional
    public void validate(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            ResponseDto.success( "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            ResponseDto.success( "로그인이 필요합니다.");
        }
    }


}
