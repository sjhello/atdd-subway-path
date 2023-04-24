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
        WeightedMultigraph weightedMultigraph = drawGraph(lines);
        dijkstraShortestPath = new DijkstraShortestPath(weightedMultigraph);
    }

    private WeightedMultigraph drawGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Line line : lines) {
            line.getStations().stream().forEach(graph :: addVertex);
            line.getSections().stream().forEach(section ->
                    graph.setEdgeWeight(
                            graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance())
            );
        }
        return graph;
    }

    public Path findShortPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathBadRequestException(PathErrorCode.SAME_STATION);
        }

        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return new Path(path.getVertexList(), (int)path.getWeight());
    }

}
