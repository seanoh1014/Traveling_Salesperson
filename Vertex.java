import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex implements Comparable<Vertex> {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private double distance;
    private double distToEnd;
    private Vertex previous;
    private List<Edge> edgeList;
    private Map<Vertex, Double> stopList;

    public Vertex(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edgeList = new ArrayList<>();
        this.stopList = new HashMap<>();
        this.distance = Double.POSITIVE_INFINITY;
        this.distToEnd = 0;
    }
      


    public void setDistanceToEnd(Vertex end) {
        distToEnd = Main.calculateDistance(this.latitude, this.longitude, end.latitude, end.longitude);
    }

    public int compareTo(Vertex other) {
        return Double.compare(this.distance + this.distToEnd, other.distance + other.distToEnd);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void addEdge(Edge edge) {
        this.edgeList.add(edge);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void addStop(Vertex stop, double distance) {
        stopList.put(stop, distance);
    }

    public Map<Vertex, Double> getStopList() {
        return stopList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Vertex other = (Vertex) obj;
        if (this.id != other.id || !
            this.name.equals(other.name) || 
            this.edgeList.size() != other.edgeList.size()) {
            return false;
        }
        for (int i = 0; i < edgeList.size(); i++) {
            if(!this.edgeList.get(i).equals(other.edgeList.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "" + id ;
    }
}
