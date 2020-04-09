package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurTile;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("xxxxxxxxx");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();
        ArrayList<SaboteurCard> cards = boardState.getCurrentPlayerCards();

        // Is random the best you can do?
        //Move myMove = boardState.getRandomMove();
        
        // after pruning
        // find best move with tile cards
        int[] nugget = MyTools.nuggetAverage(boardState);
        int[] nuggetPos = new int[2];
        nuggetPos[0] = nugget[0];
        nuggetPos[1] = nugget[1];
        int knowNugget = nugget[2];
        System.out.println("nugget average: (y,x) = ("+nuggetPos[0]+","+nuggetPos[1]+")");
        
        int bestHeuristic = 100; // used to find best position among all tiles in our hand
        int[] bestCoords = new int[2]; // (y,x)
        SaboteurCard bestCard = null;
        
        for(int i=0; i<cards.size(); i++) {
        		if (cards.get(i) instanceof SaboteurTile) {
        			SaboteurTile tile = (SaboteurTile)cards.get(i);
        			ArrayList<int[]> positions = boardState.possiblePositions(tile);
        			int heuristic = 100; // used to find best position for this tile
        			int[] tempCoords = new int[2]; 
        			boolean flipped = false;
        			
        			// get moves to goal for that tile from every position
        			// temp, tempCoords holds the properties of the best move among positions
        			for(int j=0; j<positions.size(); j++) {
        				//System.out.println("j = "+j);
        				int temp = MyTools.movesToGoal(tile, positions.get(j), nuggetPos);
        				if (temp<heuristic && boardState.verifyLegit(tile.getPath(), positions.get(j))) {
        					heuristic = temp;
        					tempCoords[0] = positions.get(j)[0]; // y 
        					tempCoords[1] = positions.get(j)[1]; // x
        					
        					//System.out.println("temp = "+temp);
        					//System.out.println("tile = "+tile.getIdx());
        					//System.out.println("position: "+tempCoords[0]+","+tempCoords[1]);
        				}
        			}
        			
        			// same thing for flipped version of tile
        			if (SaboteurTile.canBeFlipped(tile.getIdx())) {
        				for(int j=0; j<positions.size(); j++) {
            				int temp = MyTools.movesToGoal(tile.getFlipped(), positions.get(j), nuggetPos);
            				if (temp<heuristic) {
            					flipped = true;
            					heuristic = temp;
            					tempCoords[0] = positions.get(j)[0]; // y
            					tempCoords[1] = positions.get(j)[1]; // x
            				}
            			}
        			}
        			
        			// update best move properties
        			if (heuristic<bestHeuristic) {
        				bestHeuristic = heuristic;
        				bestCoords[0] = tempCoords[0]; // y
        				bestCoords[1] = tempCoords[1]; // x
        				if(flipped) {
        					bestCard = tile.getFlipped();
        				} else {
        					bestCard = tile;
        				}
        			}
        		}
        }
        
        // idk if playerId is right
        // drop if could not find card
        if (bestCard == null) {
        		Move move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
        		return move;
        } else if (bestCard instanceof SaboteurTile) {
        		System.out.println("tile: "+((SaboteurTile)bestCard).getIdx());
        		System.out.println("position: ("+bestCoords[0]+","+bestCoords[1]+")");
        }
        
        Move move = new SaboteurMove(bestCard, bestCoords[1], bestCoords[0], boardState.getTurnPlayer());
        System.out.println(move.toPrettyString());
        if (boardState.isLegal((SaboteurMove)move)) {
        		return move; 
        } else {
	        	move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
	    		return move;
        }
        

        // Return your move to be processed by the server.
        //return myMove;
    }
}