package model;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;

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

    public void startALgorithm(){
        if(currentALgorithm.equals(LEVIT)){
            startLevitAlgorithm();
        } else if(currentALgorithm.equals(KRUSKAL)){
            startKruskalAlgorithm();
        }
    }

    public void resetResult(){

    }

    private void startLevitAlgorithm(){

    }

    private void startKruskalAlgorithm(){

    }

    private CGraph(){}

}
