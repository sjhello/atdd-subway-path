package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public LineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean canAddSection() {
        return this.getUpStationId() != null && this.getDownStationId() != null && this.getDistance() != 0;
    }
}
