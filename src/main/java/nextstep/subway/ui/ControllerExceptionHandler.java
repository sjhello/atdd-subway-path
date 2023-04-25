package nextstep.subway.ui;

import nextstep.subway.common.exception.dto.ErrorResponse;
import nextstep.subway.domain.path.PathBadRequestException;
import nextstep.subway.domain.path.PathException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PathBadRequestException.class)
    public ResponseEntity<ErrorResponse> handlePathException(PathException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
