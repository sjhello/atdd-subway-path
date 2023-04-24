package nextstep.subway.exception.path;

public class PathBadRequestException extends PathException {

    public PathBadRequestException(PathErrorCode errorCode) {
        super(errorCode);
    }
}
