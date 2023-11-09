package R.VD.goomong.member.config;

import R.VD.goomong.member.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getMemberRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getMemberPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberName();
    }

    @Override
    public boolean isAccountNonExpired() {              //계정이 만료되었는가?
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {               //계정이 잠겨있는가?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {          //계정의 패스워드가 만료 되었는가?
        return true;
    }

    @Override
    public boolean isEnabled() {                        //계정 사용이 가능한가?

        //1년동안 사용 안하면 휴면 계정(휴면계정일 경우 false)
        return true;
    }
}
