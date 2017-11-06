package model;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class CGraph {

    public static String LEVIT = "levit";
    public static String KRUSKAL = "kruskal";


    private static int MIN_WEIGHT = 1;
    private static int MAX_WEIGHT = 20;

    private static Graph<Vertex, Edge> graph;
    private static ObservableList<Vertex> vertices;


    private static String currentAlgorithm;

    static {
        vertices = new ObservableListWrapper<Vertex>(new ArrayList<>());
    }

    public static Graph create(int numberOfVertex) {
        Random random = new Random();

        graph = new SparseGraph<>();

        vertices.clear();
        for (int i = 1; i <= numberOfVertex; ++i) {
            Vertex v = new Vertex(i);
            graph.addVertex(v);
            vertices.add(v);
        }

        Vertex v1 = vertices.get(0);
        for (int i = 1; i < numberOfVertex; ++i) {
            Vertex v2 = vertices.get(i);
            graph.addEdge(new Edge(random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT, v1.getId() + "-" + v2.getId()), v1, v2);
            Vertex tempV = vertices.get(random.nextInt(i));
            if (random.nextInt(5) < 3)
                graph.addEdge(new Edge(random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT, v2.getId() + "-" + tempV.getId()), v2, tempV);
            if (random.nextInt(3) > 0) {
                v1 = v2;
            }
        }

        return graph;
    }

    public static ObservableList<Vertex> getVertices() {
        return vertices;
    }

    public static Graph<Vertex, Edge> getCurrentGraph() {
        return graph;
    }

    public static void setCurrentALgorithm(String currentAlgorithm) {
        CGraph.currentAlgorithm = currentAlgorithm;
    }

    public static void startAlgorithm() {
        if (currentAlgorithm.equals(LEVIT)) {
            Vertex s = null, d = null;
            for (Vertex v : vertices) {
                if (v.isSource()) s = v;
                else if (v.isTaret()) d = v;
                if (s != null && d != null) break;
            }
            startLevitAlgorithm(graph, s, d);
        } else if (currentAlgorithm.equals(KRUSKAL)) {
            startKruskalAlgorithm();
        }
    }

    public static void resetResult() {
        graph.getEdges().forEach(edge -> edge.setState(Edge.NORMAL));
    }

    private static boolean startLevitAlgorithm(Graph<Vertex, Edge> g, Vertex s, Vertex d) {
        Map<Vertex, Integer> D = new HashMap<>();
        Map<Vertex, Vertex> P = new HashMap<>();
        Map<Vertex, Object> M2 = new HashMap<>();
        Map<Vertex, Object> M0 = new HashMap<>();
        Deque<Vertex> M1 = new ArrayDeque<>();

        try {
            vertices.forEach(vertex -> {
                D.put(vertex, Integer.MAX_VALUE);
                M2.put(vertex, null);
            });
            D.replace(s, 0);
            M2.remove(s);
            M1.add(s);
            while (M1.size() > 0) {
                Vertex v = M1.pop();
                M0.put(v, null);
                g.getInEdges(v).forEach(edge -> {
                    Vertex t;
                    Pair<Vertex> p = g.getEndpoints(edge);
                    if ((t = p.getFirst()) == v) {
                        t = p.getSecond();
                    }
                    if (M2.containsKey(t)) {
                        M2.remove(t);
                        M1.add(t);
                        D.replace(t, D.get(v) + edge.getWeight());
                        P.put(t, v);
                    } else if (M1.contains(t)) {
                        int weight = D.get(v) + edge.getWeight();
                        if (weight < D.get(t)) {
                            D.replace(t, weight);
                            P.put(t, v);
                        }
                    } else {
                        int weight = D.get(v) + edge.getWeight();
                        if (weight < D.get(t)) {
                            D.replace(t, weight);
                            P.put(t, v);
                            M1.add(t);
                            M0.remove(t);
                        }
                    }
                });
            }
            Vertex v1 = null;
            while (v1 != s) {
                v1 = P.get(d);
                g.findEdge(d, v1).setState(Edge.PATH);
                d = v1;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static void startKruskalAlgorithm() {
        List<Edge> sortedEdges = graph.getEdges().stream().sorted(Edge::compare).collect(Collectors.toList());
        Graph<Vertex, Edge> temp = new SparseGraph<>();
        sortedEdges.forEach(edge -> {
            Pair<Vertex> pair = graph.getEndpoints(edge);

            if (startLevitAlgorithm(temp, pair.getFirst(), pair.getSecond())) {
                edge.setState(Edge.HIDE);
            } else {
                temp.addVertex(pair.getFirst());
                temp.addVertex(pair.getSecond());
                temp.addEdge(edge, pair.getFirst(), pair.getSecond());
            }
        });
        sortedEdges.forEach(edge -> {
            if(edge.getState() == Edge.PATH)edge.setState(Edge.NORMAL);
        });
    }

    private CGraph() {
    }

}
