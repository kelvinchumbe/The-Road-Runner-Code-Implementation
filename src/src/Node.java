import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

// node
public class Node {
    // node contains and imageview on the grid
    ImageView imageView;

    // node's fscore
    int fScore;

    // node's gscore
    int gScore;

    // node's heuristics score i.e distance of the node from the goal
    int h;

    // arraylist to store the node.s neighbours
    ArrayList<Node> neighbours;

    // array to store the node's position on the grid
    int[] posOnGrid;

    // node's parent
    Node parent;

    // node's energy cost. Is instrumental when implementing the runners path using dijkstra's algorithm
    int energy_cost;

    public Node(ImageView view){
        fScore = 0;
        gScore = 0;
        h = 0;
        imageView = view;
        neighbours = new ArrayList<>();
    }

    public int getfScore(){
        return fScore;
    }

    public int getgScore(){
        return gScore;
    }

    public int getH(){
        return h;
    }

    public void setfScore(int newf){
        fScore = newf;
    }

    public void setgScore(int newg){
        gScore = newg;
    }

    public void setH(int H){
        h = H;
    }

    public ArrayList<Node> getNeighbours(){
        return neighbours;
    }

    public void addNeighbours(Node neighbour_node){
        this.neighbours.add(neighbour_node);
    }

    public ImageView getImageView(){
        return imageView;
    }

    public int[] getPosOnGrid(){
        return posOnGrid;
    }

    public void setPosOnGrid(int[] pos){
        posOnGrid = pos;
    }


}
