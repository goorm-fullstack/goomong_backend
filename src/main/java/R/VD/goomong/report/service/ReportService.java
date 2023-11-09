package R.VD.goomong.report.service;

import R.VD.goomong.ask.exception.AlreadyDeletedAskException;
import R.VD.goomong.ask.exception.NotFoundAsk;
import R.VD.goomong.ask.model.Ask;
import R.VD.goomong.ask.repository.AskRepository;
import R.VD.goomong.comment.exception.AlreadyDeletedCommentException;
import R.VD.goomong.comment.exception.NotExistCommentException;
import R.VD.goomong.comment.model.Comment;
import R.VD.goomong.comment.repository.CommentRepository;
import R.VD.goomong.file.model.Files;
import R.VD.goomong.file.service.FilesService;
import R.VD.goomong.member.model.Member;
import R.VD.goomong.member.repository.MemberRepository;
import R.VD.goomong.post.exception.AlreadyDeletePostException;
import R.VD.goomong.post.exception.NotExistPostException;
import R.VD.goomong.post.model.Post;
import R.VD.goomong.post.repository.PostRepository;
import R.VD.goomong.report.dto.request.RequestReportDto;
import R.VD.goomong.report.exception.*;
import R.VD.goomong.report.model.Report;
import R.VD.goomong.report.repository.ReportRepository;
import R.VD.goomong.review.exception.AlreadyDeletedReviewException;
import R.VD.goomong.review.exception.NotFoundReview;
import R.VD.goomong.review.model.Review;
import R.VD.goomong.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final AskRepository askRepository;
    private final FilesService filesService;

    // 신고글 생성
    public void saveReport(RequestReportDto requestReportDto, MultipartFile[] reportFiles) {
        Report entity = requestReportDto.toEntity();

        Member member = null;
        if (requestReportDto.getMemberId() != null) {
            member = memberRepository.findById(requestReportDto.getMemberId()).orElseThrow(() -> new RuntimeException("해당 id의 회원은 없습니다. id = " + requestReportDto.getMemberId()));
            if (member.getDelDate() != null) throw new RuntimeException("해당 id의 회원은 삭제된 회원입니다. id = " + member.getId());
        }

        Post post = null;
        if (requestReportDto.getPostId() != null) {
            post = postRepository.findById(requestReportDto.getPostId()).orElseThrow(() -> new NotExistPostException("해당 id의 게시글은 없습니다. id = " + requestReportDto.getPostId()));
            if (post.getDelDate() != null)
                throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 게시글입니다. id = " + post.getId());

            if (post.getReportList().size() != 0) {
                for (Report report : post.getReportList()) {
                    if (report.getDelDate() == null)
                        throw new AlreadyReportedException("해당 게시글은 이미 신고된 글입니다. id = " + requestReportDto.getPostId());
                }
            }
        }

        Comment comment = null;
        if (requestReportDto.getCommentId() != null) {
            comment = commentRepository.findById(requestReportDto.getCommentId()).orElseThrow(() -> new NotExistCommentException("해당 id의 댓글은 없습니다. id = " + requestReportDto.getCommentId()));
            if (comment.getDelDate() != null)
                throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + comment.getId());

            if (comment.getReportList().size() != 0) {
                for (Report report : comment.getReportList()) {
                    if (report.getDelDate() == null)
                        throw new AlreadyReportedException("해당 댓글은 이미 신고된 댓글입니다. id = " + requestReportDto.getCommentId());
                }
            }
        }

        Review review = null;
        if (requestReportDto.getReviewId() != null) {
            review = reviewRepository.findById(requestReportDto.getReviewId()).orElseThrow(() -> new NotFoundReview("해당 id의 리뷰글을 찾을 수 없습니다. id = " + requestReportDto.getReviewId()));
            if (review.getDelDate() != null)
                throw new AlreadyDeletedReviewException("해당 id의 리뷰글은 삭제된 리뷰글입니다. id = " + review.getId());

            if (review.getReportList().size() != 0) {
                for (Report report : review.getReportList()) {
                    if (report.getDelDate() == null)
                        throw new AlreadyReportedException("해당 id의 리뷰글은 이미 신고된 리뷰글입니다. id = " + review.getId());
                }
            }
        }

        Ask ask = null;
        if (requestReportDto.getAskId() != null) {
            ask = askRepository.findById(requestReportDto.getAskId()).orElseThrow(() -> new NotFoundAsk("해당 id의 문의글을 찾을 수 없습니다. id = " + requestReportDto.getAskId()));
            if (ask.getDelDate() != null)
                throw new AlreadyDeletedAskException("해당 id의 문의글은 삭제된 문의글입니다. id = " + ask.getId());

            if (ask.getReportList().size() != 0) {
                for (Report report : ask.getReportList()) {
                    if (report.getDelDate() == null)
                        throw new AlreadyReportedException("해당 id의 문의글은 이미 신고된 문의글입니다. id = " + ask.getId());
                }
            }
        }

        List<Files> files = new ArrayList<>();
        if (reportFiles.length != 0) files = filesService.saveFiles(reportFiles);

        Report build = entity.toBuilder()
                .member(member)
                .post(post)
                .comment(comment)
                .review(review)
                .ask(ask)
                .filesList(files)
                .build();
        reportRepository.save(build);
    }

    // 신고글 수정
    public Report updateReport(Long reportId, RequestReportDto requestReportDto, MultipartFile[] reportFiles) {
        Report origin = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 신고글입니다. id = " + reportId);

        List<Files> files = null;
        if (reportFiles.length != 0) files = filesService.saveFiles(reportFiles);

        Report build = origin.toBuilder()
                .reportReason(requestReportDto.getReportReason())
                .filesList(files)
                .build();
        return reportRepository.save(build);
    }

    // 신고글 소프트딜리트
    public void softDeleteReport(Long reportId) {
        Report origin = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));
        if (origin.getDelDate() != null) throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 신고글입니다.");

        Report build = origin.toBuilder()
                .delDate(ZonedDateTime.now())
                .build();
        reportRepository.save(build);
    }

    // 신고글 완전삭제
    public void deleteReport(Long reportId) {
        Report origin = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));
        if (origin.getDelDate() != null) throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 신고글입니다.");
        reportRepository.delete(origin);
    }

    // 삭제된 신고글 복구
    public void unDeleteReport(Long reportId) {
        Report origin = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));
        if (origin.getDelDate() == null)
            throw new NotDeletedReportException("해당 id의 신고글은 삭제된 글이 아닙니다. id = " + reportId);

        Report build = origin.toBuilder()
                .delDate(null)
                .build();
        reportRepository.save(build);
    }

    // 신고글 이상 없음 처리
    public void noProblemReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글을 찾을 수 없습니다. id = " + reportId));
        if (report.getReportResult() != null)
            throw new AlreadyCheckedReportException("해당 id의 신고글은 이미 처리된 신고글입니다. id = " + reportId);
        if (report.getDelDate() != null)
            throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 글입니다. id = " + reportId);
        initReportResult(reportId, "이상 없음");
    }

    // 신고글 삭제 처리
    public void checkReportDelete(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글을 찾을 수 없습니다. id = " + reportId));
        if (report.getReportResult() != null)
            throw new AlreadyCheckedReportException("해당 id의 신고글은 이미 처리된 신고글입니다. id = " + reportId);
        if (report.getDelDate() != null)
            throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 글입니다. id = " + reportId);
        initReportResult(reportId, "삭제 처리");

        if (report.getComment() != null) {
            Comment comment = report.getComment();
            if (comment.getDelDate() != null)
                throw new AlreadyDeletedCommentException("해당 id의 댓글은 삭제된 댓글입니다. id = " + comment.getId());
            Comment build = comment.toBuilder()
                    .delDate(ZonedDateTime.now())
                    .build();
            commentRepository.save(build);
        }

        if (report.getPost() != null) {
            Post post = report.getPost();
            if (post.getDelDate() != null)
                throw new AlreadyDeletePostException("해당 id의 게시글은 삭제된 게시글입니다. id = " + post.getId());
            Post build = post.toBuilder()
                    .delDate(ZonedDateTime.now())
                    .build();
            postRepository.save(build);
        }
    }

    // 특정 신고글 조회
    public Report findOneReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));
        if (report.getDelDate() != null)
            throw new AlreadyDeletedReportException("해당 id의 신고글은 삭제된 신고글입니다. id = " + reportId);
        return report;
    }

    // 삭제되지 않은 신고글 조회
    public Page<Report> listOfNotDeleted(Pageable pageable) {
        Page<Report> all = reportRepository.findAll(pageable);
        List<Report> list = new ArrayList<>();

        for (Report report : all) {
            if (report.getDelDate() == null) list.add(report);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 신고글 조회
    public Page<Report> listOfDeleted(Pageable pageable) {
        Page<Report> all = reportRepository.findAll(pageable);
        List<Report> list = new ArrayList<>();

        for (Report report : all) {
            if (report.getDelDate() != null) list.add(report);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 신고글 조회
    public Page<Report> allList(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    private void initReportResult(Long reportId, String result) {
        Report origin = reportRepository.findById(reportId).orElseThrow(() -> new NotExistReportException("해당 id의 신고글은 없습니다. id = " + reportId));

        Report build = origin.toBuilder()
                .reportCheck("처리 완료")
                .reportResult(result)
                .delDate(ZonedDateTime.now())
                .build();
        reportRepository.save(build);
    }
}