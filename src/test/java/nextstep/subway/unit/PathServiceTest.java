package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.domain.path.PathBadRequestException;
import nextstep.subway.domain.path.PathErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathServiceTest extends ServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineService lineService;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;


    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    /**
     *              10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*  2                 *신분당선*       10
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *               3
     */

    @BeforeEach
    void setUp() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        이호선 = lineRepository.save(new Line("이호선", "green"));
        lineService.addSection(이호선.getId(), new SectionRequest(교대역.getId(), 강남역.getId(), 10));

        신분당선 = lineRepository.save(new Line("신분당선", "red"));
        lineService.addSection(신분당선.getId(), new SectionRequest(강남역.getId(), 양재역.getId(), 10));

        삼호선 = lineRepository.save(new Line("삼호선", "orange"));
        lineService.addSection(삼호선.getId(), new SectionRequest(교대역.getId(), 남부터미널역.getId(), 2));
        lineService.addSection(삼호선.getId(), new SectionRequest(남부터미널역.getId(), 양재역.getId(), 3));
    }

    @Test
    void 최단경로를_조회한다() {
        // when
        PathResponse path = pathService.getPath(new PathRequest(교대역.getId(), 양재역.getId()));

        // then
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @Test
    void 출발지와_도착지가_같으면_경로조회에_실패한다() {
        // when & then
        assertThatThrownBy(() -> pathService.getPath(new PathRequest(1L, 1L)))
                .isInstanceOf(PathBadRequestException.class)
                .hasMessage(PathErrorCode.SAME_STATION.getMessage());
    }
}
