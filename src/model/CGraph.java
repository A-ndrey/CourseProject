package model;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.Tree;
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


    private static String currentALgorithm;

    static {
        vertices = new ObservableListWrapper<Vertex>(new ArrayList<>());
    }

    public static Graph create(int numberOfVertex){
        Random random = new Random();

        graph = new SparseGraph<>();

        vertices.clear();
        for(int i = 1; i <= numberOfVertex; ++i){
            Vertex v = new Vertex(i);
            graph.addVertex(v);
            vertices.add(v);
        }

        Vertex v1 = vertices.get(0);
        for(int i = 1; i < numberOfVertex; ++i){
            Vertex v2 = vertices.get(i);
            graph.addEdge(new Edge(random.nextInt(MAX_WEIGHT-MIN_WEIGHT)+MIN_WEIGHT, v1.getId() + "-" + v2.getId()), v1, v2);
            Vertex tempV = vertices.get(random.nextInt(i));
            if(random.nextInt(5) < 2)graph.addEdge(new Edge(random.nextInt(20),v2.getId() + "-" + tempV.getId()), v2, tempV);
            if(random.nextInt(3) > 0){
                v1 = v2;
            }
        }

        return graph;
    }

    public static ObservableList<Vertex> getVertices() {
        return vertices;
    }

    public static Graph<Vertex, Edge> getCurrentGraph(){
        return graph;
    }

    public static void setCurrentALgorithm(String currentALgorithm) {
        CGraph.currentALgorithm = currentALgorithm;
    }

    public static void startAlgorithm(){
        if(currentALgorithm.equals(LEVIT)){
            startLevitAlgorithm();
        } else if(currentALgorithm.equals(KRUSKAL)){
            startKruskalAlgorithm();
        }
    }

    public static void resetResult(){

    }

    private static void startLevitAlgorithm(){

    }

    private static void startKruskalAlgorithm(){
        List<Edge> sortedEdges = graph.getEdges().stream().sorted(Edge::compare).collect(Collectors.toList());
        Graph<Vertex, Edge> temp = new SparseGraph<>();
        DijkstraShortestPath<Vertex, Edge> alg = new DijkstraShortestPath<>(temp, Edge::getWeight);
        sortedEdges.forEach(edge -> {
            Pair<Vertex> pair = graph.getEndpoints(edge);
            int distance = 0;
            try{
                distance = alg.getDistance(pair.getFirst(), pair.getSecond()).intValue();
            } catch (Exception e){}
            if(distance > 0){
                edge.setState(Edge.HIDE);
            } else {
                temp.addVertex(pair.getFirst());
                temp.addVertex(pair.getSecond());
                temp.addEdge(edge, pair.getFirst(), pair.getSecond());
            }
        });
    }

    private CGraph(){}

}
