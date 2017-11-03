package model;

public class Vertex{
    private int id;

    public Vertex(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean equals(Vertex v) {
        return id == v.getId();
    }

    @Override
    public String toString() {
        return "v" + id;
    }
}