package com.marmin.book.springboot.config.auth;

import com.marmin.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;

@RequiredArgsConstructor
@EnableWebSecurity//Spring Security설정들을 활성화시켜줍니다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserTypesOAuth2UserService customUserTypesOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//h2-console을 사용하기 위해 해당 옵션들을 disable합니다
                .headers().frameOptions().disable()//h2콘솔 화면을 사용하기 위해 해당 옵션들을 disable 합니다
                .and()
                .authorizeRequests()//URL별 관한 관리를 설정하는 옵션의 시작점입니다
                .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**")
                //권한 관리 대상을 지정하는 옵션이다.URL HTTP메서드 별로 관리가 가능하다
                //"/"등 지정된 url들은 permitAll()옵션을 통해 전체 열람권한을 주었다
                //"/api/v1/**/"주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 했다
                .permitAll().antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .anyRequest()//설정된 값들 이외 나머지 URL들을 나타냅니다
                //여기서는 authenticated()을 추가하여 나머지 URL들은 모두 인증돈 사용자들에게만 허용하게 합니다
                //인증된 사용자 즉, 로그인한 사용자들을 이야기합니다
                .authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                //로그아웃 기능에 대한 여러 설정의 진입점입니다
                //로그아웃시 "/" 주소로 돌아갑니다
                .and()
                .oauth2Login()//OAuth2 로그인 기능에 대한 여러 설정의 진입점입니다
                .userInfoEndpoint()//OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당합니다
                .userService(customUserTypesOAuth2UserService); //소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의
        //구현체를 등록합니다
        //리소스 서버(즉,소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할수 있습니다

    }
}
