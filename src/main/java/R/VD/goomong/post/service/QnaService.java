package R.VD.goomong.post.service;

import R.VD.goomong.post.dto.request.RequestAnswerForQuestionDto;
import R.VD.goomong.post.dto.request.RequestQuestionDto;
import R.VD.goomong.post.exception.AlreadyDeleteCategoryException;
import R.VD.goomong.post.exception.AlreadyDeletedQnaException;
import R.VD.goomong.post.exception.NotExistCategoryException;
import R.VD.goomong.post.exception.NotExistQnaException;
import R.VD.goomong.post.model.Category;
import R.VD.goomong.post.model.Qna;
import R.VD.goomong.post.repository.CategoryRepository;
import R.VD.goomong.post.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;
    private final CategoryRepository categoryRepository;

    // 질문 생성
    public void saveQuestion(RequestQuestionDto requestQuestionDto) {
        Qna entity = requestQuestionDto.toEntity();

        Category category = null;
        if (requestQuestionDto.getCategoryId() != null) {
            category = categoryRepository.findById(requestQuestionDto.getCategoryId()).orElseThrow(() -> new NotExistCategoryException("해당 id의 카테고리를 찾을 수 없습니다. id = " + requestQuestionDto.getCategoryId()));
            if (category.getDelDate() != null)
                throw new AlreadyDeleteCategoryException("해당 id의 카테고리는 이미 삭제된 카테고리입니다. id = " + category.getId());
        }

        Qna build = entity.toBuilder()
                .category(category)
                .build();
        qnaRepository.save(build);
    }

    // 답변 생성
    public void saveAnswer(RequestAnswerForQuestionDto requestAnswerForQuestionDto) {
        Qna entity = requestAnswerForQuestionDto.toEntity();

        Qna parent = qnaRepository.findById(requestAnswerForQuestionDto.getParentId()).orElseThrow(() -> new NotExistQnaException("해당 id의 QnA를 찾을 수 없습니다. id = " + requestAnswerForQuestionDto.getParentId()));
        if (parent.getDelDate() != null)
            throw new AlreadyDeletedQnaException("해당 id의 QnA는 이미 삭제된 QnA입니다. id = " + parent.getId());

        Qna build = entity.toBuilder()
                .qna(parent)
                .build();
        qnaRepository.save(build);
    }

    // 질문 수정
    public Qna updateQuestion(Long qnaId, RequestQuestionDto requestQuestionDto) {
        Qna origin = qnaRepository.findById(qnaId).orElseThrow(() -> new NotExistQnaException("해당 id의 QnA를 찾을 수 없습니다. id = " + qnaId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedQnaException("해당 id의 QnA는 이미 삭제된 QnA입니다. id = " + qnaId);

        Qna build = origin.toBuilder()
                .title(requestQuestionDto.getTitle())
                .content(requestQuestionDto.getContent())
                .build();
        return qnaRepository.save(build);
    }

    // 답변 수정
    public Qna updateAnswer(Long qnaId, RequestAnswerForQuestionDto requestAnswerForQuestionDto) {
        Qna origin = qnaRepository.findById(qnaId).orElseThrow(() -> new NotExistQnaException("해당 id의 QnA를 찾을 수 없습니다. id = " + qnaId));
        if (origin.getDelDate() != null)
            throw new AlreadyDeletedQnaException("해당 id의 QnA는 이미 삭제된 QnA입니다. id = " + qnaId);

        Qna build = origin.toBuilder()
                .content(requestAnswerForQuestionDto.getContent())
                .build();
        return qnaRepository.save(build);
    }

    // QnA 삭제
    public void deleteQna(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(() -> new NotExistQnaException("해당 id의 QnA를 찾을 수 없습니다. id = " + qnaId));
        if (qna.getDelDate() != null) throw new AlreadyDeletedQnaException("해당 id의 QnA는 이미 삭제된 QnA입니다. id = " + qnaId);

        Qna build = qna.toBuilder()
                .delDate(LocalDateTime.now())
                .build();
        qnaRepository.save(build);
    }

    // 특정 QnA 조회
    public Qna findOneQna(Long qnaId) {
        return qnaRepository.findById(qnaId).orElseThrow(() -> new NotExistQnaException("해당 id의 QnA를 찾을 수 없습니다. id = " + qnaId));
    }

    // 삭제되지 않은 질문 리스트 중 카테고리 이름으로 질문 리스트 조회
    public Page<Qna> listOfNotDeletedAndCategory(String categoryName, Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);
        List<Qna> list = new ArrayList<>();

        for (Qna qna : all) {
            if (qna.getQna() == null && qna.getDelDate() == null && qna.getCategory().getDelDate() == null && qna.getCategory().getCategoryName().equals(categoryName))
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 질문 리스트 중 카테고리 이름으로 질문 리스트 조회
    public Page<Qna> listOfDeletedAndCategory(String categoryName, Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);
        List<Qna> list = new ArrayList<>();

        for (Qna qna : all) {
            if (qna.getQna() == null && qna.getDelDate() != null && qna.getCategory().getDelDate() == null && qna.getCategory().getCategoryName().equals(categoryName))
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 카테고리 이름으로 전체 질문 리스트 조회
    public Page<Qna> listOfAllAndCategory(String categoryName, Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);
        List<Qna> list = new ArrayList<>();

        for (Qna qna : all) {
            if (qna.getQna() == null && qna.getCategory().getDelDate() == null && qna.getCategory().getCategoryName().equals(categoryName))
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제되지 않은 질문 리스트 조회
    public Page<Qna> listOfNotDeletedQuestion(Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);

        List<Qna> list = new ArrayList<>();
        for (Qna qna : all) {
            if (qna.getQna() == null && qna.getCategory().getDelDate() == null && qna.getDelDate() == null)
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제되지 않은 답변 리스트 조회
    public Page<Qna> listOfNotDeletedAnswer(Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);

        List<Qna> list = new ArrayList<>();
        for (Qna qna : all) {
            if (qna.getQna() != null && qna.getCategory().getDelDate() == null && qna.getDelDate() == null)
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 질문 리스트 조회
    public Page<Qna> listOfDeletedQuestion(Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);

        List<Qna> list = new ArrayList<>();
        for (Qna qna : all) {
            if (qna.getQna() == null && qna.getCategory().getDelDate() == null && qna.getDelDate() != null)
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 삭제된 답변 리스트 조회
    public Page<Qna> listOfDeletedAnswer(Pageable pageable) {
        Page<Qna> all = qnaRepository.findAll(pageable);

        List<Qna> list = new ArrayList<>();
        for (Qna qna : all) {
            if (qna.getQna() != null && qna.getCategory().getDelDate() == null && qna.getDelDate() != null)
                list.add(qna);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    // 전체 QnA 리스트 조회
    public Page<Qna> allList(Pageable pageable) {
        return qnaRepository.findAll(pageable);
    }
}
