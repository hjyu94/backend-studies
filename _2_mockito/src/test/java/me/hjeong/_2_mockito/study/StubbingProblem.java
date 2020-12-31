package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.domain.Member;
import me.hjeong._2_mockito.domain.Study;
import me.hjeong._2_mockito.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StubbingProblem {

    @Mock StudyRepository studyRepository;
    @Mock MemberService memberService;

    @Test
    void test() {
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "테스트");

        Member member = new Member();

        when(memberService.findById(1L)).thenReturn(member);
        when(studyRepository.save(study)).thenReturn(study);

        // createNewStudy() 내에서 구현체가 없이 인터페이스만 이용해서 함수들을 호출하고 있다.
        studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwner());
        assertEquals(member, study.getOwner());
    }
}
