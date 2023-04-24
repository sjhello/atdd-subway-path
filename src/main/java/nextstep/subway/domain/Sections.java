package nextstep.subway.domain;

import nextstep.subway.exception.section.SectionBadRequestException;
import nextstep.subway.exception.section.SectionErrorCode;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        validateAddSection(newSection);

        if (isFirstSection(newSection) || isLastSection(newSection)) {
            sections.add(newSection);
            return;
        }

        // 기존 구간 사이에 새로운 구간을 추가하는 경우
        if (!addSectionDownStation(newSection)) {
            addSectionUpStation(newSection);
        }
    }

    private boolean addSectionDownStation(Section newSection) {
        Optional<Section> optSection = sections.stream()
                .filter(oldSection -> oldSection.getDownStation().equals(newSection.getDownStation()))
                .findFirst();

        if (optSection.isPresent()) {
            Section oldSection = optSection.get();
            validateDistance(newSection, oldSection);
            sections.add(oldSection.addStation(newSection.getUpStation(), newSection.getDistance()));
            return true;
        }
        return false;
    }

    private void addSectionUpStation(Section newSection) {
        Optional<Section> optSection = sections.stream()
                .filter(oldSection -> oldSection.getUpStation().equals(newSection.getUpStation()))
                .findFirst();

        if (optSection.isPresent()) {
            Section oldSection = optSection.get();
            validateDistance(newSection, oldSection);
            sections.add(oldSection.addStation(newSection.getDownStation(), newSection.getDistance()));
        }
    }

    private void validateDistance(Section newSection, Section section) {
        if (newSection.getDistance() >= section.getDistance()) {
            throw new SectionBadRequestException(SectionErrorCode.INVALID_DISTANCE_ERROR);
        }
    }

    // 새로운 역을 하행 종점으로 등록할 경우
    private boolean isLastSection(Section newSection) {
        return getLastSection().isDownStation(newSection.getUpStation());
    }

    // 새로운 역을 상행 종점으로 등록할 경우
    private boolean isFirstSection(Section newSection) {
        return getFirstSection().isUpstation(newSection.getDownStation());
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private Section getFirstSection() {
        return sections.get(0);
    }

    private void validateAddSection(Section section) {
        if (isSectionIsAlreadyExist(section)) {
            throw new SectionBadRequestException(SectionErrorCode.ALREADY_EXISTS);
        }
        if (isNotIncludedOneStation(section)) {
            throw new SectionBadRequestException(SectionErrorCode.NOT_INCLUDE_STATION);
        }
    }

    private boolean isNotIncludedOneStation(Section section) {
        return !isContainsStation(section.getUpStation()) && !isContainsStation(section.getDownStation());
    }

    private boolean isSectionIsAlreadyExist(Section section) {
        return isContainsStation(section.getUpStation()) && isContainsStation(section.getDownStation());
    }

    private boolean isContainsStation(Station station) {
        List<Station> stations = getStations();
        return stations.contains(station);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    private boolean hasEnoughSize() {
        return sections.size() < 2;
    }

    public void remove(Station station) {
        validateRemove();

        if (isLastStation(station)) {
            sections.removeIf(section -> section.getDownStation().equals(station));
            return;
        }

        if (isUpStation(station)) {
            sections.removeIf(section -> section.getUpStation().equals(station));
            return;
        }
        removeMiddleSection(station);
    }

    private void validateRemove() {
        if (isEmpty()) {
            throw new SectionBadRequestException(SectionErrorCode.SECTION_NOT_FOUND);
        }
        if (hasEnoughSize()) {
            throw new SectionBadRequestException(SectionErrorCode.ONE_SECTION);
        }
    }

    private void removeMiddleSection(Station station) {
        Section section = findSectionByDownStation(station);
        Section nextSection = findSectionByUpStation(station);
        nextSection.modifyUpstation(section);

        sections.remove(section);
    }

    private boolean isLastStation(Station station) {
        List<Station> upStations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        Station downStation = sections.stream()
                .map(Section::getDownStation)
                .filter(s -> !upStations.contains(s))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("역이 존재 하지 않습니다."));

        return downStation.equals(station);
    }

    private boolean isUpStation(Station station) {
        Station upStation = sections.stream()
                .map(Section::getUpStation)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("역이 존재 하지 않습니다."));

        return upStation.equals(station);
    }

    private Section findSectionByUpStation(Station upStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(upStation))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("역이 존재 하지 않습니다."));
    }

    private Section findSectionByDownStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(downStation))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("역이 존재 하지 않습니다."));
    }

    public int getDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }
}
