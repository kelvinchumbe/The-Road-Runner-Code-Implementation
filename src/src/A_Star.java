import javafx.scene.image.Image;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class A_Star {

    @SuppressWarnings("Duplicates")
    public static int[] identify_StartPos(Node runner, Node[][] grid_nodes){
        int[] start_pos = null;

        for(int i=0; i < grid_nodes.length; i++) {
            for (int j = 0; j < grid_nodes[i].length; j++) {
                if (grid_nodes[i][j].equals(runner)) {
                    start_pos = new int[]{i, j};
                }
            }
        }
        return start_pos;
    }

    @SuppressWarnings("Duplicates")
    public static int[] identify_GoalPos(Node goal, Node[][] grid_nodes){
        int[] goal_pos = null;

        for(int i=0; i < grid_nodes.length; i++) {
            for (int j = 0; j < grid_nodes[i].length; j++) {
                if (grid_nodes[i][j].equals(goal)) {
                    goal_pos = new int[]{i, j};
                }
            }
        }
        return goal_pos;
    }

    @SuppressWarnings("Duplicates")
    public static int[] identify_CellPos(Node node, Node[][] grid_nodes){
        int[] cell_pos = null;

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
                if(grid_nodes[i][j].equals(node)){
                    cell_pos = new int[]{i, j};
                }
            }
        }
        return cell_pos;
    }




    public static boolean cell_isValid(Node[][] grid_nodes, int row, int col){
        return  (row >= 0 && row < grid_nodes.length) && (col >= 0 && col < grid_nodes[1].length);
    }

    @SuppressWarnings("Duplicates")
    public static boolean cell_isBlocked(Node[][] grid_nodes, int row, int col, HashMap<Integer,Image> image_dict){
        int dict_key = 0;

        for(Integer key: image_dict.keySet()){
            if(image_dict.get(key).equals(grid_nodes[row][col].getImageView().getImage())){
                dict_key = key;
            }
        }
        return dict_key == 1;
    }

    @SuppressWarnings("Duplicates")
    public static boolean cell_isDestination(Node[][] grid_nodes, int row, int col, HashMap<Integer,Image> image_dict){
//        return map.get(row)[col] == 9;
        int dict_key = 0;

        for(Integer key: image_dict.keySet()){
            if(image_dict.get(key).equals(grid_nodes[row][col].getImageView().getImage())){
                dict_key = key;
            }
        }
        return dict_key == 9;
    }

    public static int calculateHeuristic_4D(int row, int col, Node goal, Node[][] grid_nodes){
        int[] goal_pos = identify_GoalPos(goal, grid_nodes);

        return Math.abs(row - goal_pos[0]) + Math.abs(col - goal_pos[1]);
    }

    public static int calculateHeuristic_8D(int row, int col, Node goal, Node[][] grid_nodes){
        int[] goal_pos = identify_GoalPos(goal, grid_nodes);

        return Math.max(Math.abs(row - goal_pos[0]), Math.abs(col - goal_pos[1]));
    }

//    public static ArrayList<int[]> tracePath(HashMap<int[],int[]> path_map, Node[][] grid_nodes, Node node){
////        int[] goal_pos = identify_GoalPos(goal, grid_nodes);
////        int[] start_pos = identify_StartPos(start, grid_nodes);
////        int[] key = goal_pos;
////
////        while(!Arrays.equals(path_map.get(key), start_pos)){
////            System.out.println(Arrays.toString(path_map.get(key)));
////            key = path_map.get(key);
////        }
//        int[] node_pos = identify_CellPos(node, grid_nodes);
//        ArrayList<int[]> total_path = new ArrayList<>();
//        total_path.add(node_pos);
//
//        while(path_map.keySet().contains(node_pos)){
//            node_pos = path_map.get(node_pos);
//            total_path.add(node_pos);
//        }
//        return total_path;
//    }

    public static void write_constructedPath(Node node, Node start, Node goal) throws IOException {
        System.out.println("_______________________________________");
        ArrayList<Node> total_path = new ArrayList<>();
        total_path.add(node);

        while(node.parent != null){
            node = node.parent;
            total_path.add(node);
        }

        String path = "C:\\Users\\Kelvin Kinda\\IdeaProjects\\pp-ii-the-road-runner-kelvin-judith\\src\\src\\output_runner_path.txt";
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        FileWriter bufferedWriter = new FileWriter(path);

//        bufferedWriter.write("Hello");
        bufferedWriter.write("Start "); //Arrays.toString(start.posOnGrid));
        for(int pos: start.posOnGrid){
            bufferedWriter.append(pos + " ");
        }
        bufferedWriter.append("\n");

        bufferedWriter.write("Goal "); // + Arrays.toString(goal.posOnGrid));
        for(int pos: goal.posOnGrid){
            bufferedWriter.append(pos + " ");
        }
        bufferedWriter.append("\n");

        for(int i = total_path.size() - 1; i > 0; i--){
            int[] arr1 = total_path.get(i).posOnGrid;
            int[] arr2 = total_path.get(i - 1).posOnGrid;
            StringBuilder direction = new StringBuilder();

            int var1 = arr1[0] - arr2[0];
            int var2 = arr1[1] - arr2[1];

            if(var1 == 1){
                direction.append("North ");
//                bufferedWriter.append("North \n");
//                bufferedWriter.append("\n");
            }

            if(var1 == -1){
                direction.append("South ");
//                bufferedWriter.append("South \n");
            }

            if(var2 == 1){
                direction.append("West");
//                bufferedWriter.append("West \n");
            }

            if(var2 == -1){
                direction.append("East");
//                bufferedWriter.append("East \n");
            }

            System.out.println(direction);
            bufferedWriter.append(direction + "\n");

        }
        bufferedWriter.close();

        System.out.println(" =============================");

        for(Node node1: total_path){
            System.out.println(Arrays.toString(node1.posOnGrid));
        }
//        return total_path;
    }

    @SuppressWarnings("Duplicates")
    public static void A_StarSearch_4D(Node[][] grid_nodes, Node start, Node goal, HashMap<Integer,Image> image_dict) throws IOException {
        int[] goal_pos = identify_GoalPos(goal, grid_nodes);
        int[] start_pos = identify_StartPos(start, grid_nodes);


        if(!cell_isValid(grid_nodes, goal_pos[0], goal_pos[1])){
            System.out.println("Goal Cell Not Valid");
        }

        if(!cell_isValid(grid_nodes, start_pos[0], start_pos[1])){
            System.out.println("Start Cell Not Valid");
        }

        if(cell_isBlocked(grid_nodes, goal_pos[0], goal_pos[1], image_dict)){
            System.out.println("Goal Cell is Blocked");
        }

        if(cell_isBlocked(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("Start Cell is Blocked");
        }

        if(cell_isDestination(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("We are Already at out Destination");
        }


        Set<Node> closedSet = new HashSet<>();
        Set<Node> openSet = new HashSet<>();
        openSet.add(start);

        boolean foundDestination = false;

        HashMap<Node,Node> cameFromPath = new HashMap<>();

        start.setgScore(0);
        start.setH(calculateHeuristic_4D(start_pos[0], start_pos[1], goal, grid_nodes));
        start.setfScore(start.getgScore() + start.getH());

        goal.setgScore(0);
        goal.setfScore(0);

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
//                    grid_nodes[i][j].setH(calculateHeuristic_4D(i, j, goal, grid_nodes));
//
//                    for(Integer key: image_dict.keySet()){
//                        if(image_dict.get(key).equals(grid_nodes[i][j].getImageView().getImage())){
//                            switch (key){
//                                case 0:
//                                    grid_nodes[i][j].setgScore(-1);
//                                    break;
//                                case 2:
//                                    grid_nodes[i][j].setgScore(-2);
//                                    break;
//                                case 3:
//                                    grid_nodes[i][j].setgScore(-4);
//                                    break;
//                                case 4:
//                                    grid_nodes[i][j].setgScore(-8);
//                                    break;
//                                case 5:
//                                    grid_nodes[i][j].setgScore(1);
//                                    break;
//                                case 6:
//                                    grid_nodes[i][j].setgScore(5);
//                                    break;
//                            }
//                        }
//                    }

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


        while(!openSet.isEmpty()){
            Node current = null;
            int least = (int) Double.POSITIVE_INFINITY;
            for(Node node: openSet){
                if(node.getfScore() < least){
                    current = node;
                    least = current.getfScore();
                }
            }

            int[] current_pos = identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


//            If current is our goal return the constructed path
            if(cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                write_constructedPath(current, start, goal);
                foundDestination = true;
            }

            openSet.remove(current);
            closedSet.add(current);

            for(Node neighbour: current.getNeighbours()){
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

                cameFromPath.put(neighbour, current);
                neighbour.parent = current;
                neighbour.setgScore(temp_gScore);
                neighbour.setH(calculateHeuristic_4D(neighbour_pos[0], neighbour_pos[1], goal, grid_nodes));
                neighbour.setfScore(neighbour.getgScore() + neighbour.getH());
            }
        }

        if(openSet.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }

    }

    @SuppressWarnings("Duplicates")
    public static void A_StarSearch_8D(Node[][] grid_nodes, Node start, Node goal, HashMap<Integer,Image> image_dict) throws IOException {
        int[] goal_pos = identify_GoalPos(goal, grid_nodes);
        int[] start_pos = identify_StartPos(start, grid_nodes);


        if(!cell_isValid(grid_nodes, goal_pos[0], goal_pos[1])){
            System.out.println("Goal Cell Not Valid");
        }

        if(!cell_isValid(grid_nodes, start_pos[0], start_pos[1])){
            System.out.println("Start Cell Not Valid");
        }

        if(cell_isBlocked(grid_nodes, goal_pos[0], goal_pos[1], image_dict)){
            System.out.println("Goal Cell is Blocked");
        }

        if(cell_isBlocked(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("Start Cell is Blocked");
        }

        if(cell_isDestination(grid_nodes, start_pos[0], start_pos[1], image_dict)){
            System.out.println("We are Already at out Destination");
        }


        Set<Node> closedSet = new HashSet<>();
        Set<Node> openSet = new HashSet<>();
        openSet.add(start);

        boolean foundDestination = false;

        HashMap<Node,Node> cameFromPath = new HashMap<>();

        start.setgScore(0);
        start.setH(calculateHeuristic_8D(start_pos[0], start_pos[1], goal, grid_nodes));
        start.setfScore(start.getgScore() + start.getH());

        goal.setgScore(0);
        goal.setfScore(0);

        for(int i=0; i < grid_nodes.length; i++){
            for(int j=0; j < grid_nodes[i].length; j++){
//                    grid_nodes[i][j].setH(calculateHeuristic_4D(i, j, goal, grid_nodes));
//
//                    for(Integer key: image_dict.keySet()){
//                        if(image_dict.get(key).equals(grid_nodes[i][j].getImageView().getImage())){
//                            switch (key){
//                                case 0:
//                                    grid_nodes[i][j].setgScore(-1);
//                                    break;
//                                case 2:
//                                    grid_nodes[i][j].setgScore(-2);
//                                    break;
//                                case 3:
//                                    grid_nodes[i][j].setgScore(-4);
//                                    break;
//                                case 4:
//                                    grid_nodes[i][j].setgScore(-8);
//                                    break;
//                                case 5:
//                                    grid_nodes[i][j].setgScore(1);
//                                    break;
//                                case 6:
//                                    grid_nodes[i][j].setgScore(5);
//                                    break;
//                            }
//                        }
//                    }

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


        while(!openSet.isEmpty()){
            //Use a Min Queue instead to get the node with the least fScore
            Node current = null;
            int least = (int) Double.POSITIVE_INFINITY;
            for(Node node: openSet){
                if(node.getfScore() < least){
                    current = node;
                    least = current.getfScore();
                }
            }

            int[] current_pos = identify_CellPos(current, grid_nodes);
            current.posOnGrid = current_pos;


//            If current is our goal return the constructed path
            if(cell_isDestination(grid_nodes, current_pos[0], current_pos[1], image_dict)){
                write_constructedPath(current, start, goal);
                foundDestination = true;
            }

            openSet.remove(current);
            closedSet.add(current);

            for(Node neighbour: current.getNeighbours()){
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

                cameFromPath.put(neighbour, current);
                neighbour.parent = current;
                neighbour.setgScore(temp_gScore);
                neighbour.setH(calculateHeuristic_8D(neighbour_pos[0], neighbour_pos[1], goal, grid_nodes));
                neighbour.setfScore(neighbour.getgScore() + neighbour.getH());
            }
        }

        if(openSet.isEmpty() && !foundDestination){
            System.out.println("Could Not Find the Destination");
        }

    }

}
