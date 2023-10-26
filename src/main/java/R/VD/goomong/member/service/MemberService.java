package R.VD.goomong.member.service;

import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.dto.request.RequestUpdateDto;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    //CREATE
    //회원 가입
    public void save(RequestMember requestMember) {
        Member member = requestMember.toEntity();

        memberRepository.save(member);
    }

    //READ
    //모든 정보 찾기
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    //index로 회원 정보 찾기
    public Member findById(Long id){
        Optional<Member> member = memberRepository.findById(id);

        return member.orElse(null);
    }

    //회원 아이디로 정보 찾기
    public Member findByMemberId(String memberId){
        Optional<Member> member = memberRepository.findByMemberId(memberId);

        return member.orElse(null);
    }


    //UPDATE
    //memberId로 회원 정보 변경
    public Member updateMemberByMemberId(String memberId, RequestUpdateDto requestUpdate) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.update(requestUpdate.getMemberId(), requestUpdate.getMemberName(), requestUpdate.getMemberEmail(), requestUpdate.getMemberPassword());
            return memberRepository.save(member);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. : " + memberId);
        }
    }

    //index로 회원 정보 변경
    public Member updateMemberById(Long id, RequestUpdateDto requestUpdate) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.update(requestUpdate.getMemberId(), requestUpdate.getMemberName(), requestUpdate.getMemberEmail(), requestUpdate.getMemberPassword());
            return memberRepository.save(member);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. : " + id);
        }
    }

    //DELETE
    //REALDELETE
    //회원 index로 삭제
    public void deleteById(Long id){
        memberRepository.deleteById(id);
    }

    //회원 memberId로 삭제
    public void deleteByMemberId(String memberId){
        memberRepository.deleteByMemberId(memberId);
    }

    //SOFTDELETE
    //회원 index로 softdelete
    public Member softDeleteById(Long id){
        Optional<Member> member = memberRepository.findById(id);

        if (member.isPresent()) {
            Member member1 = member.get();

            String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            String now = LocalDateTime.now().format(formatter);
            LocalDateTime softDeleteTime = LocalDateTime.parse(now, formatter);

            member1.setMemberDeleteTime(softDeleteTime);

            return memberRepository.save(member1);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. " + id);
        }
    }


    //회원 memberId로 softdelete
    public Member softDeleteByMemberId(String memberId){
        Optional<Member> member = memberRepository.findByMemberId(memberId);

        if (member.isPresent()) {
            Member member1 = member.get();

            String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            String now = LocalDateTime.now().format(formatter);
            LocalDateTime softDeleteTime = LocalDateTime.parse(now, formatter);

            member1.setMemberDeleteTime(softDeleteTime);

            return memberRepository.save(member1);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. " + memberId);
        }
    }
}
