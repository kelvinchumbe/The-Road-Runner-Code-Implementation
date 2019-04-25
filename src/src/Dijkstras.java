import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

// dijkstras
// Methods used to determine validity of some aspects have been borrowed from static methods in the A_Star class
public class Dijkstras {
    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to search the runners path using dijkstra's algorithm to the goal node considering the weight of the nodes
    @SuppressWarnings("Duplicates")
    public static void Dijkstras_Search_8D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer,Image>image_dict) throws IOException {
        // set to store all unvisited nodes
        Set<Cell_Node> unvisited_nodes = new HashSet<>();

        // update all nodes in the grid node. Set their gscore to negative infinity and update their neighbours if they are valid i.e are not blocked and their coordinates are valid
        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
                grid_nodes[i][j].setgScore((int) Double.NEGATIVE_INFINITY);

                if(A_Star.cell_isValid(grid_nodes,i-1, j-1) && !A_Star.cell_isBlocked(grid_nodes, i-1, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j-1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i-1, j) && !A_Star.cell_isBlocked(grid_nodes, i-1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j]);
                }
                if(A_Star.cell_isValid(grid_nodes, i-1, j+1) && !A_Star.cell_isBlocked(grid_nodes, i-1, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j+1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i, j-1) && !A_Star.cell_isBlocked(grid_nodes, i, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j-1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i, j+1) && !A_Star.cell_isBlocked(grid_nodes, i, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j+1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i+1, j-1) && !A_Star.cell_isBlocked(grid_nodes, i+1, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j-1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i+1, j) && !A_Star.cell_isBlocked(grid_nodes, i+1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j]);
                }
                if(A_Star.cell_isValid(grid_nodes, i+1, j+1) && !A_Star.cell_isBlocked(grid_nodes, i+1, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j+1]);
                }

                // update the grid nodes energy cost
                for(Integer key: image_dict.keySet()){
                    if(image_dict.get(key).equals(grid_nodes[i][j].getImageView().getImage())){
                        switch (key){
                            case 0:
                                grid_nodes[i][j].energy_cost = -1;
                                break;
                            case 2:
                                grid_nodes[i][j].energy_cost = -2;
                                break;
                            case 3:
                                grid_nodes[i][j].energy_cost = -4;
                                break;
                            case 4:
                                grid_nodes[i][j].energy_cost = -8;
                                break;
                            case 5:
                                grid_nodes[i][j].energy_cost = 1;
                                break;
                            case 6:
                                grid_nodes[i][j].energy_cost = 5;
                                break;
                        }
                    }
                }
            }
        }

        // add all nodes to the unvisited set
        for(Cell_Node[] node_arr: grid_nodes){
            unvisited_nodes.addAll(Arrays.asList(node_arr));
        }

        // update the start's gscore to 0
        start.setgScore(0);

        boolean foundDestination = false;

        // there's still nodes in the unvisited set and we havent found the destination
        while(!unvisited_nodes.isEmpty() && !foundDestination){

            // find the node with the largest gscore and set it to our current
            Cell_Node current = null;
            int max = (int) Double.NEGATIVE_INFINITY;
            for(Cell_Node node: unvisited_nodes){
                if(node.getgScore() > max){
                    current = node;
                    max = current.getgScore();
                }
            }

            // remove the current node from the unvisited set
            unvisited_nodes.remove(current);

            // get the current's nodes position on the grid
            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;

            // check if our current node is our destination
            if(A_Star.cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                A_Star.write_constructedPath(current, start, goal);
                foundDestination = true;
                break;
            }

            // for each of the current's neighbours
            for(Cell_Node neighbour: current.getNeighbours()){

                // check if the neighbour has not been explored yet
                if(unvisited_nodes.contains(neighbour)){

                    // if so, calculate the temp score by adding the current's gscore and the neighbour's energy cost
                    int temp_Score = current.getgScore() + neighbour.energy_cost;

                    // if the temp score is greater than the neighbour's score, update the neighbour's gscore to the temp score and update its parent to be the current node
                    if(temp_Score > neighbour.getgScore()){
                        neighbour.setgScore(temp_Score);
                        neighbour.parent = current;
                    }
                }
            }
        }

        // if the unvisited set is empty and we havent found our destination, alert the user
        if(unvisited_nodes.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }
    }

    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to search the runners path using dijkstra's algorithm to the goal node considering the weight of the nodes
    @SuppressWarnings("Duplicates")
    public static void Dijkstras_Search_4D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer,Image>image_dict) throws IOException {
        Set<Cell_Node> unvisited_nodes = new HashSet<>();

        // update the grid nodes's gscore, their neighbours and their energy costs
        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
                grid_nodes[i][j].setgScore((int) Double.NEGATIVE_INFINITY);

                if(A_Star.cell_isValid(grid_nodes, i-1, j) && !A_Star.cell_isBlocked(grid_nodes, i-1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j]);
                }
                if(A_Star.cell_isValid(grid_nodes, i, j-1) && !A_Star.cell_isBlocked(grid_nodes, i, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j-1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i, j+1) && !A_Star.cell_isBlocked(grid_nodes, i, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j+1]);
                }
                if(A_Star.cell_isValid(grid_nodes, i+1, j) && !A_Star.cell_isBlocked(grid_nodes, i+1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j]);
                }

                for(Integer key: image_dict.keySet()){
                    if(image_dict.get(key).equals(grid_nodes[i][j].getImageView().getImage())){
                        switch (key){
                            case 0:
                                grid_nodes[i][j].energy_cost = -1;
                                break;
                            case 2:
                                grid_nodes[i][j].energy_cost = -2;
                                break;
                            case 3:
                                grid_nodes[i][j].energy_cost = -4;
                                break;
                            case 4:
                                grid_nodes[i][j].energy_cost = -8;
                                break;
                            case 5:
                                grid_nodes[i][j].energy_cost = 1;
                                break;
                            case 6:
                                grid_nodes[i][j].energy_cost = 5;
                                break;
                        }
                    }
                }
            }
        }

        // add all nodes in the grid to the unvisited set
        for(Cell_Node[] node_arr: grid_nodes){
            unvisited_nodes.addAll(Arrays.asList(node_arr));
        }

        // set the start nodes gscore to 0
        start.setgScore(0);
        boolean foundDestination = false;

        // while there's still nodes that are univisited..
        while(!unvisited_nodes.isEmpty() && !foundDestination){

            // set the current node to be the node with the highest gscore
            Cell_Node current = null;
            int max = (int) Double.NEGATIVE_INFINITY;
            for(Cell_Node node: unvisited_nodes){
                if(node.getgScore() > max){
                    current = node;
                    max = current.getgScore();
                }
            }

            // remove the current node from the unvisited set
            unvisited_nodes.remove(current);

            // get currents position on the grid
            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            // check if current is our destination
            if(A_Star.cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                A_Star.write_constructedPath(current, start, goal);
                foundDestination = true;
                break;
            }

            // for each of current's neighbour, update their gscore and parents
            for(Cell_Node neighbour: current.getNeighbours()){
                if(unvisited_nodes.contains(neighbour)){
                    int temp_Score = current.getgScore() +  neighbour.energy_cost;

                    if(temp_Score > neighbour.getgScore()){
                        neighbour.setgScore(temp_Score);
                        neighbour.parent = current;
                    }
                }

            }
        }
        if(unvisited_nodes.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }
    }
}
