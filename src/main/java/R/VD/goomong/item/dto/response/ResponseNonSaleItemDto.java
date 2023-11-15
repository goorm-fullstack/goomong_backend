package R.VD.goomong.item.dto.response;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.review.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "판매가 아닌 아이템 DTO")
public class ResponseNonSaleItemDto {
    @Schema(description = "DB 인덱스")
    private Long id;//DB 인덱스

    @Schema(description = "제목")
    private String title;//제목
    @Schema(description = "작성자")
    private Member member;//작성자
    @Schema(description = "등록한 이미지 리스트")
    private List<Image> thumbNailList;//썸네일 리스트
    @Schema(description = "아이템에 대한 설명")
    private String describe;//설명
    @Schema(description = "아이템 카테고리 목록")
    private List<ItemCategory> itemCategories;//카테고리 목록
    @Schema(description = "현재 아이템에 등록된 리뷰 목록")
    private List<Review> reviewList;//리뷰 목록
    @Schema(description = "현재 아이템에 등록된 문의 목록")
    private List<Ask> askList;
    @Schema(description = "아이템 평점")
    private Float rate;//평점
    @Schema(description = "아이템 상태 분류")
    private Status status;

    public ResponseNonSaleItemDto(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.member = item.getMember();
        this.thumbNailList = item.getThumbNailList();
        this.describe = item.getDescribe();
        this.itemCategories = item.getItemCategories();
        this.reviewList = item.getReviewList();
        this.askList = item.getAskList();
        this.rate = item.getRate();
        this.status = item.getStatus();
    }
}
