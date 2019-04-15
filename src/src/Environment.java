import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Environment extends Application {
    boolean enable_8 = false;
    int clicks = 0;
    ArrayList<int[]> visited_cells = new ArrayList<>();

    //method to read input files and return an arraylist of arrays. Each item in the arraylist is an array of integers representing the images in a row
    public static ArrayList<int[]> readfile(String filename) throws IOException {

        //read the first line of the file input, split it at every space and convert the resulting array from a String array to an array of integers
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        ArrayList<int[]> environmentMap = new ArrayList<>();

        String[] line1 = bufferedReader.readLine().split(" ");

        //convert the String array to an array of inegers
        int[] matrix = Arrays.stream(line1).mapToInt(Integer::parseInt).toArray();

        //store the number of rows and columns in the file in variables
        int no_rows = matrix[0];
        int no_columns = matrix[1];

        //read the remaining part of the file input, split it at every space and convert the resulting array from a String array to an array of integers
        while((line = bufferedReader.readLine()) != null){
            String[] temp = line.split("");

            //convert the String array to an array of inegers
            int[] arr = Arrays.stream(temp).mapToInt(Integer::parseInt).toArray();

            //add the array to the arraylist
            environmentMap.add(arr);
        }
        return environmentMap;
    }

    //method to return an hashmap of integers mapping to image objects
    public static HashMap<Integer,Image> get_images() throws FileNotFoundException {
        //read image files and create image objects
        Image boulder = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/boulder.jpg"));
        Image coyote = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/coyote.jpg"));
        Image explosive = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/explosive.jpg"));
        Image goal = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/goal.jpg"));
        Image gold = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/gold.jpg"));
        Image pothole = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/pothole.jpg"));
        Image road = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/road.jpg"));
        Image road_runner = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/road_runner.jpg"));
        Image start = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/start.jpg"));
        Image tarred = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/tarred.jpg"));


        //create a hashmap of integers mapping to Images
        HashMap<Integer,Image> image_dict = new HashMap<>();

        //add the created image objects to the hashmap with their integer keys
        image_dict.put(0, road);
        image_dict.put(1, boulder);
        image_dict.put(2, pothole);
        image_dict.put(3, explosive);
        image_dict.put(4, coyote);
        image_dict.put(5, tarred);
        image_dict.put(6, gold);
        image_dict.put(8, start);
        image_dict.put(9, goal);
        image_dict.put(7, road_runner);


        return image_dict;
    }

    //method to return a hashmap of integers mapping to the alternative images
    public static HashMap <Integer,Image> get_alt_images() throws FileNotFoundException {
        //read images files and create their image objects
        Image coyote_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/coyote_alt.jpg"));
        Image explosive_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/explosive_alt.jpg"));
        Image gold_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/gold_alt.jpg"));
        Image pothole_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/pothole_alt.jpg"));
        Image road_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/road_alt.jpg"));
        Image tarred_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/tarred_alt.jpg"));
        Image boulder_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/boulder.jpg"));
        Image start_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/start.jpg"));
        Image goal_alt = new Image(new FileInputStream("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Image Files/goal.jpg"));

        //create a hashmap of integers mapping to Images
        HashMap <Integer,Image> images_alt_dict = new HashMap<>();

        //add the created image objects to the hashmap with their integer keys. Keys are similar to their original image keys
        images_alt_dict.put(4, coyote_alt);
        images_alt_dict.put(3, explosive_alt);
        images_alt_dict.put(6, gold_alt);
        images_alt_dict.put(2, pothole_alt);
        images_alt_dict.put(0, road_alt);
        images_alt_dict.put(5, tarred_alt);
        images_alt_dict.put(1, boulder_alt);
        images_alt_dict.put(8, start_alt);
        images_alt_dict.put(9, goal_alt);


        return images_alt_dict;
    }

    public static int getRunner_Xpos(ImageView runner){
        return GridPane.getRowIndex(runner);
    }

    public static int getRunner_Ypos(ImageView runner){
        return GridPane.getColumnIndex(runner);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Road Runner Simulation");

        ArrayList<int[]> environ_map = readfile("C:/Users/Kelvin Kinda/IdeaProjects/pp-ii-the-road-runner-kelvin-judith/src/src/The Road Runner Files/Test Inputs/sample_test_input_1.txt");
        HashMap<Integer,Image> image_dict = get_images();
        HashMap<Integer,Image> image_alt_dict = get_alt_images();
        ArrayList<int[]> visited_cells = new ArrayList<>();


        //create a new grid and position it at the center of the window
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        ImageView roadrunner = null;

        //create ImageViews for each image, resize them then add them to the grid at the appropriate position
        for(int i=0; i < environ_map.size(); i++){
            for(int j=0; j < environ_map.get(i).length; j++){
                if(environ_map.get(i)[j] == 8){
                    roadrunner = new ImageView(image_dict.get(7));
                    roadrunner.setFitHeight(100);
                    roadrunner.setFitWidth(100);
                    visited_cells.add(new int[]{i, j});
                    grid.add(roadrunner, j, i);
                }
                else{
                    ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                    image.setFitHeight(100);
                    image.setFitWidth(100);
                    grid.add(image, j, i);
                }
            }
        }

        //create Enable 8 Direction button
        Button toggleDirection = new Button();
        toggleDirection.setText("Enable 8 Direction");


        toggleDirection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clicks += 1;

                if (clicks % 2 == 1){
                    enable_8 = true;
                    toggleDirection.setText("Disable 8 Direction");
                }
                else{
                    enable_8 = false;
                    toggleDirection.setText("Enable 8 Direction");
                }
            }
        });


        //create a borderpane
        BorderPane borderPane = new BorderPane();
//        TilePane tile = new TilePane();
//
//        tile.getChildren().addAll(grid, toggleDirection);
        borderPane.setCenter(grid);
        borderPane.setBottom(toggleDirection);


        //create a scene and add it to the stage
        Scene scene = new Scene(borderPane, 800, 700);
        ImageView finalRoadrunner = roadrunner;

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode() == KeyCode.UP){
                   Runner_Movement.moveUp(grid, finalRoadrunner, image_alt_dict, environ_map, visited_cells);
                }
                else if(event.getCode() == KeyCode.DOWN){
                    Runner_Movement.moveDown(grid, finalRoadrunner, image_alt_dict, environ_map, visited_cells);
                }
                else if(event.getCode() == KeyCode.LEFT){
                    Runner_Movement.moveLeft(grid, finalRoadrunner, image_alt_dict, environ_map);
                }
                else if(event.getCode() == KeyCode.RIGHT){
                    Runner_Movement.moveRight(grid, finalRoadrunner, image_alt_dict, environ_map);
                }

                if(enable_8){
                    if(event.getCode() == KeyCode.W){
                        Runner_Movement.moveNorthEast(grid, finalRoadrunner, image_alt_dict, environ_map);
                    }
                    else if(event.getCode() == KeyCode.S){
                        Runner_Movement.moveSouthEast(grid, finalRoadrunner, image_alt_dict, environ_map);
                    }
                    else if(event.getCode() == KeyCode.Q){
                        Runner_Movement.moveNorthWest(grid, finalRoadrunner, image_alt_dict, environ_map);
                    }
                    else if(event.getCode() == KeyCode.A){
                        Runner_Movement.moveSouthWest(grid, finalRoadrunner, image_alt_dict, environ_map);
                    }
                }
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
}
}
