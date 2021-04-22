# The-Road-Runner-Code-Implementation
This is a code implementation of the Road Runner inspired by Looney Tunes' "The Road Runner  Show ". The project features the implementation of 3 path finding algorithms (A*, Dijkstra, Depth First Search) to help the Road Runner an optimal path to the goal. The project implements a GUI using the JavaFX library.

## Introdution to the Road Runner
“Wile E. Coyote (also known simply as "the Coyote") and the Road Runner are a duo of cartoon characters from the Looney Tunes and Merrie Melodies series of cartoons. In each episode, the Coyote repeatedly attempts to catch and subsequently eat the Road Runner, a fast-running ground bird, but is never successful. Instead of his animal instincts, the Coyote uses absurdly complex contraptions (sometimes in the manner of Rube Goldberg) to try to catch his prey, which comically "backfire", with the Coyote often getting injured in slapstick fashion. Many of the items for these contrivances are mail-ordered from a variety of companies that are all named Acme”. [(Source: Wikipedia)](https://en.wikipedia.org/wiki/Wile_E._Coyote_and_the_Road_Runner)

## Features:
 1. **Read different input files**
    - 0 => Ordinary Road (The Road Runner is free to move onto any spot)
    - 1 => Boulder (The Road Runner may not occupy or pass through this spot to get to her goal)
    - 2 => Pothole (The Road Runner can occupy this spot in the world and also freely move through a pothole. The pothole is a less ideal path for her. 
    - 3 => Explosives (The Road Runner should avoid explosives on her way to the destination because the Coyote has set up a weird contraption to blow up anytime the Road Runner moves onto this cell in the world.)
    - 4 => Wile E. Coyote (In these spots all over the world, the Coyote has left clones of himself. If the Road Runner goes onto these cells)
    - 5 => Tarred Road (Anytime the Road Runner uses these roads, she gains a little bit more power. She gains 1 energy point anytime she goes over one of these spots)
    - 6 => Gold Road (The best possible path for the Road Runner. These paths tend to be rare. She gains 5 energy points)
    - 8 => This is the start position of the Road Runner in the world.
    - 9 => This is the final destination of the Road Runner.
    
 2. **Toggle 4 directional movement( ↑ → ↓ ← ) using direction keys or 8 directional movement ( ↑ → ↓ ← ↗ ↘  ↙ ↖ ) using directions keys and QWAS for directional movement**
 
 3. **Undo and Redo functionality (undo up to the last 3 moves)**
 4. **No double takes i.e can only visit a spot/cell once**
 5. **Scoring system (Depends on the weights set. Weights can be adjusted)**
 
    **Default weights**
     - Ordinary Road => -1
     - Pothole => -1
     - Explosives =>  
     - Wile E. Coyote => -8
     - Tarred Road => + 1
     - Gold road => +5

 6. **Reset (reset the environment to its initial state)**
 7. **Autonomous path finding (Implemented in A-star, Djikstra and DFS algorithms)**
 8. **Output resulting path to file**
 9. **Set a new start from the default on in the environment or in the file read**
 
 ## Autonomous Path Finding
The project implements 3 graph search algorithms:
  * A* Algorithm
  * Dijkstra's Algorithm
  * Depth First Search (DFS)
  
**A-star Algorithm** finds the shortest path from a start point to the goal ignoring all weights.

**Dijkstra's Algorithm** finds the shortest path from a start to a goal in weighted graphs.

**Depth First Search algorithm** does not necessarily find the shortest path. It only returns the first valid path it comes across while ignoring the weights.


The program uses multithreading to implement a moving Road Runner when a valid path is found. An alert is displayed on the console if no valid path was found to the goal

## File Output
When the algorithms are run, they output the discovered paths to file.

## Sample Solution
1. Path found on 4 directional movement by the Road Runner on the Grand Scale map by the Dijkstra algorithm .

![Path found on 4 directional movement by the Road Runner on the Grand Scale map by the Dijkstra algorithm](https://user-images.githubusercontent.com/43356490/82752757-0371d300-9dc9-11ea-8ec8-ebcb625966a8.png)


2. Path found on 8 directional movement by the Road Runner on the Grand Scale map by the A* algorithm.

![Path found on 8 directional movement by the Road Runner on the Grand Scale map by the A* algorithm](https://user-images.githubusercontent.com/43356490/82752826-7418ef80-9dc9-11ea-8203-3d57bd890518.png)
