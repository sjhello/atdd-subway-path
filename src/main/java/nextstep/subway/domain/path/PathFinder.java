package nextstep.subway.domain.path;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        drawGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void drawGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> {
            addVertex(line);
            addEdgeWeight(line);
        });
    }

    private void addEdgeWeight(Line line) {
        line.getSections().stream().forEach(
                section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
        );
    }

    private void addVertex(Line line) {
        line.getStations()
                .stream()
                .forEach(graph :: addVertex);
    }

    public Path findShortPath(Station source, Station target) {
        validateStation(source, target);

        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return new Path(path.getVertexList(), (int)path.getWeight());
    }

    private void validateStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathBadRequestException(PathErrorCode.SAME_STATION);
        }
    }

}
