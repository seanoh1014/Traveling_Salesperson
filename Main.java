import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String verticesFile = "TX-dfw50.vertices.txt";
        String edgesFile = "TX-dfw50.edges.txt";
        Map<Integer, Vertex> graph = getGraph(readFile(verticesFile), readFile(edgesFile));
        
        List<Vertex> stops = new ArrayList<>();
        stops.add(graph.get(740));  // Coppell
        stops.add(graph.get(1328)); // Midlothian
        stops.add(graph.get(285));  // Colleyville
        stops.add(graph.get(545));  // Arlington
        stops.add(graph.get(983));  // Highland Park
        stops.add(graph.get(478));  // Downtown Fort Worth
        
        TravelingSalesPerson tsp = new TravelingSalesPerson(graph);
        tsp.findShortestRoute(stops);

        System.out.printf("\nBest tour:  %s", tsp.getShortestPath());
        System.out.printf("\nPath Size:  %d", tsp.getShortestPath().size());
        System.out.printf("\nDistance:   %.2f", tsp.getMinimumRouteDistance());
        System.out.printf("\nPollCount:  %,d", tsp.getPollCount());
        System.out.printf("\nRecursions: %,d\n", tsp.getRecursionCount());
    }

    /*
     * The getGraph method should return a Map where the Key is the Vertex id
     * and the value is the Vertex. You will need to create all the vertices then
     * add the edges to each Vertex.
     */
    public static Map<Integer, Vertex> getGraph(List<String> verticesList, List<String> edgesList) {
        Map<Integer, Vertex> map = new HashMap<>();

        for (int i = 0; i < verticesList.size(); i++) {
            String[] field = verticesList.get(i).split(" ");
            map.put(i, new Vertex(i, field[0], Double.parseDouble(field[1]), Double.parseDouble(field[2])));
        }

        for (String line : edgesList) {
            String[] field = line.split(" ");

            Vertex source = map.get(Integer.parseInt(field[0]));
            Vertex target = map.get(Integer.parseInt(field[1]));
            double weight = 0;
            double lat1 = source.getLatitude();
            double lon1 = source.getLongitude();

            for (int k = 3; k < field.length; k += 2) {
                Double lat2 = Double.parseDouble(field[k]);
                Double lon2 = Double.parseDouble(field[k + 1]);
                weight += calculateDistance(lat1, lon1, lat2, lon2);
                lat1 = lat2;
                lon1 = lon2;
            }
            weight += calculateDistance(lat1, lon1, target.getLatitude(), target.getLongitude());            
            source.addEdge(new Edge(source, target, field[2], weight));
            target.addEdge(new Edge(target, source, field[2], weight));
        }
        return map;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 0.621371192; // convert to miles
        return distance;
    }

    public static List<String> readFile(String filename) {
        List<String> list = new ArrayList<>();
        try (Scanner input = new Scanner(new File(filename))) {
            while (input.hasNextLine()) {
                list.add(input.nextLine());
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File not found");
        }
        return list;
    }
}
