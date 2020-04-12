package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurMap;
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
    	
        ArrayList<SaboteurCard> cards = boardState.getCurrentPlayerCards();
        for(int i=0; i<cards.size(); i++) {
        		System.out.println(cards.get(i).getName());
        }
        
        // prep variables
        int[] nugget = MyTools.nuggetAverage(boardState);
        int[] nuggetPos = new int[2];
        nuggetPos[0] = nugget[0]; // y
        nuggetPos[1] = nugget[1]; // x
        int knowNugget = nugget[2];
        int nbMalus = boardState.getNbMalus(boardState.getTurnPlayer());
        System.out.println("knowNugget? "+knowNugget);
        System.out.println("nuggetPos: "+nuggetPos[0]+", "+nuggetPos[1]);
        
        int bestHeuristic = 100; // used to find best move among all possible moves
        int[] bestCoords = new int[2]; // (y,x)
        SaboteurCard bestCard = null;
        
        // a tree to find best card to play
        // the branches are the cards in our hand
        // heuristic function returns value of playing a card (in a certain position)
        // choose card with lowest value
        for(int i=0; i<cards.size(); i++) {
        		SaboteurCard tempCard = cards.get(i);
        		int temp;
        		// tile heuristic depends on the distance to where we think the nugget is
        		if (tempCard instanceof SaboteurTile) {
        			ArrayList<int []> positions = boardState.possiblePositions((SaboteurTile)tempCard);
        			for(int j=0; j<positions.size(); j++) {
        				temp = MyTools.movesToGoal((SaboteurTile)tempCard, positions.get(j), nuggetPos);
        				System.out.println("temp at j="+j+": "+temp);
        				if (temp < bestHeuristic) {
        					if (boardState.isLegal(new SaboteurMove(tempCard, positions.get(j)[0], positions.get(j)[1], boardState.getTurnPlayer()))) {
	        					bestHeuristic = temp;
	        					bestCard = tempCard;
	        					bestCoords[0] = positions.get(j)[0];
	        					bestCoords[1] = positions.get(j)[1];
	        					System.out.println("Best move legal.");
        					} else {
        						System.out.println("bestMove not legal, j="+j);
        					}
        				}
        			}
        			if (SaboteurTile.canBeFlipped(((SaboteurTile)tempCard).getIdx())) {
        				for(int j=0; j<positions.size(); j++) {
            				temp = MyTools.movesToGoal(((SaboteurTile)tempCard).getFlipped(), positions.get(j), nuggetPos);
            				if (temp < bestHeuristic) {
            					if (boardState.isLegal(new SaboteurMove(((SaboteurTile)tempCard).getFlipped(), positions.get(j)[0], positions.get(j)[1], boardState.getTurnPlayer()))) {
	            					bestHeuristic = temp;
	            					bestCard = ((SaboteurTile)tempCard).getFlipped();
	            					bestCoords[0] = positions.get(j)[0];
	            					bestCoords[1] = positions.get(j)[1];
	            					System.out.println("Best move legal.");
            					} else {
            						System.out.println("bestMove not legal, j=."+j);
            					}
            				}
            			}
        			}
        		// card heuristic depends on if we know where the nugget is 
        		// and how many malus cards we have played against us
        		} else {
        			temp = MyTools.cardHeuristic(tempCard, knowNugget, nbMalus);
        			if (temp < bestHeuristic) {
        				bestHeuristic = temp;
        				bestCard = tempCard;
        				if (tempCard instanceof SaboteurMap) { 
        					if (knowNugget == 0) {
	        					bestCoords[0] = nuggetPos[0]; // y
	        					if (nuggetPos[1] == 4) {
	        						bestCoords[1] = 3;
	        					} else if (nuggetPos[1] == 6){ 
	        						bestCoords[1] = 7;
	        					} else {
	        						bestCoords[1] = nuggetPos[1];
	        					}
        					} else {
        						System.out.println("nugget mistake!");
        						bestCard = null;
        					}
        				} else { // SaboteurMalus, SaboteurBonus, SaboteurDestroy
        					bestCoords[0] = 0;
        					bestCoords[1] = 0;
        				}
        			}
        		}
        }
        System.out.println("bestHeuristic: "+bestHeuristic);
        Move move;
        // have to drop a card if could not find a move
        if (bestCard == null) {
        		System.out.println("Dropping card 1.");
        		move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
        		return move;
        } else {
        		System.out.println("tile: "+bestCard.getName());
        		System.out.println("position: ("+bestCoords[0]+","+bestCoords[1]+")");
        }
        
        move = new SaboteurMove(bestCard, bestCoords[0], bestCoords[1], boardState.getTurnPlayer());
        System.out.println("Card to play: "+move.toPrettyString());
        if (boardState.isLegal((SaboteurMove)move)) {
        		return move; 
        } else {
       		System.out.println("Dropping card 2.");
	        	move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
	    		return move;
        }
    }
}