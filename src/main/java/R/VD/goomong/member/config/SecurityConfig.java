//package R.VD.goomong.member.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AndRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                // stateless한 rest api를 개발할 것이므로 csrf 공격에 대한 옵션은 꺼둔다.
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(
//                        authorize -> authorize.requestMatchers(new AndRequestMatcher("/admin/login")).permitAll()
//                                .requestMatchers(new AndRequestMatcher("/admin/**")).hasRole("ADMIN")
//                                .requestMatchers(new AndRequestMatcher("/myaccount")).hasRole("ADMIN", "MEMBER")
//                                .requestMatchers(new AndRequestMatcher("/**")).permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(
//                        login -> login.loginPage("/login")
//                                .usernameParameter("memberId")
//                                .defaultSuccessUrl("/", true)
//                                .permitAll()
//                )
//                .logout(logout -> logout.logoutUrl("/logout")
//                        .permitAll()
//                )
//                .build();
//    }
//
//}
