package nextstep.subway.domain.path;

public class PathBadRequestException extends PathException {

    public PathBadRequestException(PathErrorCode errorCode) {
        super(errorCode);
    }
}
