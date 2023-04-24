package nextstep.subway.exception.section;

import nextstep.subway.exception.ErrorCode;

public class SectionException extends RuntimeException {

    protected ErrorCode errorCode;

    public SectionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return this.errorCode.getCode();
    }
}
