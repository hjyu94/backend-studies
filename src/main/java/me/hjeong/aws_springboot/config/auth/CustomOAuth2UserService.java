package me.hjeong.aws_springboot.config.auth;

import lombok.RequiredArgsConstructor;
import me.hjeong.aws_springboot.config.auth.dto.OAuthAttributes;
import me.hjeong.aws_springboot.config.auth.dto.SessionUser;
import me.hjeong.aws_springboot.domain.user.User;
import me.hjeong.aws_springboot.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // 구글인지? 네이버인지? ...
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드값. PK와 같은 의미
        // 구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않습니다. 구글의 경우 기본 코드는 "sub"입니다
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        /*
            세션에 사용자 정보를 저장하기 위한 Dto 클래스

            User 클래스를 쓰지 않고 새로 만들어서 쓰는가?
            만약 User 클래스를 그대로 사용하면 다음과 같은 에러 메세지가 출력된다.
            "Failed to convert from type [java.lang.Object] to type [byte[]] for value ..."

            이는 세션에 저장하기 위해 User 클래스를 세션에 저장하려고 하니 User 클래스에 직렬화를 구현하지 않았다는 의미읭 ㅔ러
            User 클래스는 엔티티이기 때문에 언제 다른 엔티티와 관계가 형성 될 지 모른다.
            예를 들어 @OneToMany, @ManyToMany 등 자식 엔티티를 갖고 있다면 직렬화 대상에 자식들까지 포함되니 성능이슈, 부수효과가 발생할 확률이 높기에
            차라리 User 를 직렬화 시키기보단 직렬화 기능을 가진 또다른 Dto 를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 도움이 된다.
        */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey()
        );
    }

    // 사용자가 업데이트 됐을 때를 대비하여 update 기능도 같이 구현한다
    // 이름이나, 프로필 사진이 변경 되었다면 User 엔티티에 반영한다
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
