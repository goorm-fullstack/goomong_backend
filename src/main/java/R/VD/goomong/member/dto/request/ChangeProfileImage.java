package R.VD.goomong.member.dto.request;

import R.VD.goomong.image.model.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeProfileImage {

    private String memberId;
    private List<Image> imageList = new ArrayList<>();

    @Builder
    public ChangeProfileImage(String memberId, List<Image> imageList) {
        this.memberId = memberId;
        this.imageList = imageList;
    }
}
