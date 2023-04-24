package nextstep.subway.exception.section;

import nextstep.subway.exception.ErrorCode;

public enum SectionErrorCode implements ErrorCode {
    SECTION_NOT_FOUND("구간이 존재하지 않습니다."),
    ONE_SECTION("현재 노선은 구간이 1개 입니다."),
    ALREADY_EXISTS("상행역과 하행역이 둘 다 이미 등록 되어 있습니다."),
    NOT_INCLUDE_STATION("상행역과 하행역이 둘 중 하나라도 기존 구간에 포함 되어 있어야 합니다."),
    INVALID_DISTANCE_ERROR("기존 역 사이 길이보다 크거나 같을 수 없습니다.");

    private final String message;

    SectionErrorCode(String message) {
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
