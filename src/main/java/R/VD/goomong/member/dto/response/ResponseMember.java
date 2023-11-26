package R.VD.goomong.member.dto.response;

import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.dto.response.ResponseItemDto;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.like.dto.response.ResponseLikeDto;
import R.VD.goomong.like.model.Like;
import R.VD.goomong.member.model.Member;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseMember {

    private String memberId;

    private String memberPassword;

    private String memberName;

    private String memberEmail;

    private String saleSido;

    private List<ResponseLikeDto> likeList; // 필요해서 추가합니다! @배진환 // 확인했습니다. @동규

    private String memberAddress; // @배진환

    private LocalDateTime memberSignupTime;

    private List<Image> profileImages;

    private List<ResponseItemDto> itemList;

    public ResponseMember(String memberId, String memberPassword, String memberName, String memberEmail, String saleSido, List<ResponseLikeDto> likeList, LocalDateTime memberSignupTime, List<Image> profileImages, List<ResponseItemDto> itemList) {
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.saleSido = saleSido;
        this.likeList = likeList;
        this.memberSignupTime = memberSignupTime;
        this.profileImages = profileImages;
        this.itemList = itemList;
    }

    /**
     * 파라미터가 여러 개로 이루어진 생성자는 가독성이 좋지 않다고 생각해서
     * 임의로 생성자를 하나더 만들었습니다.
     *
     * @ 김경규 - 2023.11.14
     * @ 이동규 - 확인했습니다.
     */
    public ResponseMember(Member member) {
        this.memberId = member.getMemberId();
        this.memberPassword = member.getMemberPassword();
        this.memberName = member.getMemberName();
        this.memberEmail = member.getMemberEmail();
        this.memberSignupTime = member.getMemberSignupTime();
        this.memberAddress = member.getSaleSimpleAddress() != null ? member.getSaleSimpleAddress() : member.getBuySimpleAddress() != null ? member.getBuySimpleAddress() : null;
        this.saleSido = member.getSaleSido();
        this.likeList = member.getLikeList() != null ? member.getLikeList().stream().map(Like::toResponseLikeDto).toList() : null; // likeList 필드 추가에 따른 추가 @배진환
        this.profileImages = member.getProfileImages();
        this.itemList = member.getItemList().stream().map(ResponseItemDto::new).toList();
    }
}
