package R.VD.goomong;

import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class GoomongApplication {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public static void main(String[] args) {
        SpringApplication.run(GoomongApplication.class, args);
    }

//    @PostConstruct
//    void init() {
//        Member member = Member.builder()
//                .memberId("test")
//                .build();
//        Post post = Post.builder()
//                .member(member)
//                .item(null)
//                .postCategory(null)
//                .postType("QnA")
//                .postTitle("title")
//                .postContent("content")
//                .build();
//
//        memberRepository.save(member);
//        postRepository.save(post);
//    }

}
