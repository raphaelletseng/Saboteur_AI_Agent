package student_player;

import Saboteur.cardClasses.SaboteurTile;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    /* 
     * the heuristic function for tiles
     * returns the number of moves to get to the nugget given a tile, its position, 
     * and our estimate for where the nugget is
     * 
     */
    public int movesToGoal(SaboteurTile tile, int[] pos, int[] nugget) {
    		int nuggetYPos = nugget[0];
    		int nuggetXPos = nugget[1];
    		int tileYPos = pos[0];
    		int tileXPos = pos[1];
    		int yDistance = nuggetYPos - tileYPos;
    		int xDistance = nuggetXPos - tileXPos;
    		String idx = tile.getIdx();
    		if (idx.equals("0") || idx.equals("5") || idx.equals("6") || idx.equals("6f") || idx.equals("7f") || idx.equals("8") || idx.equals("9")) {
    			return yDistance + xDistance - 1;
    		} else if (idx.equals("9f") || idx.equals("10")) {
    			if (xDistance != 0) {
    				return yDistance + xDistance - 1;
    			} else {
    				return yDistance + xDistance + 1;
    			}
    		} else if (idx.equals("5f")) {
    			if (xDistance >= 0) {
    				return yDistance + xDistance + 1;
    			} else {
    				return yDistance + xDistance - 1;
    			}
    		} else if (idx.equals("7")) {
    			if (xDistance <= 0) {
    				return yDistance + xDistance + 1;
    			} else {
    				return yDistance + xDistance - 1;
    			}
    		}
    		
    		return 100;
    }
}