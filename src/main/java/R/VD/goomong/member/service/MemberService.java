package R.VD.goomong.member.service;

import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findOneMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("없는 회원입니다."));
    }
}
