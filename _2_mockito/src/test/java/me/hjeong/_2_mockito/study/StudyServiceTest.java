package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.domain.Member;
import me.hjeong._2_mockito.member.MemberService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyServiceTest {

    @Mock private MemberService memberService;
    @Mock private StudyRepository repository;

    @Test
    void stubbing_test() {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);

        // default (without stubbing)
        Member byId = memberService.findById(1L); // null
        Optional<Member> byEmail = memberService.findByEmail("..."); // emtpy
        memberService.validate(1L); // nothing happened

        assertNull(byId);
        assertTrue(byEmail.isEmpty());

        // stubbing
        Member member = new Member();
        member.setId(1L);
        member.setEmail("hjeong@email.com");

        when(memberService.findById(1L)).thenReturn(member); // stubbing here.
        assertEquals(member, memberService.findById(1L));
        assertNotEquals(member, memberService.findById(2L));

        when(memberService.findById(any())).thenReturn(member); // stubbing here.
        assertEquals(member, memberService.findById(1L));
        assertEquals(member, memberService.findById(2L));

        when(memberService.findById(1L)).thenThrow(new RuntimeException());
        doThrow(new IllegalArgumentException()).when(memberService).validate(anyLong());
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });
        memberService.validate(2L); // didn't throw exception

        when(memberService.findById(any()))
                .thenReturn(member) // 1st
                .thenThrow(new RuntimeException()) // 2nd
                .thenReturn(null); // 3rd

        assertEquals(member, memberService.findById(1L)); // 1st test
        assertThrows(IllegalArgumentException.class, () -> { // 2nd test
            memberService.validate(2L);
        });
        assertNull(memberService.findById(3L));
    }

}