package R.VD.goomong.support.exception;

import R.VD.goomong.global.model.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"R.VD.goomong.support"})
public class SupportExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDTO> handleSupportNotFoundException(SupportNotFoundException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

}
