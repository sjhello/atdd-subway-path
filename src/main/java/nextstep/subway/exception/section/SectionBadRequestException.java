package nextstep.subway.exception.section;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SectionBadRequestException extends SectionException {

    public SectionBadRequestException(SectionErrorCode errorCode) {
        super(errorCode);
    }
}
