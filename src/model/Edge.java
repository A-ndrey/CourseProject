package model;

public class Edge{
    private int weight;
    private String id;

    public Edge(int weight, String id){
        this.weight = weight;
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }
    public String getId(){return id;}

    @Override
    public String toString() {
        return "w" + weight;
    }

    public boolean equals(Edge e) {
        return id.equals(e.id);
    }
}