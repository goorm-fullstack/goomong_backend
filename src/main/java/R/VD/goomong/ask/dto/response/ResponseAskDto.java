package R.VD.goomong.ask.dto.response;

import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.item.model.Item;
import R.VD.goomong.member.model.Member;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseAskDto {
    private Long id;
    private Member member;//작성자
    private Item item;//작성할 아이템
    private String title;//문의 제목
    private String content;//문의 내용
    private Ask parent;//부모 문의
    private List<Ask> answer = new ArrayList<>();//답변 내용들

    public ResponseAskDto(Ask ask) {
        this.id = ask.getId();
        this.member = ask.getMember();
        this.item = ask.getItem();
        this.title = ask.getTitle();
        this.content = ask.getContent();
        this.parent = ask.getAsk();
        this.answer = ask.getAsks();
    }
}
