import javafx.scene.image.Image;

import java.io.IOException;
import java.util.*;

public class Depth_First_Search {

    public static ArrayList<String> Depth_First_Search_8D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer, Image> image_dict) throws IOException {

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
//                grid_nodes[i][j].setgScore((int) Double.NEGATIVE_INFINITY);

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
            }
        }

        Stack<Cell_Node> unvisited_nodes = new Stack<>();
        unvisited_nodes.push(start);

        boolean foundDestination = false;

        Set<Cell_Node> visited = new HashSet<>();
        visited.add(start);

        while(!unvisited_nodes.isEmpty()){
            Cell_Node current = unvisited_nodes.pop();
            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            for(Cell_Node neighbour: current.getNeighbours()){
                int[] neighbour_pos = A_Star.identify_CellPos(neighbour, grid_nodes);
                neighbour.posOnGrid = neighbour_pos;

                if(!visited.contains(neighbour)){
                    neighbour.parent = current;

                    if(A_Star.cell_isDestination(grid_nodes, neighbour_pos[0], neighbour_pos[1], image_dict)){
                        foundDestination = true;
                        return A_Star.write_constructedPath(neighbour, start, goal);
                    }

                    unvisited_nodes.push(neighbour);
                    visited.add(neighbour);
                }
            }
        }

        if(unvisited_nodes.isEmpty()){
            System.out.println("Could Not Find the Destination");
        }

        return null;
    }

    public static ArrayList<String> Depth_First_Search_4D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer, Image> image_dict) throws IOException {
        for(int i=0; i < grid_nodes.length; i++) {
            for (int j = 0; j < grid_nodes[i].length; j++) {
//                grid_nodes[i][j].setgScore((int) Double.NEGATIVE_INFINITY);

                if (A_Star.cell_isValid(grid_nodes, i - 1, j) && !A_Star.cell_isBlocked(grid_nodes, i - 1, j, image_dict)) {
                    grid_nodes[i][j].addNeighbours(grid_nodes[i - 1][j]);
                }
                if (A_Star.cell_isValid(grid_nodes, i, j - 1) && !A_Star.cell_isBlocked(grid_nodes, i, j - 1, image_dict)) {
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j - 1]);
                }
                if (A_Star.cell_isValid(grid_nodes, i, j + 1) && !A_Star.cell_isBlocked(grid_nodes, i, j + 1, image_dict)) {
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j + 1]);
                }
                if (A_Star.cell_isValid(grid_nodes, i + 1, j) && !A_Star.cell_isBlocked(grid_nodes, i + 1, j, image_dict)) {
                    grid_nodes[i][j].addNeighbours(grid_nodes[i + 1][j]);
                }
            }
        }

        Stack<Cell_Node> unvisited_nodes = new Stack<>();
        unvisited_nodes.push(start);

        boolean foundDestination = false;

        Set<Cell_Node> visited = new HashSet<>();
        visited.add(start);

        while(!unvisited_nodes.isEmpty()){
            Cell_Node current = unvisited_nodes.pop();
            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            for(Cell_Node neighbour: current.getNeighbours()){
                int[] neighbour_pos = A_Star.identify_CellPos(neighbour, grid_nodes);
                neighbour.posOnGrid = neighbour_pos;

                if(!visited.contains(neighbour)){
                    neighbour.parent = current;

                    if(A_Star.cell_isDestination(grid_nodes, neighbour_pos[0], neighbour_pos[1], image_dict)){
                        foundDestination = true;
                        return A_Star.write_constructedPath(neighbour, start, goal);
                    }

                    unvisited_nodes.push(neighbour);
                    visited.add(neighbour);
                }
            }
        }

        if(unvisited_nodes.isEmpty()){
            System.out.println("Could Not Find the Destination");
        }

        return null;

    }
}
