import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Runner_Movement {

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to check is a cell is blocked. Uses the map as a reference to do in in O(1) time
    public static boolean is_Cell_Blocked(int x, int y, ArrayList<int[]> map){
        return map.get(x)[y] == 1;
    }

//    public static boolean is_Destination(int x, int y, )

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to replace an image at a specific position on the grid and update the grid nodes array
    public static void replace_image(GridPane grid, ImageView runner, ArrayList<int[]> map, HashMap<Integer,Image> image_alt_dict, int x, int y, Cell_Node[][] grid_nodes){
        Integer image_to_replace_key = map.get(x)[y];

        ImageView alt_image = new ImageView(image_alt_dict.get(image_to_replace_key));
        alt_image.setFitWidth(20);
        alt_image.setFitHeight(20);
        grid.getChildren().remove(runner);
        grid.add(alt_image, y, x);
        grid_nodes[x][y] = new Cell_Node(alt_image);
    }

    /**
     Time Complexity: O(N)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
    // function to check is the runner has already been to a specific position
    public static boolean runner_has_visited_cell(int x, int y, ArrayList<int[]> visited_cells){
        for(int i=0; i < visited_cells.size(); i++){
            if(visited_cells.get(i)[0] == x && visited_cells.get(i)[1] == y){
                return true;
            }
        }
        return false;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    /// function to move the runner up on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveUp(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0){
            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos - 1, runner_ypos, map)){
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_xpos -= 1;
                    grid.add(runner, runner_ypos, runner_xpos);

                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE UPPER THAN THIS");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner down on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveDown(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1){
            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos + 1, runner_ypos, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_xpos += 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE DOWNER THAN THIS");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner left on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveLeft(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos, runner_ypos - 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_ypos -= 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE LEFTER THAN THIS");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner right on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveRight(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos, runner_ypos + 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_ypos += 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE RIGHTER THAN THIS");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner to the North East on the grid
    public static int[] moveNorthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos - 1, runner_ypos + 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_ypos += 1;
                    runner_xpos -= 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE North East");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner to the North West on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveNorthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos - 1, runner_ypos - 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_xpos -= 1;
                    runner_ypos -= 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE North West");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner South East on the grid
    @SuppressWarnings("Duplicates")
    public static int[] moveSouthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos + 1, runner_ypos + 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_xpos += 1;
                    runner_ypos += 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE South East");
        }
        return null;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(1)
     */
    // function to move the runner South West on the grid
    public static int[] moveSouthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Cell_Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                if(!is_Cell_Blocked(runner_xpos + 1, runner_ypos - 1, map)) {
                    replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                    runner_ypos -= 1;
                    runner_xpos += 1;
                    grid.add(runner, runner_ypos, runner_xpos);
                    return new int[]{runner_xpos, runner_ypos};
                }
            }
        }
        else{
            System.out.println("CANNOT MOVE South West");
        }
        return null;
    }
}
