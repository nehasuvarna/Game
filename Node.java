package homework;


public class Node {
	int size;
	int depth;
	int score;
	char player;
	char[][] state;
	String move;
	String name;
	Node parent;
	Node(){
		
	}
	Node(int size, int depth, char player,char[][] state, Node parent){
		this.size = size;
		this.depth = depth;
		this.player = player;
		this.state = state;
		this.parent = parent;
	}
	Node(Node parent){
		this.size = parent.size;
		this.depth = parent.depth+1;
		this.player = (parent.player == 'X')? 'O' : 'X';
		this.parent = parent;
		this.state = new char[this.size][this.size];
		for(int y = 0 ; y< size; y++)
			this.state[y] = parent.state[y].clone();
	}
}
