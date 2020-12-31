package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.domain.Member;
import me.hjeong._2_mockito.domain.Study;
import me.hjeong._2_mockito.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StubbingProblem {

    @Mock StudyRepository studyRepository;
    @Mock MemberService memberService;

    @Test
    void test() {
        // [Given]
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "테스트");

        Member member = new Member();

        when(memberService.findById(1L)).thenReturn(member);
        when(studyRepository.save(study)).thenReturn(study);

        // [When]
        // createNewStudy() 내에서 구현체가 없이 인터페이스만 이용해서 함수들을 호출하고 있다.
        studyService.createNewStudy(1L, study);

        // [Then]
        assertNotNull(study.getOwner());
        assertEquals(member, study.getOwner());

        verify(memberService, times(1)).notify(study);
//        verifyNoMoreInteractions(memberService); // 어떤 액션 이후에 더 이상 mock 을 사용하면 안된다 테스트
        verify(memberService, times(1)).notify(member);
        verify(memberService, never()).validate(any());

        // notify()  호출된 순서 확인해보기
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);

    }

    @Test
    void bdd_style_test() {
        // [Given]
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "테스트");

        Member member = new Member();

        given(memberService.findById(1L)).willReturn(member); // when() 대신에
        given(studyRepository.save(study)).willReturn(study);

        // [When]
        studyService.createNewStudy(1L, study);

        // [Then]
        assertNotNull(study.getOwner());
        assertEquals(member, study.getOwner());

        then(memberService).should(times(1)).notify(study); // verify(memberService, times(1)).notify(study);
        then(memberService).should(times(1)).notify(member); // verify(memberService, times(1)).notify(member);
        then(memberService).shouldHaveNoInteractions(); // verify(memberService, never()).validate(any());
    }

}
