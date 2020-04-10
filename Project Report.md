## Write-up
4 and 5 pages (at ∼ 300 words per page)

## 1.How your program works, and a motivation for your approach.
In this project, we sought to implement an AI agent to play Saboteur, a card game with the aim of finding a golden nugget on a 15 x 15 
board before the opposing player. We looked to implement alpha-beta pruning to traverse the game state tree, before
applying A* search with a heuristic function to determine the best course of action. The heuristic function considers the
distance in number of moves between the agent's current place on the board and the 3 possible locations of the golden nugget.

-> Saboteur, imperfect information but deterministic (partially / fully observable)

## 2. A brief description of the theoretical basis of the approach (about a half-page in most cases); references
to the text of other documents, such as the textbook, are appropriate but not absolutely necessary. If
you use algorithms from other sources, briefly describe the algorithm and be sure to cite your source.

In recent years, a boom in AI has resulted in a lot of research being conducted on the topic of games and game playing
agents. Studies on turn-based games such as checkers and Go show that AI agents are most successful when using some variation 
of alpha-beta search or a Monte Carlo Search Tree.

# Algorithms 
1) Alpha-Beta Pruning: This is a technique that says if a path looks worse than what we
already have, then we can discard it (prune it). 

## 3. A summary of the advantages and disadvantages of your approach, expected failure modes, or weaknesses of your program.

## 4. If you tried other approaches during the course of the project, summarize them briefly and discuss how
they compared to your final approach.

## 5. A brief description (max. half page) of how you would go about improving your player (e.g. by
introducing other AI techniques, changing internal representation etc.).
