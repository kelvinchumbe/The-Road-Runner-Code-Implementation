import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    ImageView imageView;
    int fScore;
    int gScore;
    int h;
    ArrayList<Node> neighbours;
    int[] posOnGrid;
    Node parent;
    int parent_x;
    int parent_y;
    int energy_cost;
    int node_x;
    int node_y;

    public Node(ImageView view){
        fScore = (int) Double.POSITIVE_INFINITY;
        gScore = (int) Double.NEGATIVE_INFINITY;
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

//    public String toString(){
//        return Arrays.toString(posOnGrid);
//    }

}
