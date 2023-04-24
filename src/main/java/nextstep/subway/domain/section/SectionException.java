package nextstep.subway.domain.section;

import nextstep.subway.common.exception.ErrorCode;

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
