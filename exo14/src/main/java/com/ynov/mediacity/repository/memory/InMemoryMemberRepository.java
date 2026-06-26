package com.ynov.mediacity.repository.memory;

import com.ynov.mediacity.model.Member;
import com.ynov.mediacity.repository.MemberRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryMemberRepository implements MemberRepository {

    private final Map<String, Member> members = new HashMap<>();

    @Override
    public Member save(Member member) {
        members.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(members.get(id));
    }
}
