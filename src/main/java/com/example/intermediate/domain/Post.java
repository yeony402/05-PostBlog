package com.example.intermediate.domain;

import com.example.intermediate.domain.dto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private String author;

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  public Post(PostRequestDto postRequestDto, PasswordEncoder passwordEncoder) {
    Post.builder()
        .title(postRequestDto.getTitle())
        .content(postRequestDto.getContent())
        .author(postRequestDto.getAuthor())
        .password(passwordEncoder.encode(postRequestDto.getPassword()));
  }

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent();
    this.author = postRequestDto.getAuthor();
    this.password = postRequestDto.getPassword();
  }

  public boolean validatePassword(String password, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(password, this.password);
  }

}
