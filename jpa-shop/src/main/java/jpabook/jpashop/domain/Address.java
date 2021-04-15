package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable // 값 타입
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class Address {

    // 이런 규칙을 만들 수 있다는 점에서 값 타입을 만들면 편하다
    @Column(length = 10)
    private String city;
    @Column(length = 20)
    private String street;
    @Column(length = 5)
    private String zipcode;

    // Proxy 일 때를 생각해서 getter 를 사용한다
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }

    // 이런 메소드를 만들 수 있다는 점에서 값 타입을 만들면 편하다
    private String fullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }

}
