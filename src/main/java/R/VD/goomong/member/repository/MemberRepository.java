package R.VD.goomong.member.repository;

import R.VD.goomong.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //READ
    //회원 index로 찾기
    Optional<Member> findById(Long id);

    //회원 memberId로 찾기
    Optional<Member> findByMemberId(String memberId);

    //회원 memberEmail로 찾기
    Optional<Member> findByMemberEmail(String memberEmail);

    //DELETE
    //회원 index로 삭제하기
    void deleteById(Long id);

    //회원 memberId로 삭제하기
    void deleteByMemberId(String memberId);

    Optional<Member> findByMemberName(String memberName);
}
