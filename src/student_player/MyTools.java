package student_player;

import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    /* 
     * the heuristic function for tiles
     * returns the number of moves to get to the nugget given a tile, its position, and our estimate of where the nugget is
     */
    public static int movesToGoal(SaboteurTile tile, int[] pos, int[] nugget) {
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
    		} else {
    			return 100 - (yDistance + xDistance);
    		}
    }
    
    /*
     * returns the x,y - coordinate of where we think the nugget is based on what hidden objects we have revealed
     * if nugget is known, returns location of the nugget
     * if hidden1 or hidden2 known, returns average location of the other two hidden objectives
     * if hidden1 and hidden2 known, returns locations of the other hidden objective
     */
    public static int[] nuggetAverage(SaboteurBoardState boardState) {
    		SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    		String tile1 = tileBoard[12][3].getIdx();
    		String tile2 = tileBoard[12][5].getIdx();
    		String tile3 = tileBoard[12][3].getIdx();
    		
    		if (tile1.equals("nugget")) {
    			return new int[] {12,3};
    		} else if (tile2.equals("nugget")) {
    			return new int[] {12,5};
    		} else if (tile3.equals("nugget")) {
    			return new int[] {12,7};
    		} else if (tile1.equals("hidden1")) {
    			if (tile2.equals("hidden2")) {
    				return new int[] {12,7};
    			} else if (tile3.equals("hidden2")) {
    				return new int[] {12,5};
    			} else {
    				return new int[] {12,6};
    			}
    		} else if (tile1.equals("hidden2")) {
    			if (tile2.equals("hidden1")) {
    				return new int[] {12,7};
    			} else if (tile3.equals("hidden1")) {
    				return new int[] {12,5};
    			} else {
    				return new int[] {12,6};
    			}
    		} else if ((tile2.equals("hidden1") || tile2.equals("hidden2")) && !tile3.equals("8")) {
    			return new int[] {12,3};
    		} else if (tile3.equals("hidden1") || tile3.equals("hidden2")) {
    			return new int[] {12,4};
    		} else {
    			return new int[]{12,5};
    		}
    		
    }
}