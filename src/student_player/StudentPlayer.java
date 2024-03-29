package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;
import Saboteur.cardClasses.SaboteurMalus;

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
        nuggetPos[0] = nugget[0];
        nuggetPos[1] = nugget[1];
        int knowNugget = nugget[2];
        int nbMalus = boardState.getNbMalus(boardState.getTurnPlayer());
        
        int bestHeuristic = 100; // used to find best move among all possible moves
        int[] bestCoords = new int[2]; // (y,x)
        SaboteurCard bestCard = null;
        
        // a tree to find best card to play
        ArrayList<SaboteurCard> hand = boardState.getCurrentPlayerCards();
        for (int i = 0; i<hand.size(); i++) {
        	if (hand.get(i).getName() == "Map" && (MyTools.NuggetKnown(boardState) == 0)) {
        		Move move1;
        		move1 = new SaboteurMove((new SaboteurMap()), 0, 0, boardState.getTurnPlayer());
        		if (boardState.isLegal((SaboteurMove)move1)) {
            		return move1; 
        		}
        	}//}else if ()
        }        
        
    
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
    					if (boardState.isLegal(new SaboteurMove(tempCard, positions.get(j)[0], positions.get(j)[1], boardState.getTurnPlayer()))) {
        					bestHeuristic = temp;
        					bestCard = tempCard;
        					bestCoords[0] = positions.get(j)[0];
        					bestCoords[1] = positions.get(j)[1];
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
    		move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
    		return move;
    } 
    
    move = new SaboteurMove(bestCard, bestCoords[0], bestCoords[1], boardState.getTurnPlayer());
    if (boardState.isLegal((SaboteurMove)move)) {
    		return move; 
    } else {
        	move = new SaboteurMove((new SaboteurDrop()), MyTools.dropCard(cards, knowNugget), 0, boardState.getTurnPlayer());
    		return move;
    } 
    
} 
    
    public Move minmaxDec(ArrayList<SaboteurMove> moves, SaboteurBoardState boardState) {
    	double[] moveVal = new double[moves.size()];
    	int maxDepth = 3;
    	int curMoveId = 0;
    	for (SaboteurMove curMove : moves) {
    		SaboteurBoardState cloned = (SaboteurBoardState) boardState.clone();
        	cloned.processMove(curMove);
        	if(cloned.getWinner() == player_id) {
        		return curMove;
        	}
        	//moveVal[curMoveId] = minimaxVal(cloned, -10000,10000, 1, maxDepth);
        	curMoveId++;
    		
    	}
    	return moves.get(highestMove(moveVal));

   }
    
    public int highestMove (double[] moveVal) {
    	double maxVal = moveVal[0];
    	int maxI = 0;
    	for (int i = 1; i < moveVal.length;i++) {
    		if (moveVal[i] > maxVal) {
    			maxVal=moveVal[i];
    			maxI = i;
    		}
    	}
    	return maxI;
    }
}
