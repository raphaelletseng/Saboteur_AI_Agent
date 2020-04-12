package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurBonus;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDestroy;
import Saboteur.cardClasses.SaboteurMalus;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;

public class MyTools {
    public static double getSomething() {
        return Math.random();
    }
    
    /*
     * heuristic function for non-tile SaboteurCards
     */
    public static int cardHeuristic(SaboteurCard card, int knowNugget, int nbMalus) {
    		if (card instanceof SaboteurMap) {
    			if (knowNugget == 0) {
    				return -100;
    			} else {
    				return 101; // not worth playing a map card if we know where the nugget is
    			}
    		} else if (card instanceof SaboteurMalus) {
    			return -90;
    		} else if (card instanceof SaboteurBonus) {
    			if (nbMalus > 0) { // malus played against us
    				return -100;
    			} else {
    				return 100;
    			}
    		} else { // SaboteurDestroy
    			return 100;
    		}
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
    		
    		// continuous cards with exit at bottom
    		if (idx.equals("0") || idx.equals("5") || idx.equals("6") || idx.equals("6f") || idx.equals("7f") || idx.equals("8") || idx.equals("9")) {
    			return yDistance + Math.abs(xDistance) - 1;
    		// continuous cards with exit at both side
    		} else if (idx.equals("9f") || idx.equals("10")) {
    			if (xDistance != 0) {
    				return yDistance + Math.abs(xDistance) - 1;
    			} else {
    				return yDistance + 1;
    			}
    		// continuous cards with exit at only one side
    		} else if (idx.equals("5f")) {
    			if (xDistance >= 0) {
    				return yDistance + xDistance + 1;
    			} else {
    				return yDistance - xDistance - 1;
    			}
    		} else if (idx.equals("7")) {
    			if (xDistance <= 0) {
    				return yDistance - xDistance + 1;
    			} else {
    				return yDistance + xDistance - 1;
    			}
    		// discontinuous cards
    		} else {
    			return 100 - (yDistance + xDistance);
    		}
    }
    
    /*
     * returns the x,y - coordinate of where we think the nugget is based on what hidden objects we have revealed
     * if nugget is known, returns location of the nugget
     * if hidden1 or hidden2 known, returns average location of the other two hidden objectives
     * if hidden1 and hidden2 known, returns locations of the other hidden objective
     * third element of array is boolean indicating whether we know where the nugget is
     */
    public static int[] nuggetAverage(SaboteurBoardState boardState) {
    		SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    		String tile1 = tileBoard[12][3].getIdx();
    		String tile2 = tileBoard[12][5].getIdx();
    		String tile3 = tileBoard[12][7].getIdx();
    		
    		// we know where the nugget is
    		if (tile1.equals("nugget")) {
    			return new int[] {12,3,1};
    		} else if (tile2.equals("nugget")) {
    			return new int[] {12,5,1};		
    		} else if (tile3.equals("nugget")) {
    			return new int[] {12,7,1};
    		// we know where the nugget isn't
    		} else if (tile1.equals("hidden1")) {
    			if (tile2.equals("hidden2")) {
    				return new int[] {12,7,1};
    			} else if (tile3.equals("hidden2")) {
    				return new int[] {12,5,1};
    			} else {
    				return new int[] {12,6,0};
    			}
    		} else if (tile1.equals("hidden2")) {
    			if (tile2.equals("hidden1")) {
    				return new int[] {12,7,1};
    			} else if (tile3.equals("hidden1")) {
    				return new int[] {12,5,1};
    			} else {
    				return new int[] {12,6,0};
    			}
    		} else if ((tile2.equals("hidden1") || tile2.equals("hidden2")) && !tile3.equals("8")) {
    			return new int[] {12,3,1};
    		} else if (tile2.equals("hidden1") || tile2.equals("hidden2")){
    			return new int[] {12, 3, 0};
    		} else if (tile3.equals("hidden1") || tile3.equals("hidden2")) {
    			return new int[] {12,4,0};
    		// we know nothing
    		} else {
    			return new int[] {12,5,0};
    		}
    }
    
    /*
     * decides which card in our hand to drop
     * takes hand as input
     * returns index of the card to drop
     * drop priority: (destroy, map cards when we know where the nugget is), "unhelpful" tiles, (helpful tiles, malus/bonus, map cards)
     * TODO: decide priority of (helpful tiles, malus/bonus, map cards) and adjust code accordingly
     */
    public static int dropCard(ArrayList<SaboteurCard> cards, int knowNugget) {
    		int priority = 10;
    		int index = 0;
    		for(int i=0; i<cards.size(); i++) {
    			SaboteurCard card = cards.get(i);
    			if ((card instanceof SaboteurDestroy) || (card instanceof SaboteurMap && knowNugget == 1)) {
    				return i;
    			} else if (card instanceof SaboteurTile) {
    				String cardIdx = ((SaboteurTile)card).getIdx();
    				if (cardIdx.equals("1") || cardIdx.equals("2") || cardIdx.equals("2f") || cardIdx.equals("3") || cardIdx.equals("3f") 
    						|| cardIdx.equals("4") || cardIdx.equals("4f") || cardIdx.equals("11") || cardIdx.equals("11f") || cardIdx.equals("12") 
    						|| cardIdx.equals("12f") || cardIdx.equals("13") || cardIdx.equals("14") || cardIdx.equals("14f") || cardIdx.equals("15")) {
    					if (priority > 2) {
    						index = i;
    						priority = 2;
    					} 
    				} else {
    					if (priority > 3) {
    						index = i;
    						priority = 3;
    					}
    				}
    			} else {
    				if (priority > 3) {
    					index = i;
    					priority = 3;
    				}
    			}
    		}
    		return index;
    }
}