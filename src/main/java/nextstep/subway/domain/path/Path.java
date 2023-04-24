package nextstep.subway.domain.path;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.station.Station;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Path {

    private List<Station> stations;

    private int distance;
}
