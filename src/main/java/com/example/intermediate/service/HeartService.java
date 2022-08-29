package com.example.intermediate.service;


import com.example.intermediate.controller.request.HeartRequestDto;
import com.example.intermediate.controller.response.HeartResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Heart;
import com.example.intermediate.repository.HeartRepository;
import com.example.intermediate.repository.MemberRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    public ResponseDto<?> heart(HeartRequestDto heartDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Optional<Heart> click_heart = heartRepository.findHeartByMemberAndPostId(heartDto.getPostId(), heartDto.getMemberId());

        // 이미 좋아요 된 상태일 경우 에러
        if (click_heart.isPresent()) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        Heart heart = Heart.builder()
                .post(postRepository.findById(heartDto.getPostId()).get())
                .member(memberRepository.findById(heartDto.getMemberId()).get())
                .build();
        heartRepository.save(heart);
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .heart(1)
                        .build()
        );

    }

    public ResponseDto<?> unHeart(HeartRequestDto heartDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Optional<Heart> heart = heartRepository.findHeartByMemberAndPostId(heartDto.getPostId(), heartDto.getMemberId());

        if (heart.isEmpty()) {
            throw new RuntimeException("좋아요가 없습니다.");
        }

        heartRepository.delete(heart.get());
        return ResponseDto.success(
                HeartResponseDto.builder()
                        .heart(0)
                        .build()
        );
    }


}
