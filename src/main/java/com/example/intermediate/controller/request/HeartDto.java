package com.example.intermediate.controller.request;

import lombok.*;
import org.springframework.lang.Nullable;

@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성해줌
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자를 만들어줌
@Getter
@Setter
@Builder
public class HeartDto {
    private Long memberId;
    private Long postId;
}
