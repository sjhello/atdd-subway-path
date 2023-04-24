package nextstep.subway.exception.path;

import nextstep.subway.exception.ErrorCode;

public class PathException extends RuntimeException {

    protected final ErrorCode errorCode;

    public PathException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return this.errorCode.getCode();
    }
}
