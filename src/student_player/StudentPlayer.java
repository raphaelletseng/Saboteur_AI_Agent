package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurCard;
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
        int bestHeuristic = 100;
        int[] bestCoords = new int[2];
        SaboteurCard bestCard = cards.get(0);
        for(int i=0; i<cards.size(); i++) {
        		if (cards.get(i) instanceof SaboteurTile) {
        			ArrayList<int[]> positions = boardState.possiblePositions((SaboteurTile)cards.get(i));
        			int heuristic = 100;
        			int[] tempCoords = new int[2];
        			for(int j=0; j<positions.size(); j++) {
        				int temp = MyTools.movesToGoal((SaboteurTile)cards.get(i), positions.get(j), nugget);
        				if (temp<heuristic) {
        					heuristic = temp;
        					tempCoords[0] = positions.get(j)[0];
        					tempCoords[1] = positions.get(j)[1];
        				}
        			}
        			if (heuristic<bestHeuristic) {
        				bestHeuristic = heuristic;
        				bestCoords[0] = tempCoords[0];
        				bestCoords[1] = tempCoords[1];
        				bestCard = cards.get(i);
        			}
        		}
        }
        // idk if playerId is right
        Move move = new SaboteurMove(bestCard, bestCoords[1], bestCoords[0], 1);
        return move; 

        // Return your move to be processed by the server.
        //return myMove;
    }
}