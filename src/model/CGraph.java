package model;

import com.sun.javafx.collections.ObservableListWrapper;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class CGraph {

    private static CGraph instance;
    private Graph<Vertex, Edge> graph;
    private ObservableList<Vertex> vertices;

    public CGraph create(int numberOfVertex){
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
            graph.addEdge(new Edge(random.nextInt(20), v1.getId() + "-" + v2.getId()), v1, v2);
            Vertex tempV = vertices.get(random.nextInt(i));
            if(random.nextInt(5) < 2)graph.addEdge(new Edge(random.nextInt(20),v2.getId() + "-" + tempV.getId()), v2, tempV);
            if(random.nextInt(3) > 0){
                v1 = v2;
            }
        }

        return this;
    }

    public ObservableList<Vertex> getVertices() {
        return vertices;
    }

    public Graph<Vertex, Edge> getCurrentGraph(){
        return graph;
    }

    public static CGraph getInstance(){
        if(instance == null)instance = new CGraph();
        return instance;
    }

    private CGraph(){
        vertices = new ObservableListWrapper<Vertex>(new ArrayList<>());
    }

}
