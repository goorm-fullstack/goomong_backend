package R.VD.goomong.member.model;

import R.VD.goomong.global.model.BaseTimeEntity;
import R.VD.goomong.member.dto.response.ResponseSellerDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Seller extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberId; // 회원 아이디

    @Column(nullable = false)
    private String email; // 회원 이메일

    @Column(nullable = false)
    private String name; // 별명

    @Column(length = 50000)
    private String description; // 간단 소개

    @Column
    private Long income; // 총수익

    @Column
    private Float rate; // 평정

    @Column(nullable = false)
    private Long saleZipCode; //판매자 우편 번호

    @Column(nullable = false)
    private String saleSido; //판매자 시/도

    @Column(nullable = false)
    private String saleSimpleAddress; //판매자 간단 주소

    @Column(nullable = false)
    private String saleDetailAddress; //판매자 상세 주소

    @Column
    private String imagePath; // 썸네일

    @Column
    private Long transactionCnt; // 거래수

    @Column
    private Long reviewCnt; // 리뷰수

    @Column
    private LocalDateTime delDate; // 삭제날짜

    public ResponseSellerDto toResponseSellerDto() {
        return ResponseSellerDto.builder()
                .description(description)
                .name(name)
                .reviewCnt(reviewCnt)
                .income(income)
                .rate(rate)
                .transactionCnt(transactionCnt)
                .saleSimpleAddress(saleSimpleAddress)
                .saleZipCode(saleZipCode)
                .saleDetailAddress(saleDetailAddress)
                .id(id)
                .saleSido(saleSido)
                .memberId(memberId)
                .imagePath(imagePath)
                .regDate(this.getRegDate())
                .build();
    }
}
