import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;

public class Runner_Movement {

    public static void replace_image(GridPane grid, ImageView runner, ArrayList<int[]> map, HashMap<Integer,Image> image_alt_dict, int x, int y){
        Integer image_to_replace_key = map.get(x)[y];

        ImageView alt_image = new ImageView(image_alt_dict.get(image_to_replace_key));
        alt_image.setFitWidth(100);
        alt_image.setFitHeight(100);
        grid.getChildren().remove(runner);
        grid.add(alt_image, y, x);
    }

    public static boolean runner_has_visited_cell(int x, int y, ArrayList<int[]> visited_cells){
        int[] runner_new_pos = {x, y};

        return visited_cells.contains(runner_new_pos);
    }

    public static void moveUp(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_xpos -= 1;

            if(runner_has_visited_cell(runner_xpos, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                grid.add(runner, runner_ypos, runner_xpos);
                visited_cells.add(new int[]{runner_xpos, runner_ypos});
            }
        }
        else{
            System.out.println("CANNOT MOVE UPPER THAN THIS");
        }
    }

    public static void moveDown(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_xpos += 1;

            if(runner_has_visited_cell(runner_xpos, runner_ypos, visited_cells)){
                System.out.println("Runner has already visited this cell");
            }
            else{
                grid.add(runner, runner_ypos, runner_xpos);
                visited_cells.add(new int[]{runner_xpos, runner_ypos});
            }
        }
        else{
            System.out.println("CANNOT MOVE DOWNER THAN THIS");
        }
    }

    public static void moveNorthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos < map.get(1).length - 1){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_ypos += 1;
            runner_xpos -= 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE North East");
        }
    }

    public static void moveNorthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos > 0 && runner_ypos > 0){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_xpos -= 1;
            runner_ypos -= 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE North West");
        }
    }

    public static void moveSouthEast(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos < map.get(1).length - 1){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_xpos += 1;
            runner_ypos += 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE South East");
        }
    }

    public static void moveSouthWest(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_xpos < map.size() - 1 && runner_ypos > 0){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_ypos -= 1;
            runner_xpos += 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE South West");
        }
    }





    public static void moveLeft(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos > 0){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_ypos -= 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE LEFTER THAN THIS");
        }
    }

    public static void moveRight(GridPane grid, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map){
        int runner_xpos = Environment.getRunner_Xpos(runner);
        int runner_ypos = Environment.getRunner_Ypos(runner);

        if(runner_ypos < map.get(1).length - 1){
            replace_image(grid, runner, map, image_alt_dict, runner_xpos, runner_ypos);

            runner_ypos += 1;

            grid.add(runner, runner_ypos, runner_xpos);
        }
        else{
            System.out.println("CANNOT MOVE RIGHTER THAN THIS");
        }
    }
}
