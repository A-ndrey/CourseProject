package model;

public class Vertex{
    private int id;

    public Vertex(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "v" + id;
    }

    public boolean equals(Vertex v) {
        return id == v.getId();
    }
}