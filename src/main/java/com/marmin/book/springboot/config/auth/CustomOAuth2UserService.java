package com.marmin.book.springboot.config.auth;

import com.marmin.book.springboot.config.auth.dto.OAuthAttributes;
import com.marmin.book.springboot.config.auth.dto.SessionUser;
import com.marmin.book.springboot.domain.user.User;
import com.marmin.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;


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
        OAuth2UserService<OAuth2UserRequest,OAuth2User>
                delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration().getRegistrationId();//현재 로그인 진행중인 서비스를 구분하는 코드
        //구글만 사용시에는 불필요하지만,이후 다른 소셜 서비스 연결시 네이버 로그인인지 구글 로그인인지 구분하기 위해 사용한다.
        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        //OAuth2 로그인 진행시 키가 되는 필드값을 이야기한다. Primary Key와 같은 의미이다.
        //구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오등은 지원하지 않는다. 구글의 기본 코드는 "sub"이다
        //이후 네이버 로그인과 구글 로그인을 동시 지원할때 사용된다

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());
        //OAuth2UserService를 통해 가져온 OAuth2User의 attribute 를 담을 클래스 입니다
        //다른 소셜 로그인 서비스 역시 이 클래스를 사용한다 *OAuth2Attributes
        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user",new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRolekey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity ->entity.update(attributes.getName()
                ,attributes.getPicture())).orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
