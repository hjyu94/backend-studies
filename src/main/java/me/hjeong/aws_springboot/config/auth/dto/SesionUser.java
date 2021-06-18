package me.hjeong.aws_springboot.config.auth.dto;

import lombok.Getter;
import me.hjeong.aws_springboot.domain.user.User;

@Getter
public class SesionUser {

    // SessionUser 에는 인증된 사용자 정보만 필요하다. 그 외에 필요한 정보들은 없으니 name, email, picture만 필드로 선언합니다.

    private String name;
    private String email;
    private String picture;

    public SesionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }

}
