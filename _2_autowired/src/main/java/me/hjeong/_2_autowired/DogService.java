package me.hjeong._2_autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DogService {
    @Autowired
    @Qualifier("XXXDogRepository")
    DogRepository dogRepository;
    // DogRepository interface를 MyDogRepository, AnotherDogRepository 에서 구현하고 있다.
    // 이 경우 어떤 DogRepository 를 써야 할 지 모른다.

    public void printDogRepository() {
        System.out.println(dogRepository.getClass());
    }
}
