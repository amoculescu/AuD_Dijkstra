package lab;

import java.util.ArrayList;

public class Node {
    private final String name;
    private final int delay;
    private ArrayList<Edge> edges;
    private boolean done;
    private double distanceToStart;
    private Node previousInPath;

    public Node (String name, int delay){
        this.name = name;
        this.delay = delay;
        this.edges = new ArrayList<>();
        this.distanceToStart = Double.POSITIVE_INFINITY;
        this.previousInPath = null;
        this.done = false;
    }

    public String getName(){
        return this.name;
    }

    public int getDelay(){
        return this.delay;
    }

    public ArrayList<Edge> getEdges(){
        return this.edges;
    }

    public Edge getEdge(int index){
        return this.edges.get(index);
    }

    public void addEdge(Edge e){
        this.edges.add(e);
    }

    public double getDistanceToStart(){ return  this.distanceToStart; }

    public void setDistanceToStart(double newDistance){ this.distanceToStart = newDistance; }

    public Node getPreviousInPath(){ return this.previousInPath; }

    public void setPreviousInPath(Node newNode){ this.previousInPath = newNode; }

    public boolean isFinished(){ return this.done; }

    public void finished(){ this.done = true; }

    public Edge getNext(){
        Edge shortest = edges.get(0);
        for(int i = 0; i < this.edges.size(); i++){
            Edge currentEdge = edges.get(i);
            if(currentEdge.getDistance() < shortest.getDistance())
                shortest = currentEdge;
        }
        return shortest;
    }

    public void updateNeighbors(){
        for(int i = 0; i < edges.size(); i++){
            Edge currentEdge = edges.get(i);
            if(this.distanceToStart + currentEdge.getDistance() < currentEdge.getB().distanceToStart) {
                currentEdge.getB().setDistanceToStart(this.distanceToStart + currentEdge.getDistance());
                currentEdge.getB().setPreviousInPath(this);
            }
        }
    }
}
