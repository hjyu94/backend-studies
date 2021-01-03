package me.hjeong._3_test_containers.study;

import me.hjeong._3_test_containers.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

}
