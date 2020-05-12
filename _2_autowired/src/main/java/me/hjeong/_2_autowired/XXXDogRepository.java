package me.hjeong._2_autowired;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
//@Primary
public class XXXDogRepository implements DogRepository {
}
// DogRepository 타입으로 빈을 주입받을 때
// @Primary가 붙은 타입이 우선순위가 제일 높은 후보가 된다.