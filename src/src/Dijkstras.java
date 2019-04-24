import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dijkstras {
    @SuppressWarnings("Duplicates")
    public static void Dijkstras_Search_8D(Node[][] grid_nodes, Node start, Node goal, HashMap<Integer,Image>image_dict) throws IOException {
        Set<Node> unvisited_nodes = new HashSet<>();
        Set<Node> traced_path = new HashSet<>();
        traced_path.add(start);

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
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

                for(Integer key: image_dict.keySet()){
                    if(image_dict.get(key).equals(grid_nodes[i][j].getImageView().getImage())){
                        switch (key){
                            case 0:
                                grid_nodes[i][j].energy_cost = 8;
                                break;
                            case 2:
                                grid_nodes[i][j].energy_cost = 7;
                                break;
                            case 3:
                                grid_nodes[i][j].energy_cost = 5;
                                break;
                            case 4:
                                grid_nodes[i][j].energy_cost = 1;
                                break;
                            case 5:
                                grid_nodes[i][j].energy_cost = 10;
                                break;
                            case 6:
                                grid_nodes[i][j].energy_cost = 14;
                                break;
                        }
                    }
                }
            }
        }

        for(Node[] node_arr: grid_nodes){
            unvisited_nodes.addAll(Arrays.asList(node_arr));
        }

        start.setgScore(0);
        boolean foundDestination = false;

        while(!unvisited_nodes.isEmpty() && !foundDestination){
            Node current = null;
            int max = (int) Double.NEGATIVE_INFINITY;
            for(Node node: unvisited_nodes){
                if(node.getgScore() > max){
                    current = node;
                    max = current.getgScore();
                }
            }

            unvisited_nodes.remove(current);
//            traced_path.add(current);


            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            if(A_Star.cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                A_Star.write_constructedPath(current, start, goal);
                foundDestination = true;
                break;
            }


            for(Node neighbour: current.getNeighbours()){
                int temp_Score = current.getgScore() + (neighbour.energy_cost - current.energy_cost);

                if(temp_Score > neighbour.getgScore()){
                    neighbour.setgScore(temp_Score);
                    neighbour.parent = current;
                }
            }



        }
        if(unvisited_nodes.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }

    }


    @SuppressWarnings("Duplicates")
    public static void Dijkstras_Search_4D(Node[][] grid_nodes, Node start, Node goal, HashMap<Integer,Image>image_dict) throws IOException {
        Set<Node> unvisited_nodes = new HashSet<>();
//        Set<Node> traced_path = new HashSet<>();
//        traced_path.add(start);

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){

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

        for(Node[] node_arr: grid_nodes){
            unvisited_nodes.addAll(Arrays.asList(node_arr));
        }

        start.setgScore(0);
        boolean foundDestination = false;

        while(!unvisited_nodes.isEmpty() && !foundDestination){
            Node current = null;
            int max = (int) Double.NEGATIVE_INFINITY;
            for(Node node: unvisited_nodes){
                if(node.getgScore() > max){
                    current = node;
                    max = current.getgScore();
                }
            }

            unvisited_nodes.remove(current);
//            traced_path.add(current);


            int[] current_pos = A_Star.identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            if(A_Star.cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                A_Star.write_constructedPath(current, start, goal);
                foundDestination = true;
                break;
            }


            for(Node neighbour: current.getNeighbours()){
                int temp_Score = current.getgScore() + (current.getgScore() + neighbour.energy_cost);

                if(temp_Score > neighbour.getgScore()){
                    neighbour.setgScore(temp_Score);
                    neighbour.parent = current;
                }
            }



        }
        if(unvisited_nodes.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }

    }
}
