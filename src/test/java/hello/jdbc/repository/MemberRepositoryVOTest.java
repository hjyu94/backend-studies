package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
class MemberRepositoryVOTest {

    MemberRepositoryVO repository = new MemberRepositoryVO();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV1", 10000);
        repository.save(member);

        // find by id
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember = {}", findMember);
        log.info("member == findMember : ", member == findMember);
        log.info("member equals findMember : ", member.equals(findMember));
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}