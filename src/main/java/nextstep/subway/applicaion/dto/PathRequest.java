package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PathRequest {

    private Long source;
    private Long target;
}
