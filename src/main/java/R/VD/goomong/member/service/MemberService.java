package R.VD.goomong.member.service;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.image.service.ImageService;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.dto.request.*;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.support.repository.EmailMemberSaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final ImageService imageService;
    private final EmailMemberSaveRepository emailMemberSaveRepository;
    private final SellerService sellerService;

    //CREATE
    //아이디 중복 체크
    private boolean isId(String memberId) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);
        if (byMemberId.isEmpty()) {
            return true;                        //이미 생성된 아이디가 없음
        } else
            return false;                         //이미 생성된 아이디가 있음
    }

    //비밀번호 체크
    private boolean isPassword(String memberPassword, String memberId) {
        int minLength = 9;
        int maxLength = 20;
        Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?]");
        Matcher matcher = specialCharPattern.matcher(memberPassword);

        if (memberPassword.length() < minLength || memberPassword.length() >= maxLength) {               //비밀번호 9자 이상 20자 미만
            throw new NotFoundMember("비밀번호는 9자 이상 20자 미만이어야 합니다.");
        }

        if (memberPassword.contains(memberId)) {
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
        if (byMemberEmail.isEmpty())                 //존재할 때
            return true;
        else                                        //없을 때
            return false;

    }

    //회원 가입
    public void save(RequestMember requestMember) {
        if (isId(requestMember.getMemberId()) == false) {
            throw new NotFoundMember("이미 존재하는 아이디입니다.");
        }
        isPassword(requestMember.getMemberPassword(), requestMember.getMemberId());
        if (isEmail(requestMember.getMemberEmail()) == false) {
            throw new NotFoundMember("이미 존재하는 이메일입니다.");
        }
        Member member = requestMember.toEntity();

        member.setMemberRole("MEMBER");
        String rawPassword = member.getMemberPassword();
        String encodePassword = encoder.encode(rawPassword);
        member.setMemberPassword(encodePassword);
        emailMemberSaveRepository.deleteByMemberEmail(member.getMemberEmail());

        memberRepository.save(member);
    }

    //READ
    //모든 정보 찾기
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    //index로 회원 정보 찾기
    public Member findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        return member.orElse(null);
    }

    //회원 아이디로 정보 찾기
    public Member findByMemberId(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);

        return member.orElse(null);
    }

    //이메일로 회원 찾기
    public Member findByMemberEmail(String memberEmail) {
        Optional<Member> member = memberRepository.findByMemberEmail(memberEmail);

        return member.orElse(null);
    }

    //이름으로 회원 찾기
    public Member findByMemberName(String memberName) {
        Optional<Member> member = memberRepository.findByMemberName(memberName);

        return member.orElse(null);
    }

    //별명과 이메일로 회원 찾기
    public Member findByMemberNameAndMemberEmail(String memberName, String email) {
        Optional<Member> member = memberRepository.findByMemberNameAndMemberEmail(memberName, email);

        return member.orElse(null);
    }


    //UPDATE
    //memberId로 회원 정보 변경
    public Member updateMemberByMemberId(RequestUpdateDto requestUpdate) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(requestUpdate.getMemberId());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            if (requestUpdate.getMemberPassword() != null) {
                String rawPassword = requestUpdate.getMemberPassword();
                String encodePassword = encoder.encode(rawPassword);
                member.setMemberPassword(encodePassword);
            }

            if (requestUpdate.getMemberName() != null) {
                member.setMemberName(requestUpdate.getMemberName());
            }

            if (requestUpdate.getMemberEmail() != null) {
                member.setMemberEmail(requestUpdate.getMemberEmail());
            }

            if (requestUpdate.getBuyZipCode() != null) {
                member.setBuyZipCode(requestUpdate.getBuyZipCode());
            }

            if (requestUpdate.getBuySido() != null) {
                member.setBuySido(requestUpdate.getBuySido());
            }

            if (requestUpdate.getBuySimpleAddress() != null) {
                member.setBuySimpleAddress(requestUpdate.getBuySimpleAddress());
            }

            if (requestUpdate.getBuyDetailAddress() != null) {
                member.setBuyDetailAddress(requestUpdate.getBuyDetailAddress());
            }

            if (requestUpdate.getSaleZipCode() != null) {
                member.setSaleZipCode(requestUpdate.getSaleZipCode());
            }

            if (requestUpdate.getSaleSido() != null) {
                member.setSaleSido(requestUpdate.getSaleSido());
            }

            if (requestUpdate.getSaleSimpleAddress() != null) {
                member.setSaleSimpleAddress(requestUpdate.getSaleSimpleAddress());
            }

            if (requestUpdate.getSaleDetailAddress() != null) {
                member.setSaleDetailAddress(requestUpdate.getSaleDetailAddress());
            }

            if (requestUpdate.getSaleInfo() != null) {
                member.setSaleInfo(requestUpdate.getSaleInfo());
            }

            sellerService.saveSellerFromMember(member);

            return memberRepository.save(member);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. : " + requestUpdate.getMemberId());
        }
    }

    //index로 회원 정보 변경
    public Member updateMemberById(Long id, RequestUpdateDto requestUpdate) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
//            if (multipartFiles != null) {
//                List<Image> images = imageService.saveImage(multipartFiles);
//                member.setProfileImages(images);
//            }

            member.memberUpdate(requestUpdate.getMemberId(), requestUpdate.getMemberPassword(), requestUpdate.getMemberName(), requestUpdate.getMemberEmail(), requestUpdate.getBuyZipCode(), requestUpdate.getBuySido(), requestUpdate.getBuySimpleAddress(), requestUpdate.getBuyDetailAddress(), requestUpdate.getSaleZipCode(), requestUpdate.getSaleSido(), requestUpdate.getSaleSimpleAddress(), requestUpdate.getSaleDetailAddress(), requestUpdate.getSaleInfo());

            sellerService.saveSellerFromMember(member);

            return memberRepository.save(member);

        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. : " + id);
        }
    }

    //memberId로 비밀번호 변경
    public Member changePasswordByMemberId(RequestChangePassword requestChangePassword) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(requestChangePassword.getMemberId());

        if (byMemberId.isPresent()) {
            Member member = byMemberId.get();

            if (encoder.matches(requestChangePassword.getMemberPassword(), member.getMemberPassword())) {
                String rawPassword2 = requestChangePassword.getNewPassword();
                String newPassword = encoder.encode(rawPassword2);

                member.changePassword(requestChangePassword.getMemberId(), newPassword);
                sellerService.saveSellerFromMember(member);
                return memberRepository.save(member);
            } else throw new NotFoundMember("현재 비밀번호가 틀렸습니다.");

        } else
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다.");
    }

    //memberId로 비밀번호 찾기
    public Member findPasswordByMemberId(RequestFindPassword requestFindPassword) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(requestFindPassword.getMemberId());

        if (byMemberId.isPresent()) {
            Member member = byMemberId.get();

            String rawPassword = requestFindPassword.getNewPassword();
            String newPassword = encoder.encode(rawPassword);

            member.changePassword(requestFindPassword.getMemberId(), newPassword);
            sellerService.saveSellerFromMember(member);

            return memberRepository.save(member);

        } else
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다.");
    }

    //memberId로 이메일 변경
    public Member changeEmailByMemberId(RequestChangeEmail requestChangeEmail) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(requestChangeEmail.getMemberId());

        if (byMemberId.isPresent()) {
            Member member = byMemberId.get();
            member.changeEmail(requestChangeEmail.getMemberId(), requestChangeEmail.getMemberEmail());
            sellerService.saveSellerFromMember(member);

            return memberRepository.save(member);
        } else
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다.");
    }

    //memberId로 프로필사진 저장
    public Member changeProfileImageByMemberId(String memberId, MultipartFile[] multipartFiles) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(memberId);

        if (byMemberId.isPresent()) {
            Member member = byMemberId.get();
            List<Image> imageList = null;
            if (multipartFiles.length != 0)
                imageList = imageService.saveImage(multipartFiles);

            member.changeProfileImage(member.getMemberId(), imageList);
            sellerService.saveSellerFromMember(member);

            return memberRepository.save(member);
        } else throw new NotFoundMember("회원 아이디를 찾을 수 없습니다.");

    }

    //DELETE
    //REALDELETE
    //회원 index로 삭제
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    //회원 memberId로 삭제
    public void deleteByMemberId(String memberId) {
        memberRepository.deleteByMemberId(memberId);
    }

    //SOFTDELETE
    //회원 index로 softdelete
    public Member softDeleteById(Long id) {
        Optional<Member> member = memberRepository.findById(id);

        if (member.isPresent()) {
            Member member1 = member.get();

            String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            String now = LocalDateTime.now().format(formatter);
            LocalDateTime softDeleteTime = LocalDateTime.parse(now, formatter);

            member1.setDelDate(softDeleteTime);
            sellerService.deleteSellerByMemberId(member1.getMemberId());

            return memberRepository.save(member1);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. " + id);
        }
    }


    //회원 memberId로 softdelete
    public Member softDeleteByMemberId(String memberId) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);

        if (member.isPresent()) {
            Member member1 = member.get();

            String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            String now = LocalDateTime.now().format(formatter);
            LocalDateTime softDeleteTime = LocalDateTime.parse(now, formatter);

            member1.setDelDate(softDeleteTime);
            sellerService.deleteSellerByMemberId(member1.getMemberId());

            return memberRepository.save(member1);
        } else {
            throw new NotFoundMember("회원 아이디를 찾을 수 없습니다. " + memberId);
        }
    }

    //로그인
    @Transactional(rollbackFor = Exception.class) // 예외가 발생하면 롤백하도록 설정
    public Member memberLogin(RequestLogin requestLogin) {
        Optional<Member> byMemberId = memberRepository.findByMemberId(requestLogin.getMemberId());

        if (byMemberId.isEmpty()) { // 아이디 없음
            throw new NotFoundMember("아이디 없음.");
        }

        Member member = byMemberId.get();
        if (!encoder.matches(requestLogin.getMemberPassword(), member.getMemberPassword())) { // 비밀번호 틀림
            member.incrementLoginFailed(); // 로그인 실패 횟수 증가
            memberRepository.save(member); // 실패 횟수를 저장

            if (member.getMemberLoginFailed() >= 5L) { // 5회 이상 로그인 실패 시
                throw new NotFoundMember("회원 잠김");
            } else {
                throw new NotFoundMember("비밀번호 불일치");
            }
        } else { // 아이디, 비밀번호 모두 맞췄을 때
            member.resetLoginFailed(); // 로그인 실패 횟수 초기화
            memberRepository.save(member); // 초기화된 실패 횟수를 저장
        }
        return member;
    }

    // 회원 아이디로 상품 리스트 가져오기 - @배진환
    public Page<Item> getMemberItem(String memberId, Pageable pageable){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + memberId));

        List<Item> itemList = member.getItemList();
        List<Item> list = new ArrayList<>();
        for (Item item : itemList) {
            if(item.getDelDate() == null) list.add(item);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 회원 아이디로 리뷰 리스트 가져오기 - @배진환
    public Page<Review> getMemberReview(String memberId, Pageable pageable){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + memberId));

        List<Review> reviewList = new ArrayList<>();
        for (Item item : member.getItemList()) {
            for (Review review : item.getReviewList()) {
                if(review.getDelDate() == null) reviewList.add(review);
            }
        }
        return new PageImpl<>(reviewList, pageable, reviewList.size());
    }
}


class LoginFail {

    private final MemberRepository memberRepository;

    public LoginFail(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(noRollbackFor = Exception.class)
    public void extracted(Member member) throws Exception {
        member.setMemberLoginFailed(member.getMemberLoginFailed() + 1L);
        memberRepository.save(member);

        throw new Exception("비밀번호 불일치");
    }
}