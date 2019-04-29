import javafx.scene.control.Cell;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// A_Star
public class A_Star {
    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to identify the position of the node in the grid
    @SuppressWarnings("Duplicates")
    public static int[] identify_CellPos(Cell_Node node, Cell_Node[][] grid_nodes){
        int[] cell_pos = null;

        // go over the grid and search for the node
        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
                if(grid_nodes[i][j].equals(node)){
                    cell_pos = new int[]{i, j};
                }
            }
        }
        // return the nodes position on the grid
        return cell_pos;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(1)
     Auxiliary Space: O(1)
     */
// check if the coordinates of the cell in the grid are valid
    public static boolean cell_isValid(Cell_Node[][] grid_nodes, int row, int col){
        return  (row >= 0 && row < grid_nodes.length) && (col >= 0 && col < grid_nodes[1].length);
    }

    /**
     Time Complexity: O(N)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // check if cell is blocked by a boulder
    @SuppressWarnings("Duplicates")
    public static boolean cell_isBlocked(Cell_Node[][] grid_nodes, int row, int col, HashMap<Integer,Image> image_dict){
        int dict_key = 0;

        for(Integer key: image_dict.keySet()){
            if(image_dict.get(key).equals(grid_nodes[row][col].getImageView().getImage())){
                dict_key = key;
            }
        }
        return dict_key == 1;
    }

    /**
     Time Complexity: O(N)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // check if the cell is the destination/ goal cell
    @SuppressWarnings("Duplicates")
    public static boolean cell_isDestination(Cell_Node[][] grid_nodes, int row, int col, HashMap<Integer,Image> image_dict){
//        return map.get(row)[col] == 9;
        int dict_key = 0;

        for(Integer key: image_dict.keySet()){
            if(image_dict.get(key).equals(grid_nodes[row][col].getImageView().getImage())){
                dict_key = key;
            }
        }
        return dict_key == 9;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
    // function to calculate the heuristic value of the cell from the goal (how far the cell is from the goal) with four neighbours
    public static int calculateHeuristic_4D(int row, int col, Cell_Node goal, Cell_Node[][] grid_nodes){
        int[] goal_pos = identify_CellPos(goal, grid_nodes);

        return Math.abs(row - goal_pos[0]) + Math.abs(col - goal_pos[1]);
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
    // function to calculate the heuristic value of the cell from the goal (how far the cell is from the goal) with eight neighbours
    public static int calculateHeuristic_8D(int row, int col, Cell_Node goal, Cell_Node[][] grid_nodes){
        int[] goal_pos = identify_CellPos(goal, grid_nodes);

        return Math.max(Math.abs(row - goal_pos[0]), Math.abs(col - goal_pos[1]));
    }

    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to construct the path the runner took after reaching the goal
    public static ArrayList<String> write_constructedPath(Cell_Node node, Cell_Node start, Cell_Node goal) throws IOException {
        ArrayList<Cell_Node> total_path = new ArrayList<>();
        ArrayList<String> runner_path_from_start = new ArrayList<>();
        total_path.add(node);

        while(node.parent != null){
            node = node.parent;
            total_path.add(node);
        }

        // path to the output file
        String path = "C:\\Users\\Kelvin Kinda\\IdeaProjects\\pp-ii-the-road-runner-kelvin-judith\\src\\src\\Output Files\\output_" + new SimpleDateFormat("yyyy-MM-dd HH.mm").format(new Date()) + ".txt";

        File output_file = new File(path);
        output_file.createNewFile();

        // write to the output file
        FileWriter bufferedWriter = new FileWriter(output_file);


        // output Start and the coordinates of the start node
        bufferedWriter.write("Start ");
        for(int pos: start.posOnGrid){
            bufferedWriter.append(pos + " ");
        }
        bufferedWriter.append("\n");

        // output Start and the coordinates of the goal node
        bufferedWriter.write("Goal ");
        for(int pos: goal.posOnGrid){
            bufferedWriter.append(pos + " ");
        }
        bufferedWriter.append("\n");

        // for each of the next lines, write the direction the roadrunner takes from the start cell to the next cell
        for(int i = total_path.size() - 1; i > 0; i--){
            int[] arr1 = total_path.get(i).posOnGrid;
            int[] arr2 = total_path.get(i - 1).posOnGrid;

            // use string builder to append the direction of the runner depending on the value of the x and y coordinate
            StringBuilder direction = new StringBuilder();

            // get the difference between the runners current x and next x and runners y and next y
            int var1 = arr1[0] - arr2[0];
            int var2 = arr1[1] - arr2[1];

            // check if the runner's x difference is positive, if so then she moved north. Append the value to the string builder
            if(var1 == 1){
                direction.append("North ");
            }

            // check if the runner's x difference is negative, if so then she moved south. Append the value to the string builder
            if(var1 == -1){
                direction.append("South ");
            }

            // check if the runner's y difference is positive, if so then she moved west. Append the value to the string builder
            if(var2 == 1){
                direction.append("West");
            }

            // check if the runner's y difference is negative, if so then she moved east. Append the value to the string builder
            if(var2 == -1){
                direction.append("East");
            }

            // print to screen the direction the road runner took and write it to file
            System.out.println(direction);
            bufferedWriter.append(direction + "\n");
            runner_path_from_start.add(String.valueOf(direction));
        }

        bufferedWriter.close();

        // print to screen the road runners positions as traced from the goal
        for(Cell_Node node1: total_path){
            System.out.println(Arrays.toString(node1.posOnGrid));
        }
        return runner_path_from_start;
    }

    // loop to update all grid nodes g and f scores with positive infinity.
    // update the nodes with their valid neighbours(4 max) i.e coordinates are within the grid and are not blocked.
    public static void set4Neighbours(Cell_Node[][] grid_nodes, HashMap<Integer,Image> image_dict){
        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
                grid_nodes[i][j].setfScore((int) Double.POSITIVE_INFINITY);
                grid_nodes[i][j].setgScore((int) Double.POSITIVE_INFINITY);

                if(cell_isValid(grid_nodes, i-1, j) && !cell_isBlocked(grid_nodes, i-1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j]);
                }
                if(cell_isValid(grid_nodes, i, j-1) && !cell_isBlocked(grid_nodes, i, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j-1]);
                }
                if(cell_isValid(grid_nodes, i, j+1) && !cell_isBlocked(grid_nodes, i, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j+1]);
                }
                if(cell_isValid(grid_nodes, i+1, j) && !cell_isBlocked(grid_nodes, i+1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j]);
                }
            }
        }
    }

    // loop to update all grid nodes g and f scores with positive infinity.
    // update the nodes with their valid neighbours (8 max) i.e coordinates are within the grid and are not blocked.
    public static void set8Neighbours(Cell_Node[][] grid_nodes, HashMap<Integer,Image> image_dict){
        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
//                grid_nodes[i][j].setfScore((int) Double.POSITIVE_INFINITY);
//                grid_nodes[i][j].setgScore((int) Double.POSITIVE_INFINITY);

                if(cell_isValid(grid_nodes,i-1, j-1) && !cell_isBlocked(grid_nodes, i-1, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j-1]);
                }
                if(cell_isValid(grid_nodes, i-1, j) && !cell_isBlocked(grid_nodes, i-1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j]);
                }
                if(cell_isValid(grid_nodes, i-1, j+1) && !cell_isBlocked(grid_nodes, i-1, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i-1][j+1]);
                }
                if(cell_isValid(grid_nodes, i, j-1) && !cell_isBlocked(grid_nodes, i, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j-1]);
                }
                if(cell_isValid(grid_nodes, i, j+1) && !cell_isBlocked(grid_nodes, i, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i][j+1]);
                }
                if(cell_isValid(grid_nodes, i+1, j-1) && !cell_isBlocked(grid_nodes, i+1, j-1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j-1]);
                }
                if(cell_isValid(grid_nodes, i+1, j) && !cell_isBlocked(grid_nodes, i+1, j, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j]);
                }
                if(cell_isValid(grid_nodes, i+1, j+1) && !cell_isBlocked(grid_nodes, i+1, j+1, image_dict)){
                    grid_nodes[i][j].addNeighbours(grid_nodes[i+1][j+1]);
                }
            }
        }
    }


    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to trace the runner's shortest path using A* algorithm to the goal with only four directions to take from a node
    @SuppressWarnings("Duplicates")
    public static ArrayList<String> A_StarSearch_4D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer,Image> image_dict) throws IOException {
        // get the goal position on the grid
        int[] goal_pos = identify_CellPos(goal, grid_nodes);

        // get the start position on the grid
        int[] start_pos = identify_CellPos(start, grid_nodes);


        // check if the goal cell is valid
        if(!cell_isValid(grid_nodes, goal_pos[0], goal_pos[1])){
            System.out.println("Goal Cell Not Valid");
            return null;
        }

        // check if the start node is valid
        if(!cell_isValid(grid_nodes, start_pos[0], start_pos[1])){
            System.out.println("Start Cell Not Valid");
            return null;
        }

        // check if the goal is blocked. i.e is a boulder
        if(cell_isBlocked(grid_nodes, goal_pos[0], goal_pos[1], image_dict)){
            System.out.println("Goal Cell is Blocked");
            return null;
        }

        // check if the start is blocked i.e is a boulder
        if(cell_isBlocked(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("Start Cell is Blocked");
            return null;
        }

        // check if the start node is our destination. if so, then we are already at our destination
        if(cell_isDestination(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("We are Already at out Destination");
            return null;
        }

        // create a set to store all the visited nodes in the grid
        Set<Cell_Node> closedSet = new HashSet<>();

        // create a set to store all the unvisited nodes in the grid
        Set<Cell_Node> openSet = new HashSet<>();

        // initialize the unvisited set with the start node
        openSet.add(start);

        // variable to check if we've reached our destination
        boolean foundDestination = false;

        set4Neighbours(grid_nodes, image_dict);

        // initialize the start node's gscore as 0
        start.setgScore(0);

        // initialize the start node's heuristic as 0 using the heuristics formula
        start.setH(calculateHeuristic_4D(start_pos[0], start_pos[1], goal, grid_nodes));

        // initialize the start node's fscore as the sum of its gscore and fscore
        start.setfScore(start.getgScore() + start.getH());

        // while there's still a valid unvisited node, do this
        while(!openSet.isEmpty()){

            // find the node with the least fscore value in the open set and set it as our current
            Cell_Node current = null;
            int least = (int) Double.POSITIVE_INFINITY;
            for(Cell_Node node: openSet){
                if(node.getfScore() < least){
                    current = node;
                    least = current.getfScore();
                }
            }

            if(current == null){
                break;
            }

            // get the current node's position on the grid
            int[] current_pos = identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


//            If current is our goal write the constructed path and set our foundDestination variable to true
            if(cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                foundDestination = true;
                return write_constructedPath(current, start, goal);

            }

            // remove the current from the open set
            openSet.remove(current);

            // add the current node to the closed set
            closedSet.add(current);

            // for each of the current's neighbour nodes..
            for(Cell_Node neighbour: current.getNeighbours()){
                // get is position on the grid
                int[] neighbour_pos = identify_CellPos(neighbour, grid_nodes);
                neighbour.posOnGrid = neighbour_pos;

                // check if the neighbour node is in the closed set
                if(closedSet.contains(neighbour)){
                    continue;
                }

                // create a var to store the current's gscore + distance from the neighbour node to the current cell
                int temp_gScore = current.getgScore() + 1;

                // if the neighbour is in the openset and has its gscore less than our temp variable, remove it from the open set
                if(openSet.contains(neighbour) && temp_gScore < neighbour.getgScore()){
                    openSet.remove(neighbour);
                }

                // if the neighbour is in the closed set and has its gscore less than the temp variable, remove it from the closed set
                if(closedSet.contains(neighbour) && temp_gScore < neighbour.getgScore()){
                    closedSet.remove(neighbour);
                }

                // check if the neighbour is not in the open set and add it if its not
                if(!openSet.contains(neighbour)){
                    openSet.add(neighbour);
                }
                // if the neighbours gscore is less or equal than our temp variable, move on to the next neighbour
                else if(temp_gScore >= neighbour.getgScore()){
                    continue;
                }

//                cameFromPath.put(neighbour, current);
                // update the neighbour's parent to the current node
                neighbour.parent = current;

                // update the neighbour's gscore the temp variable
                neighbour.setgScore(temp_gScore);

                // calculate the neighbour's h value, distance from the goal node
                neighbour.setH(calculateHeuristic_4D(neighbour_pos[0], neighbour_pos[1], goal, grid_nodes));

                // update the neighbour's fscore to be the sum of the neighbour's gscore and its h value
                neighbour.setfScore(neighbour.getgScore() + neighbour.getH());
            }
        }

        // if we've exhausted nodes in the open set and havent found the destination, alert the user on the screen
        if(openSet.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }
        return null;

    }

    /**
     Time Complexity: O(N^2)
     Space Complexity: O(N^2)
     Auxiliary Space: O(N)
     */
    // function to trace the runner's shortest path using A* algorithm to the goal with only eight directions to take from a node
    @SuppressWarnings("Duplicates")
    public static ArrayList<String> A_StarSearch_8D(Cell_Node[][] grid_nodes, Cell_Node start, Cell_Node goal, HashMap<Integer,Image> image_dict) throws IOException {
        int[] goal_pos = identify_CellPos(goal, grid_nodes);
        int[] start_pos = identify_CellPos(start, grid_nodes);


        if(!cell_isValid(grid_nodes, goal_pos[0], goal_pos[1])){
            System.out.println("Goal Cell Not Valid");
            return null;
        }

        if(!cell_isValid(grid_nodes, start_pos[0], start_pos[1])){
            System.out.println("Start Cell Not Valid");
            return null;
        }

        if(cell_isBlocked(grid_nodes, goal_pos[0], goal_pos[1], image_dict)){
            System.out.println("Goal Cell is Blocked");
            return null;
        }

        if(cell_isBlocked(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("Start Cell is Blocked");
            return null;
        }

        if(cell_isDestination(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("We are Already at out Destination");
            return null;
        }


        Set<Cell_Node> closedSet = new HashSet<>();
        Set<Cell_Node> openSet = new HashSet<>();
        openSet.add(start);

        boolean foundDestination = false;

        set8Neighbours(grid_nodes, image_dict);

        start.setgScore(0);
        start.setH(calculateHeuristic_8D(start_pos[0], start_pos[1], goal, grid_nodes));
        start.setfScore(start.getgScore() + start.getH());


        while(!openSet.isEmpty()){
            //Use a Min Queue instead to get the node with the least fScore
            Cell_Node current = null;
            int least = (int) Double.POSITIVE_INFINITY;
            for(Cell_Node node: openSet){
                if(node.getfScore() < least){
                    current = node;
                    least = current.getfScore();
                }
            }

            if(current == null){
                break;
            }

            int[] current_pos = identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


            // If current is our goal return the constructed path
            if(cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                foundDestination = true;
                return write_constructedPath(current, start, goal);

            }

            openSet.remove(current);
            closedSet.add(current);

            for(Cell_Node neighbour: current.getNeighbours()){
                int[] neighbour_pos = identify_CellPos(neighbour, grid_nodes);
                neighbour.posOnGrid = neighbour_pos;

                if(closedSet.contains(neighbour)){
                    continue;
                }

                int temp_gScore = current.getgScore() + 1;

                if(openSet.contains(neighbour) && temp_gScore < neighbour.getgScore()){
                    openSet.remove(neighbour);
                }

                if(closedSet.contains(neighbour) && temp_gScore < neighbour.getgScore()){
                    closedSet.remove(neighbour);
                }

                if(!openSet.contains(neighbour)){
                    openSet.add(neighbour);
                }
                else if(temp_gScore >= neighbour.getgScore()){
                    continue;
                }

                neighbour.parent = current;
                neighbour.setgScore(temp_gScore);
                neighbour.setH(calculateHeuristic_8D(neighbour_pos[0], neighbour_pos[1], goal, grid_nodes));
                neighbour.setfScore(neighbour.getgScore() + neighbour.getH());
            }
        }

        if(openSet.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }
        return  null;
    }

}
