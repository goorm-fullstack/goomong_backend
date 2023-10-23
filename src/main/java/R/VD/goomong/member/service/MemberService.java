package goomong.member.service;

import goomong.member.model.Member;
import goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    // 테스트용 코드
    public void save(Member member) {
        memberRepository.save(member);
    }
}
