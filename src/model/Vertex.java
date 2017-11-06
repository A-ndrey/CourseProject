package model;

public class Vertex{


    private boolean source;
    private boolean target;
    private int id;

    public Vertex(int id){
        this.id = id;
    }

    public void setSource(boolean source){
        this.source = source;
    }

    public void setTarget(boolean target){
        this.target = target;
    }

    public boolean isSource(){
        return source;
    }

    public boolean isTaret(){
        return target;
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