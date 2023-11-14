package R.VD.goomong.item.dto.response;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.review.model.Review;
import lombok.Data;

import java.util.List;

@Data
public class ResponseItemDto {
    private Long id;//DB 인덱스
    private String title;//제목
    //    private String member;//작성자
    private int price;//가격
    private List<Image> thumbNailList;//썸네일 리스트
    private String describe;//설명
    //    private List<ResponseItemCategoryDto> itemCategories;//카테고리 목록
    private List<ItemCategory> itemCategories;
    //    private List<ResponseReviewDto> reviewList;//리뷰 목록
    private List<Review> reviewList;
    //    private List<ResponseAskDto> askList;
    private List<Ask> askList;
    private Float rate;//평점
    private Status status;

    public ResponseItemDto(Item item) {
//        List<ResponseItemCategoryDto> categoryDtoList = new ArrayList<>();
//        for (ItemCategory itemCategory : item.getItemCategories()) {
//            categoryDtoList.add(new ResponseItemCategoryDto(itemCategory));
//        }

        this.id = item.getId();
        this.title = item.getTitle();
//        this.member = item.getMember().getMemberId();
        this.price = item.getPrice();
        this.thumbNailList = item.getThumbNailList();
        this.describe = item.getDescribe();
//        this.itemCategories = categoryDtoList;
        this.itemCategories = item.getItemCategories();
//        this.reviewList = item.getReviewList().stream().map(Review::toResponseReviewDto).toList();
        this.reviewList = item.getReviewList();
//        this.askList = item.getAskList().stream().map(Ask::toResponseAskDto).toList();
        this.askList = item.getAskList();
        this.rate = item.getRate();
        this.status = item.getStatus();
    }
}
