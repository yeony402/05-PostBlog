package com.example.intermediate.repository;

import com.example.intermediate.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartCommentRepository extends JpaRepository<HeartComment, Long> {
    Optional<HeartComment> findHeartByMemberAndPostAndComment(Member member, Post post, Comment comment);

}
