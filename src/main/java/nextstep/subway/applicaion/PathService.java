package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.station.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private final StationService stationService;

    private final LineRepository lineRepository;

    public PathResponse getPath(PathRequest pathRequest) {
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);

        return createPathResponse(pathFinder.findShortPath(source, target));
    }

    private List<StationResponse> createStationResponse(List<Station> stations) {
        return stations.stream()
                .map(stationService :: createStationResponse)
                .collect(Collectors.toList());
    }

    private PathResponse createPathResponse(Path path) {
        return new PathResponse(createStationResponse(path.getStations()), path.getDistance());
    }
}
