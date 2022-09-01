package com.example.intermediate.repository;

import com.example.intermediate.domain.Member;
import org.springframework.data.repository.Repository;

public interface MemberDataRepository extends Repository<Member, Long> {

}
