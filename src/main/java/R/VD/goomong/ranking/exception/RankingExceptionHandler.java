package R.VD.goomong.ranking.exception;

import R.VD.goomong.global.model.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"R.VD.goomong.ranking"})
public class RankingExceptionHandler {

    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO("유효하지 않은 'period' 값 입니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}