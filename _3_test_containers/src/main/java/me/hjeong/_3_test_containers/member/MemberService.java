package me.hjeong._3_test_containers.member;

import me.hjeong._3_test_containers.domain.Member;
import me.hjeong._3_test_containers.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

    void notify(Member member);
}
