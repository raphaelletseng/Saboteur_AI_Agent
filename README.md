# SaboteurComp424
Development of a final project for the comp 424 mcGill course

The best coders are happy coders!

## Strategy

- Bonus with malus played against us worth MAX
- Map when we don't know where the nugget is worth MAX
- Bonus with no malus played against us worth LEAST
- Map when we know where the nugget is worth LEAST 
- we know where the nugget is if it was revealed by a map card or we played 2 map cards 
- Malus card worth less than MAX but still valuable
- Tile card heuristic: number of moves up/down and left/right to get to the nugget 
- if we don't know where the nugget is, then we will use the average based on what we know about the hidden objectives to calculate the heuristic
- Destroy cards we will avoid
- in the case where we cannot prune any branches and cannot find anywhere to play a tile card, we will drop a card 
