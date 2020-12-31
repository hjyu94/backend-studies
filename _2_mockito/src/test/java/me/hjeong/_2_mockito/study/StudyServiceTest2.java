package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.member.MemberService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyServiceTest2 {

    @Mock MemberService memberService;
    @Mock StudyRepository repository;

    @Test
    void create_study_service_using_mock() {
        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);
    }

}