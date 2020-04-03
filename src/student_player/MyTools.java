package student_player;

import Saboteur.cardClasses.SaboteurTile;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    // the heuristic for tiles
    // returns the number of moves to get to the nugget given a tile and its position
    public int movesToGoal(SaboteurTile tile, int[] pos, int[] nugget) {
    		int nuggetXPos = nugget[0];
    		int nuggetYPos = nugget[1];
    		int tileXPos = pos[0];
    		int tileYPos = pos[1];
    		
    		return 0;
    }
}