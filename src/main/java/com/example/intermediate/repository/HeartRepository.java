package com.example.intermediate.repository;

import com.example.intermediate.domain.Heart;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    List<Heart> findHeartByMemberAndPost(Member member, Post post);

}
