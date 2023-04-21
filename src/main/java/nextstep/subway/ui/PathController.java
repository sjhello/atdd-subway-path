package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/path")
@RequiredArgsConstructor
public class PathController {

    private final PathService pathService;

    @GetMapping
    public ResponseEntity<PathResponse> showPath(PathRequest pathRequest) {
        return ResponseEntity.ok().body(pathService.getPath(pathRequest));
    }
}
