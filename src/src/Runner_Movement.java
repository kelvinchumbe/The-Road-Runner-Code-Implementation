import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Runner_Movement {

    public static void replace_image(GridPane grid, ImageView runner, ArrayList<int[]> map, HashMap<Integer,Image> image_alt_dict, int x, int y, Node[][] grid_nodes){
        Integer image_to_replace_key = map.get(x)[y];

        ImageView alt_image = new ImageView(image_alt_dict.get(image_to_replace_key));
        alt_image.setFitWidth(100);
        alt_image.setFitHeight(100);
        grid.getChildren().remove(runner);
        grid.add(alt_image, y, x);
        grid_nodes[x][y] = new Node(alt_image);
    }

    public static boolean runner_has_visited_cell(int x, int y, ArrayList<int[]> visited_cells){
        for(int i=0; i < visited_cells.size(); i++){
            if(visited_cells.get(i)[0] == x && visited_cells.get(i)[1] == y){
                return true;
            }
//            else if(visited_cells.get(i) == null){
//                return false;
//            }
        }

        return false;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveUp(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0){
//            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);
//            runner_xpos -= 1;

//            next_cell_pos = new int[]{runner_xpos - 1, runner_ypos};

            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_xpos -= 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE UPPER THAN THIS");
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveDown(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1){
//            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);
//            runner_xpos += 1;

//            next_cell_pos = new int[]{runner_xpos + 1, runner_ypos};

            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_xpos += 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE DOWNER THAN THIS");
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveLeft(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_ypos -= 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE LEFTER THAN THIS");
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveRight(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_ypos += 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE RIGHTER THAN THIS");
        }
        return null;
    }

    public static int[] moveNorthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_ypos += 1;
                runner_xpos -= 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE North East");
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveNorthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos - 1, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_xpos -= 1;
                runner_ypos -= 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE North West");
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static int[] moveSouthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos < map.get(1).length - 1){

            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos + 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_xpos += 1;
                runner_ypos += 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE South East");
        }
        return null;
    }

    public static int[] moveSouthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells, Node[][] grid_nodes){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos > 0){

            if(runner_has_visited_cell(runner_xpos + 1, runner_ypos - 1, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos, grid_nodes);
                runner_ypos -= 1;
                runner_xpos += 1;
                grid.add(runner, runner_ypos, runner_xpos);
                return new int[]{runner_xpos, runner_ypos};
            }
        }
        else{
            System.out.println("CANNOT MOVE South West");
        }
        return null;
    }
}
