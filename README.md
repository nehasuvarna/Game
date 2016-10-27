#Game

The game involves a n*n grid with a specific integer value attached to each grid. The objective of the two player game is to score maximum total score which is the sum of all the grid values captured by the player. 
The two allowed moves are :
1. Stake : Take any open space on the board
2. Raid : Take an open space on the board along with any adjacent space occupied by the opponent.
Developed a program that generated the search tree for a given game state and depth. The tree nodes are the game states after applying one of the moves. And a stake move has higher priority than a raid move for a particular state.The program would implement minimax algorithm in order to predict the next best game move by traversing the entire search tree.
Also implemented alpha-beta pruning, wherein the number of tree nodes evaluated is decreased significantly. The application works smoothly in 9 seconds for a depth of 5 and more than 1000 search nodes.
Technologies : Java



