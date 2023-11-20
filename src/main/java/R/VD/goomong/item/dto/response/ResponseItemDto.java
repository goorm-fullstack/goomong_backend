package R.VD.goomong.item.dto.response;

import R.VD.goomong.ask.dto.response.ResponseAskDto;
import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.image.model.Image;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.item.model.ItemCategory;
import R.VD.goomong.item.model.ItemOption;
import R.VD.goomong.item.model.Status;
import R.VD.goomong.member.dto.response.ResponseMember;
import R.VD.goomong.review.dto.response.ResponseReviewDto;
import R.VD.goomong.review.model.Review;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseItemDto {
    private Long id;//DB 인덱스
    private String title;//제목
    private ResponseMember member;//작성자
    private int price;//가격
    private List<ItemOption> itemOptions;//아이템 옵션
    private List<Image> thumbNailList;//썸네일 리스트
    private String description;//설명
    private List<ResponseItemCategoryDto> itemCategories;//카테고리 목록
    private List<ResponseReviewDto> reviewList;//리뷰 목록
    private List<ResponseAskDto> askList;
    private Float rate;//평점
    private Status status;

    public ResponseItemDto(Item item) {

        this.id = item.getId();
        this.title = item.getTitle();
        this.member = new ResponseMember(item.getMember());
        this.price = item.getPrice();
        this.itemOptions = item.getItemOptions();
        this.thumbNailList = item.getThumbNailList();
        this.description = item.getDescribe();
        this.itemCategories = getCategoryDto(item.getItemCategories());
        this.reviewList = getReviewDto(item.getReviewList());
        this.askList = getAskDto(item.getAskList());
        this.rate = item.getRate();
        this.status = item.getStatus();
    }

    public List<ResponseItemCategoryDto> getCategoryDto(List<ItemCategory> categories) {
        List<ResponseItemCategoryDto> result = new ArrayList<>();
        for (ItemCategory category : categories)
            result.add(new ResponseItemCategoryDto(category));

        return result;
    }

    public List<ResponseAskDto> getAskDto(List<Ask> asks) {
        List<ResponseAskDto> askDtos = new ArrayList<>();
        for (Ask ask : asks) {
            askDtos.add(ask.toResponseAskDto());
        }
        return askDtos;
    }

    public List<ResponseReviewDto> getReviewDto(List<Review> reviews) {
        List<ResponseReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            reviewDtos.add(review.toResponseReviewDto());
        }

        return reviewDtos;
    }
}
