package me.hjeong._2_autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogService {
    @Autowired
    //@Qualifier("XXXDogRepository")
    DogRepository dogRepository;
    // DogRepository interface를 MyDogRepository, AnotherDogRepository 에서 구현하고 있다.
    // 이 경우 어떤 DogRepository 를 써야 할 지 모른다.

    @Autowired
    DogRepository XXXDogRepository;
    // 필드명을 빈 이름으로 지정하는 경우 자동으로 XXXDogRepository 타입으로 주입받는다.

    @Autowired
    List<DogRepository> dogRepositories;

    public void printDogRepository() {
        System.out.println(dogRepository.getClass());
    }
}
