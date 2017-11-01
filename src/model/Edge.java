package model;

public class Edge{

    public static String NORMAL = "normal";
    public static String HIDE = "hide";
    public static String PATH = "path";

    private int weight;
    private String id;
    private String state;

    public Edge(int weight, String id){
        this.weight = weight;
        this.id = id;
        state = NORMAL;
    }

    public int getWeight() {
        return weight;
    }

    public String getId(){return id;}

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    @Override
    public String toString() {
        return "w" + weight;
    }

    public boolean equals(Edge e) {
        return id.equals(e.id);
    }
}