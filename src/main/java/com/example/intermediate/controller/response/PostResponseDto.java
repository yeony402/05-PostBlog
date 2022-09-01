package com.example.intermediate.controller.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String author;
  private List<CommentResponseDto> commentResponseDtoList;
  private int TotalCommentCount;
  private int TotalHeartCount;
  private String imgUrl;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
