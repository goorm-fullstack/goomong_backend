package R.VD.goomong.ranking.exception;

import R.VD.goomong.global.model.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"R.VD.goomong.ranking"})
public class RankingExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleRankingIllegalArgumentException(RankingIllegalArgumentException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}