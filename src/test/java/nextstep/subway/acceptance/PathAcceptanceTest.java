package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import nextstep.subway.domain.path.PathErrorCode;
import nextstep.subway.domain.path.PathException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.fixture.LineFixture.createLineCreateParams;
import static nextstep.fixture.SectionFixture.createSectionCreateParams;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청_ID;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathSteps.경로_조회_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 조회 기능")
class PathAcceptanceTest extends AcceptanceTest{
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 길동역;
    private Long 강동역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 오호선;


    /**
     *              10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*  2                 *신분당선*       10
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *               3
     */

    /**
     * Given 지하철역을 생성하고 지하철 노선의 구간으로 등록한다
     * */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청_ID("교대역");
        강남역 = 지하철역_생성_요청_ID("강남역");
        양재역 = 지하철역_생성_요청_ID("양재역");
        길동역 = 지하철역_생성_요청_ID("길동역");
        강동역 = 지하철역_생성_요청_ID("길동역");
        남부터미널역 = 지하철역_생성_요청_ID("남부터미널역");

        이호선 = 지하철_노선_생성_요청_ID(createLineCreateParams("2호선", "green", 교대역, 강남역, 10));
        신분당선 = 지하철_노선_생성_요청_ID(createLineCreateParams("신분당선", "red", 강남역, 양재역, 10));
        삼호선 = 지하철_노선_생성_요청_ID(createLineCreateParams("3호선", "orange", 교대역, 남부터미널역, 2));
        오호선 = 지하철_노선_생성_요청_ID(createLineCreateParams("5호선", "purple", 길동역, 강동역, 17));

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    /**
     * When 목적지와 도착지의 경로를 조회하면
     * Then 목적지와 도착지의 최단 경로가 조회된다
     * */
    @Test
    void 최단경로를_조회_한다() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(5);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(교대역, 남부터미널역, 양재역);
    }

    /**
     * When 목적지와 도착지가 같으면
     * Then 경로조회에 실패한다
     * */
    @Test
    void 목적지와_도착지가_같으면_경로조회에_실패한다() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 교대역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo(PathErrorCode.SAME_STATION.getCode());
        assertThat(response.body().jsonPath().getString("message")).isEqualTo(PathErrorCode.SAME_STATION.getMessage());
    }

    /**
     * When 목적지와 도착지가 연결되어 있지 않으면
     * Then 경로조회에 실패한다
     * */
    @Test
    void 목적지와_도착지가_연결되어_있지_않으면_경로조회에_실패한다() {
        // when
        ExtractableResponse<Response> response = 경로_조회_요청(교대역, 길동역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().jsonPath().getString("errorCode")).isEqualTo(PathErrorCode.NOT_CONNECTION.getCode());
        assertThat(response.body().jsonPath().getString("message")).isEqualTo(PathErrorCode.NOT_CONNECTION.getMessage());
    }
}