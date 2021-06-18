package me.hjeong.aws_springboot.config.auth;

import lombok.RequiredArgsConstructor;
import me.hjeong.aws_springboot.domain.user.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정을 활성화 시켜 줍니다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 한다
                .and() // URL별 권한 관리를 설정하는 옵션
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint() // OAuth2 로그인 성공 후 사용자 정보를 가져올 때 설정
                            // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
                            // 리소스 서버(즉 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
                            .userService(customOAuth2UserService)
                ;
    }

}
