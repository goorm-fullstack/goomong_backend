package R.VD.goomong.member.service;

import R.VD.goomong.member.dto.request.RequestSellerDto;
import R.VD.goomong.member.exception.NotFoundMember;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.model.Seller;
import R.VD.goomong.member.repository.SellerRepository;
import R.VD.goomong.order.model.Order;
import R.VD.goomong.review.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    // 판매자 저장
    public void save(RequestSellerDto requestSellerDto) {

        Seller entity = requestSellerDto.toEntity();
        sellerRepository.save(entity);
    }

    // 판매자 수정
    public Seller update(RequestSellerDto requestSellerDto) {
        Seller seller = sellerRepository.findByMemberId(requestSellerDto.getMemberId()).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + requestSellerDto.getMemberId()));

        Seller build = seller.toBuilder()
                .imagePath(requestSellerDto.getImage() != null ? requestSellerDto.getImage().getPath() : null)
                .saleSimpleAddress(requestSellerDto.getSaleSimpleAddress())
                .saleSido(requestSellerDto.getSaleSido())
                .saleDetailAddress(requestSellerDto.getSaleDetailAddress())
                .saleZipCode(requestSellerDto.getSaleZipCode())
                .memberId(requestSellerDto.getMemberId())
                .description(requestSellerDto.getDescription())
                .build();
        return sellerRepository.save(build);
    }

    // 판매자 아이디를 통해 찾기
    public Seller findOne(String memberId) {
        Seller seller = sellerRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + memberId));
        if (seller.getDelDate() != null) throw new RuntimeException("해당 아이디의 회원은 이미 삭제된 회원입니다. memberId = " + memberId);

        return seller;
    }

    // 판매자 삭제
    public void delete(String memberId) {
        Seller seller = sellerRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + memberId));
        if (seller.getDelDate() != null) throw new RuntimeException("해당 아이디의 회원은 이미 삭제된 회원입니다. memberId = " + memberId);

        sellerRepository.delete(seller);
    }

    // 판매자 리스트 조회
    public Page<Seller> all(Pageable pageable, String region) {
        Page<Seller> all = sellerRepository.findAll(pageable);
        List<Seller> list = new ArrayList<>();

        if (region != null) {
            for (Seller seller : all) {
                if (seller.getDelDate() == null && seller.getSaleSido() != null && region.contains(seller.getSaleSido()))
                    list.add(seller);
            }
            return new PageImpl<>(list, pageable, list.size());
        }

        for (Seller seller : all) {
            if (seller.getDelDate() == null) list.add(seller);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 회원 정보를 통해 판매자 삭제
    public void deleteSellerByMemberId(String memberId) {
        Seller seller = sellerRepository.findByMemberId(memberId).orElse(null);
        if (seller == null) return;
        sellerRepository.delete(seller);
    }

    // 회원 정보를 통해 판매자 저장
    public void saveSellerFromMember(Member member) {
        if (member.getSaleSido() != null) {
            Seller seller = sellerRepository.findByMemberId(member.getMemberId()).orElse(null);
            Seller build;
            if (seller != null) {
                build = seller.toBuilder()
                        .imagePath(member.getProfileImages().size() != 0 ? member.getProfileImages().get(0).getPath() : null)
                        .description(member.getSaleInfo() != null ? member.getSaleInfo() : null)
                        .saleDetailAddress(member.getSaleDetailAddress())
                        .saleSido(member.getSaleSido())
                        .saleSimpleAddress(member.getSaleSimpleAddress())
                        .memberId(member.getMemberId())
                        .saleZipCode(member.getSaleZipCode())
                        .email(member.getMemberEmail())
                        .name(member.getMemberName())
                        .build();
            } else {
                build = Seller.builder()
                        .imagePath(member.getProfileImages().size() != 0 ? member.getProfileImages().get(0).getPath() : null)
                        .description(member.getSaleInfo() != null ? member.getSaleInfo() : null)
                        .saleDetailAddress(member.getSaleDetailAddress())
                        .saleSido(member.getSaleSido())
                        .saleSimpleAddress(member.getSaleSimpleAddress())
                        .memberId(member.getMemberId())
                        .saleZipCode(member.getSaleZipCode())
                        .email(member.getMemberEmail())
                        .name(member.getMemberName())
                        .build();
            }
            sellerRepository.save(build);
        }
    }

    // 오더 정보를 통해 판매자 수익 업데이트
    public void updateIncomeByOrder(Order order) {
        Seller seller = sellerRepository.findByMemberId(order.getOrderItem().get(0).getMember().getMemberId()).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + order.getOrderItem().get(0).getMember().getMemberId()));

        Seller build = seller.toBuilder()
                .income(seller.getIncome() != null ? seller.getIncome() + order.getPrice() : order.getPrice())
                .transactionCnt(seller.getTransactionCnt() != null ? seller.getTransactionCnt() + 1 : 1)
                .build();
        sellerRepository.save(build);
    }

    // 오더 정보를 통해 판매자 수익 환불
    public void minusIncomeByOrder(Order order) {
        Seller seller = sellerRepository.findByMemberId(order.getOrderItem().get(0).getMember().getMemberId()).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + order.getOrderItem().get(0).getMember().getMemberId()));

        Seller build = seller.toBuilder()
                .income(seller.getIncome() - order.getPrice())
                .transactionCnt(seller.getTransactionCnt() - 1)
                .build();
        sellerRepository.save(build);
    }

    // 리뷰 정보를 통해 판매자 리뷰 수 증가 및 평점 계산
    public void updateReviewCntAndRateByReview(Review review) {
        Seller seller = sellerRepository.findByMemberId(review.getItem().getMember().getMemberId()).orElseThrow(() -> new NotFoundMember("해당 아이디의 회원을 찾을 수 없습니다. memberId = " + review.getItem().getMember().getMemberId()));

        Seller build = seller.toBuilder()
                .reviewCnt(seller.getReviewCnt() != null ? seller.getReviewCnt() + 1 : 1)
                .rate(seller.getRate() != null ? (seller.getRate() + review.getRate()) / (seller.getReviewCnt() + 1) : review.getRate())
                .build();
        sellerRepository.save(build);
    }
}
