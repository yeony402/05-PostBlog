package com.example.intermediate.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken extends Timestamped {

  @Id
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String value;

  public void updateValue(String token) {
    this.value = token;
  }
}
