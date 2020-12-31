package me.hjeong._2_mockito.study;

import me.hjeong._2_mockito.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

}
