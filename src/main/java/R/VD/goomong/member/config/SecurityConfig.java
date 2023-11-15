package R.VD.goomong.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * H2 접근 차단 해제 설정이 추가되었습니다.
     *
     * @ 김경규 2023.11.14
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(withDefaults())
                .authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                                .anyRequest().permitAll()
                )
                .headers(AbstractHttpConfigurer::disable
                )
                .csrf(AbstractHttpConfigurer::disable
                )
                .formLogin(
                        AbstractAuthenticationFilterConfigurer::permitAll
                );

        return httpSecurity.build();
    }


}
