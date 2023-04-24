package nextstep.subway.domain.line;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    private Sections sections = new Sections();

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getDistance() {
        return sections.getDistance();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void removeSection(Station station) {
        sections.remove(station);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
