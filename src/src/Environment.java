import com.sun.scenario.animation.shared.AnimationAccessor;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

// environment
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
    Cell_Node runner = null;

    //track the goal node
    Cell_Node goal_node;

    //list of arrays to store the input file's contents
    ArrayList<int[]> environ_map;

    // array of array to store the grid nodes
    Cell_Node[][] grid_nodes;

    ArrayList<String> path = null;

    int count = 0;

    int[] clicked_pos = null;

    boolean start_isClicked = false;

    boolean goal_isReached = false;


    /**
        Time Complexity: O(N)
        Space Complexity: O(N)
        Auxiliary Space: O(N)
     */

    //method to read input files and return an arraylist of arrays. Each item in the arraylist is an array of integers representing the images in a row
    public static ArrayList<int[]> readfile(File filename) throws IOException {

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

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
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

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
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

    /**
     Time Complexity: O(1)
     Space Complexity: O(1)
     Auxiliary Space: O(1)
     */
    // funtion to find the runner's x position in the grid
    public static int getRunner_Xpos(ImageView runner){
        return GridPane.getRowIndex(runner);
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(1)
     Auxiliary Space: O(1)
     */
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

        File file_name = new File("C:\\Users\\Kelvin Kinda\\IdeaProjects\\pp-ii-the-road-runner-kelvin-judith\\src\\src\\Test Files\\test_input_10_15_unsolvable.txt");

        // read and store contents of the file in an arraylist of arrays
        environ_map = readfile(file_name);

        // create a hashmap of the original images
        HashMap<Integer,Image> image_dict = get_images();

        // create a hashmap of the alternate images
        HashMap<Integer,Image> image_alt_dict = get_alt_images();

        // initialize the grid nodes array and make it as big as the map
        grid_nodes = new Cell_Node[environ_map.size()][environ_map.get(1).length];

        int image_width = 20;
        int image_height = 20;


        HashMap<Integer,Integer> weights_dict = new HashMap<>();
        weights_dict.put(0, -1);
        weights_dict.put(2, -50);
        weights_dict.put(3, -4);
        weights_dict.put(4, -8);
        weights_dict.put(5, 1);
        weights_dict.put(6, 5);


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
                    roadrunner.setFitHeight(image_height);
                    roadrunner.setFitWidth(image_width);

                    // initialize the runner node object
                    runner = new Cell_Node(roadrunner);

                    runner.posOnGrid = new int[]{i, j};

                    // add the runner to the grid nodes
                    grid_nodes[i][j] = runner;

                    // add the runner's start position to the visited cells list
                    visited_cells.add(new int[]{i, j});

                    // add the runner to the grid
                    grid.add(roadrunner, j, i);

                    int finalI = i;
                    int finalJ = j;
                    roadrunner.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(start_isClicked){
                                clicked_pos = new int[]{finalI, finalJ};

                                System.out.println("RoadRunner is already here");
                            }

                        }
                    });

                }

                //check if the map value reps the goal position
                else if(environ_map.get(i)[j] == 9){
                    // initialize the goal object
                    goal = new ImageView(image_dict.get(9));
                    goal.setFitWidth(image_width);
                    goal.setFitHeight(image_height);

                    // initialize the goal node object
                    goal_node = new Cell_Node(goal);

                    goal_node.posOnGrid = new int[]{i,j};

                    // add the goal to the grid of nodes
                    grid_nodes[i][j] = goal_node;


                    // add the goal to the grid
                    grid.add(goal, j, i);

                    int finalI = i;
                    int finalJ = j;

                    goal.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(start_isClicked){
                                clicked_pos = new int[]{finalI, finalJ};

                                System.out.println("Cannot Set the Start at the Goal");
                            }
                        }
                    });
                }

                else{
                    // create a new image view of the image using the image dict hashmap
                    ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                    image.setFitHeight(image_height);
                    image.setFitWidth(image_width);

                    // create a node object of the image
                    Cell_Node image_node = new Cell_Node(image);

                    // add the node object to the grid nodes
                    grid_nodes[i][j] = image_node;

                    // add the image to the grid
                    grid.add(image, j, i);

                    int finalI = i;
                    int finalJ = j;

                    image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if(start_isClicked){
                                clicked_pos = new int[]{finalI, finalJ};

                                int x = getRunner_Xpos(roadrunner);
                                int y = getRunner_Ypos(roadrunner);
                                Runner_Movement.replace_image(grid, roadrunner, environ_map, image_dict, x, y, grid_nodes);
                                grid.add(roadrunner, clicked_pos[1], clicked_pos[0]);

                                for(int i=0; i < visited_cells.size(); i++){
                                    if(visited_cells.get(i)[0] == x && visited_cells.get(i)[1] == y){
                                        visited_cells.remove(visited_cells.get(i));
                                    }
                                }
                                visited_cells.add(clicked_pos);
                            }

                            grid_nodes[finalI][finalJ] = runner;
                        }
                    });
                }
            }
        }

        //create Enable 8 Direction button
        Button toggleDirection = new Button();
        toggleDirection.setText("Enable 8 Direction");

        // set actions when the toggleDiection button is clicked
        toggleDirection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            /**
             Time Complexity: O(1)
             Space Complexity: O(1)
             Auxiliary Space: O(1)
             */
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
            /**
             Time Complexity: O(1)
             Space Complexity: O(1)
             Auxiliary Space: O(1)
             */
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
                        redo.setDisable(false);
                        System.out.println("No Legal Undos left");
                    }

                    // undo the scores by reversing them.
                    switch (environ_map.get(undone_move[0])[undone_move[1]]) {
                        case 0:
                            score -= weights_dict.get(0);
                            break;
                        case 2:
                            score -= weights_dict.get(2);
                            break;
                        case 3:
                            score -= weights_dict.get(3);
                            break;
                        case 4:
                            score -= weights_dict.get(4);
                            break;
                        case 5:
                            score -= weights_dict.get(5);
                            break;
                        case 6:
                            score -= weights_dict.get(6);
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
            /**
             Time Complexity: O(1)
             Space Complexity: O(1)
             Auxiliary Space: O(1)
             */
            @SuppressWarnings("Duplicates")
            public void handle(ActionEvent event) {
                // array to store the recently redone move
                int[] redone = null;

                // check what was the recent action the user did.
                // if the action was an undo, then redo the undone move
                if (recent_action.equals("undo")) {

                    // check that the redo recent moves stack is not empty
                    if (!redo_recent_moves.isEmpty()) {
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        // enable the undo button
                        undo.setDisable(false);

                        // store the redone move in an array
                        redone = redo_move(grid, redo_recent_moves, roadrunner, image_alt_dict, environ_map, visited_cells);

                        // add it to the recent moves stack
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));

                        visited_cells.add(redone);

                        // enable the undo button
//                        undo.setDisable(false);


                        // check if redo recent moves is empty. if so disable the redo button
                        if (redo_recent_moves.isEmpty()) {
                            redo.setDisable(false);
                        }
                    }

                    // if empty, display a warning
                    else {
                        redo.setDisable(true);
                        System.out.println("Cannot Redo");
                    }

                }
                // if the last action was move up, redo the move up action
                else if (recent_action.equals("up")) {
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveUp(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("down")) {
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveDown(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("left")) {
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveLeft(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                } else if (recent_action.equals("right")) {
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveRight(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }
                else if(recent_action.equals("north east")){
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveNorthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("south east")){
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveSouthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("north west")){
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveNorthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                else if(recent_action.equals("south west")){
                    undo.setDisable(false);
                    redo.setDisable(false);
                    redone = Runner_Movement.moveSouthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                    if (redone != null) {
                        recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                        visited_cells.add(redone);
                    }
                }

                score = get_score(score, redone, environ_map, weights_dict);
                score_label.setText("SCORE: " + score);
            }
        });

        Timeline dijkstraTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            score = move_with_algorithm(grid, roadrunner,goal, image_alt_dict, environ_map, visited_cells, recent_moves, path, grid_nodes, score, count, weights_dict);
            count += 1;
            score_label.setText("SCORE: " + score);
        })
        );

        Timeline astarTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            score = move_with_algorithm(grid, roadrunner, goal, image_alt_dict, environ_map, visited_cells, recent_moves, path, grid_nodes, score, count, weights_dict);
            count += 1;
            score_label.setText("SCORE: " + score);
        })
        );

        Timeline dfsTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            score = move_with_algorithm(grid, roadrunner, goal, image_alt_dict, environ_map, visited_cells, recent_moves, path, grid_nodes, score, count, weights_dict);
            count += 1;
            score_label.setText("SCORE: " + score);

        })
        );


        // define what happens when the reset button is clicked
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            /**
             Time Complexity: O(N)
             Space Complexity: O(N)
             Auxiliary Space: O(N)
             */
            public void handle(ActionEvent event) {
                // reset the member variables of the environment
                clicks = 0;
                enable_8 = false;
                count_undos = 0;
                visited_cells = new ArrayList<>();
                recent_moves = new Stack<>();
                redo_recent_moves = new Stack<>();
                recent_action = null;
                score = 0;
                count = 0;
                clicked_pos = null;
                start_isClicked = false;
                path = null;

                dfsTimeline.stop();
                astarTimeline.stop();
                dijkstraTimeline.stop();


                // delete all images from the grid before adding new Imageviews
                grid.getChildren().removeAll();

                // update the grid nodes with nodes from the new image objects created
                grid_nodes = new Cell_Node[environ_map.size()][environ_map.get(1).length];
//                runner = null;

                score_label.setText("Score: " + score);
                toggleDirection.setText("Enable 8 Direction");

                // repopulate the grid again and update the runner, goal and other images
                for(int i=0; i < environ_map.size(); i++){
                    for(int j=0; j < environ_map.get(i).length; j++){
                        if(environ_map.get(i)[j] == 8){
                            roadrunner = new ImageView(image_dict.get(7));
                            roadrunner.setFitHeight(image_height);
                            roadrunner.setFitWidth(image_width);
                            runner = new Cell_Node(roadrunner);
                            visited_cells.add(new int[]{i, j});
                            grid.add(roadrunner, j, i);
                            grid_nodes[i][j] = runner;

                            int finalI = i;
                            int finalJ = j;

                            roadrunner.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if(start_isClicked){
                                        clicked_pos = new int[]{finalI, finalJ};

                                        System.out.println("RoadRunner is already here");
                                    }

                                }
                            });
                        }
                        else if(environ_map.get(i)[j] == 9){
                            goal = new ImageView(image_dict.get(9));
                            goal.setFitWidth(image_width);
                            goal.setFitHeight(image_height);
                            goal_node = new Cell_Node(goal);
                            grid.add(goal, j, i);
                            grid_nodes[i][j] = goal_node;

                            int finalI = i;
                            int finalJ = j;

                            goal.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if(start_isClicked){
                                        clicked_pos = new int[]{finalI, finalJ};

                                        System.out.println("Cannot Set the Start at the Goal");
                                    }
                                }
                            });
                        }
                        else{
                            ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                            image.setFitHeight(image_height);
                            image.setFitWidth(image_width);
                            Cell_Node image_node = new Cell_Node(image);
                            grid.add(image, j, i);
                            grid_nodes[i][j] = image_node;

                            int finalI = i;
                            int finalJ = j;

                            image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if(start_isClicked){
                                        clicked_pos = new int[]{finalI, finalJ};

                                        int x = getRunner_Xpos(roadrunner);
                                        int y = getRunner_Ypos(roadrunner);
                                        Runner_Movement.replace_image(grid, roadrunner, environ_map, image_dict, x, y, grid_nodes);
                                        grid.add(roadrunner, clicked_pos[1], clicked_pos[0]);

                                        for(int i=0; i < visited_cells.size(); i++){
                                            if(visited_cells.get(i)[0] == x && visited_cells.get(i)[1] == y){
                                                visited_cells.remove(visited_cells.get(i));
                                            }
                                        }
                                        visited_cells.add(clicked_pos);
                                    }

                                    grid_nodes[finalI][finalJ] = runner;
                                }
                            });
                        }
                    }
                }



            }
        });

        // create buttons to solve the runners path using A*, Dijkstra and DFS algorithms
        Button solve_A_star = new Button("Solve with A*");
        Button solve_dijkstra = new Button("Solve with Dijkstra");
        Button solve_dfs = new Button("Solve with DFS");






        solve_A_star.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings("Duplicates")
            @Override
            /**
             Time Complexity: O(N)
             Space Complexity: O(N)
             Auxiliary Space: O(1)
             */
            public void handle(ActionEvent event) {
//                ArrayList<String> path = null;
                if (enable_8) {
                    try {
                        path = A_Star.A_StarSearch_8D(grid_nodes, runner, goal_node, image_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        path = A_Star.A_StarSearch_4D(grid_nodes, runner, goal_node, image_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(path != null){
                    astarTimeline.setCycleCount(path.size());
                    astarTimeline.play();
                }

                count = 0;

          }

        });




        solve_dijkstra.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings("Duplicates")
            @Override
            /**
             Time Complexity: O(N)
             Space Complexity: O(N)
             Auxiliary Space: O(1)
             */
            public void handle(ActionEvent event) {
                if (enable_8) {
                    try {
                        path = Dijkstras.Dijkstras_Search_8D(grid_nodes, runner, goal_node, image_dict, weights_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        path = Dijkstras.Dijkstras_Search_4D(grid_nodes, runner, goal_node, image_dict, weights_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(path != null){
                    dijkstraTimeline.setCycleCount(path.size());
                    dijkstraTimeline.play();
                }

                count = 0;
            }

        });




        solve_dfs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(enable_8){
                    try {
                        path = Depth_First_Search.Depth_First_Search_8D(grid_nodes, runner, goal_node, image_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        path = Depth_First_Search.Depth_First_Search_4D(grid_nodes, runner, goal_node, image_dict);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(path != null){
                    dfsTimeline.setCycleCount(path.size());
                    dfsTimeline.play();
                }

                count = 0;
            }
        });

        // create buttons to manipulate the GUI
        // button to set a new start position by clicking on a grid position
        Button set_new_start =  new Button("Set New Start");

        // button to load a new map to the grid
        Button load_map = new Button("Load Map");

        // button to change the weights of the nodes in the grid
        Button change_weights = new Button("Change Weights");


        set_new_start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            /**
             Time Complexity: O(1)
             Space Complexity: O(1)
             Auxiliary Space: O(1)
             */
            public void handle(ActionEvent event) {
                start_isClicked = true;
            }
        });


        load_map.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            /**
             Time Complexity: O(N)
             Space Complexity: O(N)
             Auxiliary Space: O(N)
             */
            public void handle(ActionEvent event) {
//                System.out.println("Kindly input the path to the new map below");
//                Scanner scanner = new Scanner(System.in);
//
//                String file_path = scanner.nextLine();

                grid.getChildren().removeAll();

                FileChooser fileChooser = new FileChooser();

                File load_file = fileChooser.showOpenDialog(primaryStage);

                environ_map = null;
                grid_nodes = null;

                if(load_file != null){
                    try {
                        environ_map = readfile(load_file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    grid_nodes = new Cell_Node[environ_map.size()][environ_map.get(1).length];
                    enable_8 = false;
                    count_undos = 0;
                    visited_cells = new ArrayList<>();
                    recent_moves = new Stack<>();
                    redo_recent_moves = new Stack<>();
                    recent_action = null;
                    score = 0;




                    for(int i=0; i < environ_map.size(); i++){
                        for(int j=0; j < environ_map.get(i).length; j++){

                            // check if the map value represents the start position. If so, put the runner at the start position
                            if(environ_map.get(i)[j] == 8){
                                // initialize the runner imageview object
                                roadrunner = new ImageView(image_dict.get(7));
                                roadrunner.setFitHeight(image_height);
                                roadrunner.setFitWidth(image_width);

                                // initialize the runner node object
                                runner = new Cell_Node(roadrunner);

                                // add the runner to the grid nodes
                                grid_nodes[i][j] = runner;

                                // add the runner's start position to the visited cells list
                                visited_cells.add(new int[]{i, j});

                                // add the runner to the grid
                                grid.add(roadrunner, j, i);

                                int finalI = i;
                                int finalJ = j;

                                roadrunner.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if(start_isClicked){
                                            clicked_pos = new int[]{finalI, finalJ};

                                            System.out.println("RoadRunner is already here");
                                        }

                                    }
                                });
                            }

                            //check if the map value reps the goal position
                            else if(environ_map.get(i)[j] == 9){
                                // initialize the goal object
                                goal = new ImageView(image_dict.get(9));
                                goal.setFitWidth(image_width);
                                goal.setFitHeight(image_height);

                                // initialize the goal node object
                                goal_node = new Cell_Node(goal);

                                // add the goal to the grid of nodes
                                grid_nodes[i][j] = goal_node;

                                // add the goal to the grid
                                grid.add(goal, j, i);

                                int finalI = i;
                                int finalJ = j;

                                goal.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if(start_isClicked){
                                            clicked_pos = new int[]{finalI, finalJ};

                                            System.out.println("Cannot Set the Start at the Goal");
                                        }
                                    }
                                });
                            }

                            else{
                                // create a new image view of the image using the image dict hashmap
                                ImageView image = new ImageView(image_dict.get(environ_map.get(i)[j]));
                                image.setFitHeight(image_height);
                                image.setFitWidth(image_width);

                                // create a node object of the image
                                Cell_Node image_node = new Cell_Node(image);

                                // add the node object to the grid nodes
                                grid_nodes[i][j] = image_node;

                                // add the image to the grid
                                grid.add(image, j, i);

                                int finalI = i;
                                int finalJ = j;

                                image.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        if(start_isClicked){
                                            clicked_pos = new int[]{finalI, finalJ};

                                            int x = getRunner_Xpos(roadrunner);
                                            int y = getRunner_Ypos(roadrunner);
                                            Runner_Movement.replace_image(grid, roadrunner, environ_map, image_dict, x, y, grid_nodes);
                                            grid.add(roadrunner, clicked_pos[1], clicked_pos[0]);

                                            for(int i=0; i < visited_cells.size(); i++){
                                                if(visited_cells.get(i)[0] == x && visited_cells.get(i)[1] == y){
                                                    visited_cells.remove(visited_cells.get(i));
                                                }
                                            }
                                            visited_cells.add(clicked_pos);
                                        }

                                        grid_nodes[finalI][finalJ] = runner;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        VBox weight_area = new VBox();
        weight_area.setAlignment(Pos.CENTER_LEFT);
        weight_area.setSpacing(2);


        change_weights.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Label road_label = new Label("Road: ");
                TextField road_weight_text = new TextField();

                Label pothole_label = new Label("Pothole: ");
                TextField pothole_weight_text = new TextField();

                Label explosive_label = new Label("Explosive: ");
                TextField explosive_weight_text = new TextField();

                Label coyote_label = new Label("Coyote: ");
                TextField coyote_weight_text = new TextField();

                Label tarred_label = new Label("Tarred: ");
                TextField tarred_weight_text = new TextField();

                Label gold_label = new Label("Gold: ");
                TextField gold_weight_text = new TextField();

                Button submit = new Button("Update Weights");

                weight_area.getChildren().addAll(road_label, road_weight_text, pothole_label, pothole_weight_text, explosive_label, explosive_weight_text, coyote_label, coyote_weight_text, tarred_label, tarred_weight_text, gold_label, gold_weight_text, submit);

                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(!road_weight_text.getText().equals("")){
                            weights_dict.put(0, Integer.parseInt(road_weight_text.getText()));
                        }

                        if(!pothole_weight_text.getText().equals("")){
                            weights_dict.put(2, Integer.parseInt(pothole_weight_text.getText()));
                        }

                        if(!explosive_weight_text.getText().equals("")){
                            weights_dict.put(3, Integer.parseInt(explosive_weight_text.getText()));

                        }

                        if(!coyote_weight_text.getText().equals("")){
                            weights_dict.put(4, Integer.parseInt(coyote_weight_text.getText()));

                        }

                        if(!tarred_weight_text.getText().equals("")){
                            weights_dict.put(5, Integer.parseInt(tarred_weight_text.getText()));

                        }

                        if(!gold_weight_text.getText().equals("")){
                            weights_dict.put(6, Integer.parseInt(gold_weight_text.getText()));

                        }

                        System.out.println(weights_dict);

                        weight_area.getChildren().removeAll(road_label, road_weight_text, pothole_label, pothole_weight_text, explosive_label, explosive_weight_text, coyote_label, coyote_weight_text, tarred_label, tarred_weight_text, gold_label, gold_weight_text, submit);
                    }
                });
            }
        });


//         Organize all the items on the display window (scene)
        score_label.setAlignment(Pos.TOP_LEFT);

        HBox hBox_top = new HBox();
        hBox_top.setAlignment(Pos.TOP_CENTER);
        hBox_top.setSpacing(25);
        hBox_top.getChildren().addAll(undo, redo, reset, toggleDirection);

        HBox hBox_bottom = new HBox();
        hBox_bottom.setAlignment(Pos.BOTTOM_CENTER);
        hBox_bottom.setSpacing(25);
        hBox_bottom.getChildren().addAll(solve_A_star, solve_dijkstra, solve_dfs);

        HBox hBox_bottom2 = new HBox();
        hBox_bottom2.setAlignment(Pos.BOTTOM_CENTER);
        hBox_bottom2.setSpacing(25);
        hBox_bottom2.getChildren().addAll(set_new_start, load_map, change_weights);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        vBox.getChildren().addAll(score_label, hBox_top, grid, hBox_bottom, hBox_bottom2);

        HBox hBox_game = new HBox();
        hBox_game.setAlignment(Pos.CENTER);
        hBox_game.setSpacing(25);
        hBox_game.getChildren().addAll(weight_area, vBox);


        //create a scene and add it to the stage
        Scene scene = new Scene(hBox_game, 800, 700);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            @SuppressWarnings("Duplicates")
            /**
             Time Complexity: O(1)
             Space Complexity: O(1)
             Auxiliary Space: O(1)
             */
            public void handle(KeyEvent event) {
                if(!goal_isReached){
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
                        new_pos = Runner_Movement.moveUp(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);

                        // if the new position is not null, i.e the runner moved despite the restrictions set in the upward movement
                        if(new_pos != null){
                            grid_nodes[new_pos[0]][new_pos[1]] = runner;
                            runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                            // add his last position to the recent moves
                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));

                            // add his new position in the visited cells
                            visited_cells.add(new_pos);

                            // update the user's last action to be move up
                            recent_action = "up";

                            if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                goal_isReached = true;
                            }
                        }
                    }
                    // check if key pressed is DOWN and move runner down. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.DOWN){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveDown(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                        runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                        if(new_pos != null){
                            grid_nodes[new_pos[0]][new_pos[1]] = runner;
                            runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "down";

                            if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                goal_isReached = true;
                            }
                        }
                    }
                    // check if key pressed is LEFT and move runner left. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.LEFT){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveLeft(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                        runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                        if(new_pos != null){
                            grid_nodes[new_pos[0]][new_pos[1]] = runner;
                            runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "left";

                            if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                goal_isReached = true;
                            }
                        }
                    }
                    // check if key pressed is RIGHT and move runner right. update the recent moves stack, the visited cells list and recent action
                    else if(event.getCode() == KeyCode.RIGHT){
                        if(count_undos > 0){
                            count_undos -= 1;
                        }
                        undo.setDisable(false);
                        new_pos = Runner_Movement.moveRight(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                        runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                        if(new_pos != null){
                            grid_nodes[new_pos[0]][new_pos[1]] = runner;
                            runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                            recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                            visited_cells.add(new_pos);
                            recent_action = "right";

                            if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                goal_isReached = true;
                            }
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
                            new_pos = Runner_Movement.moveNorthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                            runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                            if(new_pos != null){
                                grid_nodes[new_pos[0]][new_pos[1]] = runner;
                                runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                                recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                                visited_cells.add(new_pos);
                                recent_action = "north east";

                                if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                    goal_isReached = true;
                                }
                            }
                        }
                        // check if key pressed is S and move runner south east. update the recent moves stack, the visited cells list and recent action
                        else if(event.getCode() == KeyCode.S){
                            if(count_undos > 0){
                                count_undos -= 1;
                            }
                            undo.setDisable(false);
                            new_pos = Runner_Movement.moveSouthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                            runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                            if(new_pos != null){
                                grid_nodes[new_pos[0]][new_pos[1]] = runner;
                                runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                                recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                                visited_cells.add(new_pos);
                                recent_action = "south east";

                                if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                    goal_isReached = true;
                                }
                            }
                        }
                        // check if key pressed is Q and move runner north west. update the recent moves stack, the visited cells list and recent action
                        else if(event.getCode() == KeyCode.Q){
                            if(count_undos > 0){
                                count_undos -= 1;
                            }
                            undo.setDisable(false);
                            new_pos = Runner_Movement.moveNorthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                            runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                            if(new_pos != null){
                                grid_nodes[new_pos[0]][new_pos[1]] = runner;
                                runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                                recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                                visited_cells.add(new_pos);
                                recent_action = "north west";

                                if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                    goal_isReached = true;
                                }
                            }
                        }
                        // check if key pressed is A and move runner south west. update the recent moves stack, the visited cells list and recent action
                        else if(event.getCode() == KeyCode.A){
                            if(count_undos > 0){
                                count_undos -= 1;
                            }
                            undo.setDisable(false);
                            new_pos = Runner_Movement.moveSouthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                            runner.posOnGrid = new int[]{getRunner_Xpos(roadrunner), getRunner_Ypos(roadrunner)};

                            if(new_pos != null){
                                grid_nodes[new_pos[0]][new_pos[1]] = runner;
                                runner.posOnGrid = new int[]{new_pos[0], new_pos[1]};

                                recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                                visited_cells.add(new_pos);
                                recent_action = "south west";

                                if(new_pos[0] == getRunner_Xpos(goal) && new_pos[1] == getRunner_Ypos(goal)){
                                    goal_isReached = true;
                                }
                            }
                        }
                    }

                    score = get_score(score, new_pos, environ_map, weights_dict);
                    score_label.setText("SCORE: " + score);

                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
}

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
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
        newView.setFitWidth(20);
        newView.setFitHeight(20);

        // update the image at the current runner's position
        grid.add(newView, runner_ypos, runner_xpos);

        // remove the runner from the grid
        grid.getChildren().remove(runner);

        // readd the runner at the new undone position
        grid.add(runner, y, x);

        // return the undone move
        return visited_cells.remove(visited_cells.size() - 1);
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
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
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        // update the image at the current runner's position
        grid.add(imageView, runner_ypos, runner_xpos);

        // delete the runner from the grid
        grid.getChildren().remove(runner);

        // readd the runner to the grid
        grid.add(runner, y, x);

        // update the visited cells list witht the redone move
//        visited_cells.add(redo_pos);

        // return the redone move
        return redo_pos;
    }

    /**
     Time Complexity: O(N)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
    // function to move the runner on the grid based on the path determined by the algorithms defined above. Returns the score of the route
    public static int move_with_algorithm(GridPane grid, ImageView roadrunner, ImageView goal, HashMap<Integer,Image> image_alt_dict, ArrayList<int[]> environ_map, ArrayList<int[]> visited_cells, Stack<int[]> recent_moves, ArrayList<String> path, Cell_Node[][] grid_nodes, int score, int count, HashMap<Integer,Integer> weights_dict){

            // move the runner up when the direction says north from his position
            if (path.get(count).trim().equals("North")) {
                int[] new_pos = Runner_Movement.moveUp(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);

                // if the new position is not null, i.e the runner moved despite the restrictions set in the upward movement
                if (new_pos != null) {
                    // add his last position to the recent moves
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));

                    // update the score
                    score = get_score(score, new_pos, environ_map, weights_dict);

                    // add his new position in the visited cells
                    visited_cells.add(new_pos);

                }

            }

            // move the runner left when the direction says east from his position
            else if (path.get(count).trim().equals("East")) {
                int[] new_pos = Runner_Movement.moveRight(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner down when the direction says south from his position
            else if (path.get(count).trim().equals("South")) {
                int[] new_pos = Runner_Movement.moveDown(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner left when the direction says west from his position
            else if (path.get(count).trim().equals("West")) {
                int[] new_pos = Runner_Movement.moveLeft(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner north east when the directions says north east
            else if (path.get(count).trim().equals("North East")) {
                int[] new_pos = Runner_Movement.moveNorthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner north west when the directions says north west
            else if (path.get(count).trim().equals("North West")) {
                int[] new_pos = Runner_Movement.moveNorthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner south east when the directions says south east
            else if (path.get(count).trim().equals("South East")) {
                int[] new_pos = Runner_Movement.moveSouthEast(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

            // move the runner south west when the directions says south west
            else if (path.get(count).trim().equals("South West")) {
                int[] new_pos = Runner_Movement.moveSouthWest(grid, roadrunner, image_alt_dict, environ_map, visited_cells, grid_nodes);
                if (new_pos != null) {
                    recent_moves.push(visited_cells.get(visited_cells.size() - 1));
                    score = get_score(score, new_pos, environ_map, weights_dict);
                    visited_cells.add(new_pos);
                }
            }

        return score;
    }

    /**
     Time Complexity: O(1)
     Space Complexity: O(N)
     Auxiliary Space: O(N)
     */
    public static int get_score(int score, int[] new_pos, ArrayList<int[]> map, HashMap<Integer,Integer> weights_dict) {
        if (new_pos != null) {
            switch (map.get(new_pos[0])[new_pos[1]]) {
                case 0:
                    score += weights_dict.get(0);
                    break;
                case 2:
                    score += weights_dict.get(2);
                    break;
                case 3:
                    score += weights_dict.get(3);
                    break;
                case 4:
                    score += weights_dict.get(4);
                    break;
                case 5:
                    score += weights_dict.get(5);
                    break;
                case 6:
                    score += weights_dict.get(6);
                    break;
            }
        }
        return score;
    }


    // Used the function in Runner Movement instead. Passed in the image dict with original images instead of the alt images dict

}
