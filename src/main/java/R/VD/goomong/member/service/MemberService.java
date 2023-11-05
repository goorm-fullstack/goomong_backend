package R.VD.goomong.member.service;

import R.VD.goomong.member.dto.request.RequestLogin;
import R.VD.goomong.member.dto.request.RequestMember;
import R.VD.goomong.member.dto.request.RequestUpdateDto;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    //CREATE
    //아이디 중복 체크
    private boolean isId(String memberId){
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);
        if(byMemberId.isEmpty()){
            return true;                        //이미 생성된 아이디가 없음
        }
        else
            return false;                         //이미 생성된 아이디가 있음
    }
    //비밀번호 체크
    private boolean isPassword(String memberPassword, String memberId) {
        int minLength = 9;
        int maxLength = 20;
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?]");
        Matcher matcher = specialCharPattern.matcher(memberPassword);

        if(memberPassword.length() < minLength || memberPassword.length() >= maxLength) {               //비밀번호 9자 이상 20자 미만
            throw new NotFoundMember("비밀번호는 9자 이상 20자 이하이어야 합니다.");
        }

        if(memberPassword.contains(memberId)){
            throw new NotFoundMember("비밀번호에 ID를 포함할 수 없습니다.");
        }

        if (!matcher.find()) {
            throw new NotFoundMember("비밀번호에 특수문자가 최소 1개 포함되어야 합니다.");
        }

        return true;
    }

    //이메일 중복 체크
    private boolean isEmail(String memberEmail) {
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if(byMemberEmail.isEmpty())                 //존재할 때
            return true;
        else                                        //없을 때
            return false;

    }
    //회원 가입
    public void save(RequestMember requestMember) {
        if(isId(requestMember.getMemberId())==false){
            throw new NotFoundMember("이미 존재하는 아이디입니다.");
        }
        isPassword(requestMember.getMemberPassword(), requestMember.getMemberId());
        if(isEmail(requestMember.getMemberEmail())==false){
            throw new NotFoundMember("이미 존재하는 이메일입니다.");
        }
        Member member = requestMember.toEntity();

        member.setMemberRole("MEMBER");
        String rawPassword = member.getMemberPassword();
        String encodePassword = encoder.encode(rawPassword);
        member.setMemberPassword(encodePassword);

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
            member.update(requestUpdate.getMemberId(), requestUpdate.getMemberPassword(), requestUpdate.getMemberName(), requestUpdate.getMemberEmail(), requestUpdate.getZipCode(), requestUpdate.getSimpleAddress(), requestUpdate.getDetailAddress());
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
            member.update(requestUpdate.getMemberId(), requestUpdate.getMemberPassword(), requestUpdate.getMemberName(), requestUpdate.getMemberEmail(), requestUpdate.getZipCode(), requestUpdate.getSimpleAddress(), requestUpdate.getDetailAddress());
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

    //로그인
    public Member memberLogin(RequestLogin requestLogin){
        Optional<Member> byMemberId = memberRepository.findByMemberId(requestLogin.getMemberId());

        if(byMemberId.isEmpty()){                                               //아이디 없음
            throw new NotFoundMember("아이디 없음.");
        }

        Member member = byMemberId.get();
        if(!encoder.matches(requestLogin.getMemberPassword(), member.getMemberPassword())){                 //비밀번호 틀림
            member.setMemberLoginFailed(member.getMemberLoginFailed() + 1L);
            memberRepository.save(member);

            throw new NotFoundMember("비밀번호 불일치");
        }
        else{                                                                       //로그인 성공
            if(member.getMemberLoginFailed() == 5L){
                throw new NotFoundMember("회원 잠김");
            }
            else{
                member.setMemberLoginFailed(0L);
                memberRepository.save(member);
            }
        }
        return member;
    }
}
