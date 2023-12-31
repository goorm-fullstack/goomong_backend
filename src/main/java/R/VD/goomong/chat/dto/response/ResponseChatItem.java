package R.VD.goomong.chat.dto.response;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.review.model.Review;
import lombok.Data;

import java.util.List;

@Data
public class ResponseChatItem {
    private Long id;//DB 인덱스
    private String title;//제목
    private String memberName;//작성자
    private int price;//가격
    private List<Image> thumbNailList;//썸네일 리스트
    private String description;//설명
    private List<ItemCategory> itemCategories;//카테고리 목록
    private List<Review> reviewList;//리뷰 목록
    private List<Ask> askList;
    private Float rate;//평점

    public ResponseChatItem(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.memberName = item.getMember().getMemberName();
        this.price = item.getPrice();
        this.thumbNailList = item.getThumbNailList();
        this.description = item.getDescription();
        this.itemCategories = item.getItemCategories();
        this.reviewList = item.getReviewList();
        this.askList = item.getAskList();
        this.rate = item.getRate();
    }
}