package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.domain.Member;
import me.hjeong._2_mockito.domain.Study;
import me.hjeong._2_mockito.member.InvalidMemberException;
import me.hjeong._2_mockito.member.MemberNotFoundException;
import me.hjeong._2_mockito.member.MemberService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyServiceTest1 {

    @Test
    void create_study_service_without_mock() {
        MemberService memberService = new MemberService() {
            @Override
            public void validate(Long memberId) throws InvalidMemberException {

            }

            @Override
            public Member findById(Long memberId) throws MemberNotFoundException {
                return null;
            }

            @Override
            public Optional<Member> findByEmail(String email) throws MemberNotFoundException {
                return Optional.empty();
            }
        };

        StudyRepository repository = new StudyRepository() {
            @Override
            public List<Study> findAll() {
                return null;
            }

            @Override
            public List<Study> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Study> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends Study> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Study> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Study> entities) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Study getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Study> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Study> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Study> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Study entity) {

            }

            @Override
            public void deleteAll(Iterable<? extends Study> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Study> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Study> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Study> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Study> boolean exists(Example<S> example) {
                return false;
            }
        };

        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);
    }


    @Test
    void create_study_service_using_mock() {
        MemberService memberService = mock(MemberService.class);
        StudyRepository repository = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberService, repository);
        assertNotNull(studyService);
    }

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

}