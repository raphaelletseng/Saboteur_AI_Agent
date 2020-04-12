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
        
        // prep variables
        int[] nugget = MyTools.nuggetAverage(boardState);
        int[] nuggetPos = new int[2];
        nuggetPos[0] = nugget[0];
        nuggetPos[1] = nugget[1];
        int knowNugget = nugget[2];
        int nbMalus = boardState.getNbMalus(boardState.getTurnPlayer());
        
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
        				if (temp < bestHeuristic) {
        					bestHeuristic = temp;
        					bestCard = tempCard;
        					bestCoords[0] = positions.get(j)[0];
        					bestCoords[1] = positions.get(j)[1];
        				}
        			}
        			if (SaboteurTile.canBeFlipped(((SaboteurTile)tempCard).getIdx())) {
        				for(int j=0; j<positions.size(); j++) {
            				temp = MyTools.movesToGoal(((SaboteurTile)tempCard).getFlipped(), positions.get(j), nuggetPos);
            				if (temp < bestHeuristic) {
            					bestHeuristic = temp;
            					bestCard = ((SaboteurTile)tempCard).getFlipped();
            					bestCoords[0] = positions.get(j)[0];
            					bestCoords[1] = positions.get(j)[1];
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
        					// this is only worth playing if knowNugget = 0
        					// we will guess the outside hidden positions first
        					bestCoords[0] = nuggetPos[0];
        					if (nuggetPos[1] == 5) {
        						bestCoords[1] = 5;
        					} else if (nuggetPos[1] == 4) {
        						bestCoords[1] = 3;
        					} else { // nuggetPosX = 6
        						bestCoords[1] = 7;
        					}
        				} else { // SaboteurMalus, SaboteurBonus, SaboteurDestroy
        					bestCoords[0] = 0;
        					bestCoords[1] = 0;
        				}
        			}
        		}
        }
        
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
        
        move = new SaboteurMove(bestCard, bestCoords[1], bestCoords[0], boardState.getTurnPlayer());
        System.out.println("Card to play: "+move.toPrettyString());
        if (boardState.isLegal((SaboteurMove)move)) {
        		return move; 
        } else {
       		System.out.println("Dropping card 2.");
	        	move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
	    		return move;
        }
        
        /*
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
        */
    }
}