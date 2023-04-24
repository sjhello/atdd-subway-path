package nextstep.subway.domain.path;

import nextstep.subway.common.exception.ErrorCode;

public enum PathErrorCode implements ErrorCode {
    SAME_STATION("출발지와 목적지는 같을 수 없습니다.");

    private final String message;

    PathErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
