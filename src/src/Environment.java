import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;


public class Environment extends Application {
    // variable to keep track whether the user has enabled 8 directions
    boolean enable_8 = false;

    // check how many times the user has clicked the toggleDirection button
    int clicks = 0;

    // check how many times the undo button been clicked
    static int count_undos = 0;

    // list to store all positions [x, y] that the user has visited
    ArrayList<int[]> visited_cells = new ArrayList<>();

    // stack to keep track of the last recent moves
    Stack<int[]> recent_moves = new Stack<>();

    // stack to keep track of any recent undone moves
    Stack<int[]> redo_recent_moves = new Stack<>();

    // variable to keep track of the user's last action
    String recent_action = null;

    // variable to keep track of the user's score
    int score = 0;

    // keep track of the runner
    ImageView roadrunner = null;

    //keep track of the goal
    ImageView goal;

    // create a runner node to keep track of it
    Node runner = null;

    //track the goal node
    Node goal_node;

    //list of arrays to store the input file's contents
    ArrayList<int[]> environ_map;

    // array of array to store the grid nodes
    Node[][] grid_nodes;


    //method to read input files and return an arraylist of arrays. Each item in the arraylist is an array of integers representing the images in a row
    public static ArrayList<int[]> readfile(String filename) throws IOException {

        //read the first line of the file input, split it at every space and convert the resulting array from a String array to an array of integers
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        ArrayList<int[]> environmentMap = new ArrayList<>();

        //split the new line at a new space and convert into an array
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

    // funtion to find the runner's x position in the grid
    public static int getRunner_Xpos(ImageView runner){
        return GridPane.getRowIndex(runner);
    }

    // funtion to find the runner's y position in the grid
    public static int getRunner_Ypos(ImageView runner){
        return GridPane.getColumnIndex(runner);
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Road Runner Simulation");

        // read and store contents of the file in an arraylist of arrays
        environ_map = readfile("C:\\Users\\Kelvin Kinda\\IdeaProjects\\pp-ii-the-road-runner-kelvin-judith\\src\\src\\sample_test_input.txt");

        // create a hashmap of the original images
        HashMap<Integer,Image> image_dict = get_images();

        // create a hashmap of the alternate images
        HashMap<Integer,Image> image_alt_dict = get_alt_images();

        // initialize the grid nodes array and make it as big as the map
        grid_nodes = new Node[environ_map.size()][environ_map.get(1).length];


        //create a new grid and position it at the center of the window
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        //create ImageViews for each image, resize them then add them to the grid at the appropriate position
        for(int i=0; i < environ_map.size(); i++){
            for(int j=0; j < environ_map.get(i).length; j++){

                // check if the map value represents the start position. If so, put the runner at the start position
                if(environ_map.get(i)[j] == 8){
                    // initialize the runner imageview object
                    roadrunner = new ImageView(image_dict.get(7));
                    roadrunner.setFitHeight(100);
                    roadrunner.setFitWidth(100);

                    // initialize the runner node object
                    runner = new Node(roadrunner);

                    // add the runner to the grid nodes
                    grid_nodes[i][j] = runner;

                    // add the runner's start position to the visited cells list
                    visited_cells.add(new int[]{i, j});

                    // add the runner to the grid
                    grid.add(roadrunner, j, i);
                }

                //check if the map value reps the goal position
                else if(environ_map.get(i)[j] == 9){
                    // initialize the goal object
                    goal = new ImageView(image_dict.get(9));
                    goal.setFitWidth(100);
                    goal.setFitHeight(100);

                    // initialize the goal node object
                    goal_node = new Node(goal);

                    // add the goal to the grid of nodes
                    grid_nodes[i][j] = goal_node;

                    // add the goal to the grid
                    grid.add(goal, j, i);
                }

                else{
                    // create a new image view of the image using the image dict hashmap
                    ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                    image.setFitHeight(100);
                    image.setFitWidth(100);

                    // create a node object of the image
                    Node image_node = new Node(image);

                    // add the node object to the grid nodes
                    grid_nodes[i][j] = image_node;

                    // add the image to the grid
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

                // check if button has been clicked an odd number of times and change the enable_8 state to true and enable access to 8 directional movement
                if (clicks % 2 == 1){
                    enable_8 = true;
                    toggleDirection.setText("Disable 8 Direction");
                }
                // set the state to false and only allow 4 directional movement
                else{
                    enable_8 = false;
                    toggleDirection.setText("Enable 8 Direction");
                }
            }
        });

        // create buttons to undo, redo and reset
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button reset = new Button("Reset");

        // create a label to display the score
        Label score_label = new Label("SCORE: " + score);

        // define what happens when the undo button is clicked
        undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if the recent moves stack is not empty before undoing a move
                if(!recent_moves.isEmpty()) {
                    // store the coordinates of the undone move and store them in an array
                    int[] undone_move = undo_move(grid, recent_moves, roadrunner, image_dict, environ_map, visited_cells);

                    // add the undone move to the redo recent moves stack
                    redo_recent_moves.push(undone_move);

                    // store the recent action to the recent action variable
                    recent_action = "undo";

                    // increment the number of undos done so far
                    count_undos += 1;

                    //check if there has been 3 undos. If so disable the undo button and enable the undo button
                    if (count_undos == 3) {
                        undo.setDisable(true);
//                        redo.setDisable(false);
                        System.out.println("No Legal Undos left");
                    }

                    // undo the scores by reversing them.
                    switch (environ_map.get(undone_move[0])[undone_move[1]]) {
                        case 0:
                            score += 1;
                            break;
                        case 2:
                            score += 2;
                            break;
                        case 3:
                            score += 4;
                            break;
                        case 4:
                            score += 8;
                            break;
                        case 5:
                            score -= 1;
                            break;
                        case 6:
                            score -= 5;
                            break;
                    }
                    // update the score label with the new scores
                    score_label.setText("SCORE: " + score);

                }

                // if the recent moves stack is empty, disable the undo button
                if(recent_moves.isEmpty()){
                    System.out.println("Cannot Undo Further");
                    undo.setDisable(true);
                }

            }
        });

        // define what happens when the redo button is clicked
        redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            @SuppressWarnings("Duplicates")
            public void handle(ActionEvent event) {
                // array to store the recently redone move
                int[] redone = null;

                // check what was the recent action the user did.
                // if the action was an undo, then redo the undone move
                if (recent_action.equals("undo")) {

                    // check that the redo recent moves stack is not empty
                    if (!redo_recent_moves.isEmpty()) {
                        // store the redone move in an array
                        redone = redo_move(grid, redo_recent_moves, roadrunner, image_alt_dict, environ_map, visited_cells);

                        // add it to the recent moves stack
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));

                        // enable the undo button
                        undo.setDisable(false);

                        // check if redo recent moves is empty. if so disable the redo button
                        if (redo_recent_moves.isEmpty()) {
                            redo.setDisable(true);
                        }
                    }
                    // if empty, display a warning
                    else {
                        System.out.println("Cannot Redo");
                    }

                }
                // if the last action was move up, redo the move up action
                else if (recent_action.equals("up")) {
                    undo.setDisable(false);
                    redone = Runner_Movement.moveUp(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("down")) {
                    undo.setDisable(false);
                    redone = Runner_Movement.moveDown(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("left")) {
                    undo.setDisable(false);
                    redone = Runner_Movement.moveLeft(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("right")) {
                    undo.setDisable(false);
                    redone = Runner_Movement.moveRight(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }
                else if(recent_action.equals("north east")){
                    undo.setDisable(false);
                    redone = Runner_Movement.moveNorthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("south east")){
                    undo.setDisable(false);
                    redone = Runner_Movement.moveSouthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("north west")){
                    undo.setDisable(false);
                    redone = Runner_Movement.moveNorthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("south west")){
                    undo.setDisable(false);
                    redone = Runner_Movement.moveSouthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                // check to ensure the redone move is not null before updating the scores
                if(redone != null){
                    switch (environ_map.get(redone[0])[redone[1]]) {
                        case 0:
                            score -= 1;
                            break;
                        case 2:
                            score -= 2;
                            break;
                        case 3:
                            score -= 4;
                            break;
                        case 4:
                            score -= 8;
                            break;
                        case 5:
                            score += 1;
                            break;
                        case 6:
                            score += 5;
                            break;
                    }
                }

                score_label.setText("SCORE: " + score);
            }
        });

        // define what happens when the reset button is clicked
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // reset the member variables of the environment
                roadrunner = null;
                enable_8 = false;
                count_undos = 0;
                visited_cells = new ArrayList<>();
                recent_moves = new Stack<>();
                redo_recent_moves = new Stack<>();
                recent_action = null;
                score = 0;
                grid.getChildren().removeAll();
                grid_nodes = new Node[environ_map.size()][environ_map.get(1).length];

                score_label.setText("Score: " + score);

                // repopulate the grid again and update the runner, goal and other images
                for(int i=0; i < environ_map.size(); i++){
                    for(int j=0; j < environ_map.get(i).length; j++){
                        if(environ_map.get(i)[j] == 8){
                            roadrunner = new ImageView(image_dict.get(7));
                            roadrunner.setFitHeight(100);
                            roadrunner.setFitWidth(100);
                            visited_cells.add(new int[]{i, j});
                            grid.add(roadrunner, j, i);
                        }
                        else if(environ_map.get(i)[j] == 9){
                            goal = new ImageView(image_dict.get(9));
                            goal.setFitWidth(100);
                            goal.setFitHeight(100);
                            goal_node = new Node(goal);
                            grid_nodes[i][j] = goal_node;
                            grid.add(goal, j, i);
                        }
                        else{
                            ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                            image.setFitHeight(100);
                            image.setFitWidth(100);
                            grid.add(image, j, i);
                        }
                    }
                }



            }
        });

        //create a borderpane
        BorderPane borderPane = new BorderPane();
//        TilePane tile = new TilePane();
//
//        tile.getChildren().addAll(grid, toggleDirection);
        borderPane.setCenter(grid);
//        borderPane.setBottom(reset);
        borderPane.setBottom(toggleDirection);
        borderPane.setLeft(undo);
        borderPane.setRight(redo);
        borderPane.setTop(score_label);


        //create a scene and add it to the stage
        Scene scene = new Scene(borderPane, 800, 700);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            @SuppressWarnings("Duplicates")
            public void handle(KeyEvent event) {
                // array to store the new position of the runner
                int[] new_pos = null;

                // if UP key is pressed
                if(event.getCode() == KeyCode.UP){
                    // if the user has already undone some moves, reduce the possible number of undos they can make by one
                    if(count_undos > 0){
                        count_undos -= 1;
                    }
                    // enable the undo button
                    undo.setDisable(false);

                    // store the runner's new position in an array
                    new_pos = Runner_Movement.moveUp(grid, roadrunner, image_alt_dict, environ_map, visited_cells);

                    // if the new position is not null, i.e the runner moved despite the restrictions set in the upward movement
                    if(new_pos != null){
                        // add his last position to the recent moves
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));

                        // add his new position in the visited cells
                        visited_cells.add(new_pos);

                        // update the user's last action to be move up
                        recent_action = "up";
                   }
                    A_Star.A_StarSearch_4D(grid_nodes, runner, goal_node, image_dict);
//                    A_Star.A_StarSearch_8D(grid_nodes, runner, goal_node, image_dict);

//                    Dijkstras.Dijkstras_Search_4D(grid_nodes, runner, goal_node, image_dict);
                }
                // check if key pressed is DOWN and move runner down. update the recent moves stack, the visited cells list and recent action
                else if(event.getCode() == KeyCode.DOWN){
                    if(count_undos > 0){
                        count_undos -= 1;
                    }
                    undo.setDisable(false);
                    new_pos = Runner_Movement.moveDown(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if(new_pos != null){
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(new_pos);
                        recent_action = "down";
                    }
                }
                // check if key pressed is LEFT and move runner left. update the recent moves stack, the visited cells list and recent action
                else if(event.getCode() == KeyCode.LEFT){
                    if(count_undos > 0){
                        count_undos -= 1;
                    }
                    undo.setDisable(false);
                    new_pos = Runner_Movement.moveLeft(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if(new_pos != null){
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(new_pos);
                        recent_action = "left";
                    }
                }
                // check if key pressed is RIGHT and move runner right. update the recent moves stack, the visited cells list and recent action
                else if(event.getCode() == KeyCode.RIGHT){
                    if(count_undos > 0){
                        count_undos -= 1;
                    }
                    undo.setDisable(false);
                    new_pos = Runner_Movement.moveRight(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                    if(new_pos != null){
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(new_pos);
                        recent_action = "right";
                    }
                }

                // check if the user has enabled 8 directional movement, if so check which diagonal direction the user wants to move
                if(enable_8){
                    // check if key pressed is W and move runner north east. update the recent moves stack, the visited cells list and recent action
                    if(event.getCode() == KeyCode.W){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveNorthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                        if(new_pos != null){
                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "north east";
                        }
                    }
                    // check if key pressed is S and move runner south east. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.S){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveSouthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                        if(new_pos != null){
                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "south east";
                        }
                    }
                    // check if key pressed is Q and move runner north west. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.Q){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveNorthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                        if(new_pos != null){
                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "north west";
                        }
                    }
                    // check if key pressed is A and move runner south west. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.A){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveSouthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells);
                        if(new_pos != null){
                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "south west";
                        }
                    }
                }

                // check if the new position is not null, then calculate the score
                if(new_pos != null){
                    switch(environ_map.get(new_pos[0])[new_pos[1]]){
                        case 0:
                            score -= 1;
                            break;
                        case 2:
                            score -= 2;
                            break;
                        case 3:
                            score -= 4;
                            break;
                        case 4:
                            score -= 8;
                            break;
                        case 5:
                            score += 1;
                            break;
                        case 6:
                            score += 5;
                            break;
                    }
                    score_label.setText("SCORE: " + score);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
}

    @SuppressWarnings("Duplicates")
    // function to undo the user's move
    public static int[] undo_move(GridPane grid, Stack<int[]> moves, ImageView runner, HashMap<Integer,Image> image_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells){
        // pop the recent moves stack to get the new position to move the runner
        int[] prev_pos = moves.pop();
        int x = prev_pos[0];
        int y = prev_pos[1];

        // get the runner's current position on the grid
        int runner_xpos = getRunner_Xpos(runner);
        int runner_ypos = getRunner_Ypos(runner);

        // get the key of the image where the is currently at
        int prev_image_key = map.get(runner_xpos)[runner_ypos];
        ImageView newView = new ImageView(image_dict.get(prev_image_key));
        newView.setFitWidth(100);
        newView.setFitHeight(100);

        // update the image at the current runner's position
        grid.add(newView, runner_ypos, runner_xpos);

        // remove the runner from the grid
        grid.getChildren().remove(runner);

        // readd the runner at the new undone position
        grid.add(runner, y, x);

        // return the undone move
        return visited_cells.remove(visited_cells.size() - 1);
    }

    @SuppressWarnings("Duplicates")
    // function to redo the user's move
    public static int[] redo_move(GridPane grid, Stack<int[]> redo_moves, ImageView runner, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> map, ArrayList<int[]> visited_cells){
        // pop the redo moves stack to get the new position to move the runner
        int[] redo_pos = redo_moves.pop();
        int x = redo_pos[0];
        int y = redo_pos[1];

        // get the runner's current position on the grid
        int runner_xpos = getRunner_Xpos(runner);
        int runner_ypos = getRunner_Ypos(runner);

        // get the key of the image where the is currently at
        int next_image_key = map.get(runner_xpos)[runner_ypos];
        ImageView imageView = new ImageView(image_alt_dict.get(next_image_key));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        // update the image at the current runner's position
        grid.add(imageView, runner_ypos, runner_xpos);

        // delete the runner from the grid
        grid.getChildren().remove(runner);

        // readd the runner to the grid
        grid.add(runner, y, x);

        // update the visited cells list witht the redone move
        visited_cells.add(redo_pos);

        // return the redone move
        return redo_pos;
    }
}
