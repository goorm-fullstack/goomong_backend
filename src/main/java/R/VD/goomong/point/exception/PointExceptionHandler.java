package R.VD.goomong.point.exception;

import R.VD.goomong.global.model.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"R.VD.goomong.point"})
public class PointExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handlePointNotFoundException(PointNotFoundException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handlePointIllegalArgumentException(PointIllegalArgumentException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}
