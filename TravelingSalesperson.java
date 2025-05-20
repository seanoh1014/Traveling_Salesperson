import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class TravelingSalesPerson {
    
    private Map<Integer, Vertex> graph;
    private long pollCount = 0;
    private int recursionCount = 0;
    double minDistance = 0; // The minimum distance for the path
    List<Vertex> shortestPath; // The order of stops that make up the minimum distance path
    Set<Vertex> visitedSet; // Keeps track fo the stops that have been visited so we only visit them once


    public TravelingSalesPerson(Map<Integer, Vertex> graph) {
        this.graph = graph;
        this.shortestPath = new ArrayList<>();
        this.visitedSet = new HashSet<>();
    }

    public void findShortestRoute(List<Vertex> vertices) {
        minDistance = Integer.MAX_VALUE;
        shortestPath = new ArrayList<>();
        visitedSet = new HashSet<>();

        // loop through each stop 
        for (Vertex stop : vertices) {
            // loop through each stop and store the other vertex and distance from current vertex
            for (Vertex other : vertices) {
                if (stop.equals(other)) { continue; }
                stop.addStop(other, computePath(stop, other));
            }
        }

        // call findShortestRouteRecursive 
        findShortestRouteRecursive(vertices, vertices.get(0), 0, new ArrayList<>());
    }

    private void findShortestRouteRecursive(List<Vertex> vertices, Vertex currentVertex, double currentDistance, List<Vertex> currentPath) {
        visitedSet.add(currentVertex);
        currentPath.add(currentVertex);

        if (currentPath.size() == vertices.size()) {
            //currentDistance += computePath(currentPath.get(currentPath.size() - 1), currentPath.get(0));
            currentDistance += currentPath.get(0).getStopList().get(currentPath.get(currentPath.size() - 1));

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                shortestPath = new ArrayList<>();
                shortestPath.addAll(currentPath);
                shortestPath.add(currentPath.get(0));
            } 
        }
        else {
            for (Vertex current : currentVertex.getStopList().keySet()) {
                if (!visitedSet.contains(current)) {
                    double dist = currentVertex.getStopList().get(current) + currentDistance;
                    recursionCount++;
                    findShortestRouteRecursive(vertices, current, dist, currentPath);
                }
            }
        }

        visitedSet.remove(currentVertex);
        currentPath.remove(currentPath.size() - 1);
    }

    /*
    *   A* Algorithm
    */
    public double computePath(Vertex start, Vertex end) {

        resetVerticeGraph();

        start.setDistance(0);
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Vertex source = queue.poll();
            pollCount++;
            if (source == end) {
                return end.getDistance();
            }

            for (Edge edge : source.getEdgeList()) {
                Vertex target = edge.getTargetVertex();
                target.setDistanceToEnd(end);
                double d = source.getDistance() + edge.getWeight();

                if (d < target.getDistance()) {
                    target.setDistance(d);
                    target.setPrevious(source);
                    queue.remove(target);
                    queue.add(target);
                }
            }
        }
        return -1;
    }

    public List<Vertex> getPath(Vertex current) {
        List<Vertex> path = new LinkedList<>();
        while (current != null) {
            path.add(0, current);
            current = current.getPrevious();
        }
        return path;
    }

    public void resetVerticeGraph() {
        for (Vertex v : graph.values()) {
            v.setDistance(Double.POSITIVE_INFINITY);
            v.setPrevious(null);
        }
    }

    public long getPollCount() {
        return pollCount;
    }

    public double getMinimumRouteDistance() {
        return minDistance;
    }

    public List<Vertex> getShortestPath() {
        return shortestPath;
    }

    public int getRecursionCount() {
        return recursionCount;
    }
}
