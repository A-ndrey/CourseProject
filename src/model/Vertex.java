package model;

public class Vertex{

    public static final String START = "start";
    public static final String END = "end";
    public static final String NORMAL = "normal";

    private String state;
    private int id;

    public Vertex(int id){
        this.id = id;
        this.state = NORMAL;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return  state;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Vertex v) {
        return id == v.getId();
    }

    @Override
    public String toString() {
        return "" + id;
    }
}