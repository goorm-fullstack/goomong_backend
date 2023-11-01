package R.VD.goomong.member.service;

import R.VD.goomong.member.config.PrincipalDetails;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


//
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byMemberName = memberRepository.findByMemberName(username);
        Member member = byMemberName.get();
        if(member != null){
            return new PrincipalDetails(member);
        }
        return null;
    }
}
