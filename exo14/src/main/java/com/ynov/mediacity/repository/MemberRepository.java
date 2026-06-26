package com.ynov.mediacity.repository;

import com.ynov.mediacity.model.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(String id);
}
