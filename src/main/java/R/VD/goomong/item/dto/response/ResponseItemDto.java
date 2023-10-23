package goomong.item.dto.response;

import goomong.ask.model.Ask;
import goomong.image.model.Image;
import goomong.item.model.Item;
import goomong.item.model.ItemCategory;
import goomong.member.model.Member;
import goomong.review.model.Review;
import lombok.Data;

import java.util.List;

@Data
public class ResponseItemDto {
    private Long id;//DB 인덱스
    private String title;//제목
    private Member member;//작성자
    private int price;//가격
    private List<Image> thumbNailList;//썸네일 리스트
    private String describe;//설명
    private List<ItemCategory> itemCategories;//카테고리 목록
    private List<Review> reviewList;//리뷰 목록
    private List<Ask> askList;
    private Float rate;//평점

    public ResponseItemDto(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.member = item.getMember();
        this.price = item.getPrice();
        this.thumbNailList = item.getThumbNailList();
        this.describe = item.getDescribe();
        this.itemCategories = item.getItemCategories();
        this.reviewList = item.getReviewList();
        this.askList = item.getAskList();
        this.rate = item.getRate();
    }
}
