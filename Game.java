package Game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Game {
	static int depth = 0;
	static int size = 0;
	static String mode ="";
	static String move;
	static String name;
	static int[][] board;
	static char[][] finalstate;
	static char player;
	static char opponent;
	static int emptyspaces;
	static ArrayList<Node> list = new ArrayList<Node>();
	
	public static void display(int score, String name, String move, char[][] state){
		System.out.println(name + move);
		for(int i = 0 ; i < size ; i ++){
			for(int j = 0 ; j < size ; j++)
				System.out.print(state[i][j]);
			System.out.println();
		}
		File file = new File("output.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(name + move);
			writer.newLine();
			for(int i = 0 ; i < size ; i ++){
				for(int j = 0 ; j < size ; j++)
					writer.write(state[i][j]);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int checkRaid(Node node,int row, int col){
		boolean raid = false;
		boolean mate = false;
		char[][] state = node.state;
		if((row != size-1 && state[row+1][col]== state[row][col]) || 
				(row != 0 && state[row-1][col]== state[row][col]) || 
					(col != size-1 && state[row][col+1]== state[row][col]) || 
						(col != 0 && state[row][col-1]== state[row][col])){
			mate = true;
		}
		
		if(mate){
			if(row != 0 && state[row-1][col]!='.' && state[row-1][col] != state[row][col]){
				state[row-1][col] = state[row][col];
				raid = true;
			}
			if(col != size-1 && state[row][col+1]!='.' && state[row][col+1] != state[row][col]){
				state[row][col+1] = state[row][col];
				raid = true;
			}
			if(col != 0 && state[row][col-1]!='.' && state[row][col-1] != state[row][col]){
				state[row][col-1] = state[row][col];
				raid = true;
			}
			if(row != size-1 && state[row+1][col]!='.' && state[row+1][col] != state[row][col]){
				state[row+1][col] = state[row][col];
				raid = true;
			}
		}
		if (raid) {
			node.move = "Raid";
			return 1;
		}
		return 0;
	}
	public static int maxval(Node node){
		int value = -Integer.MAX_VALUE;
		if(node.depth == Game.depth) return findscore(node);
		ArrayList<Node> nodelist = new ArrayList<Node>();
		for(int i = 0 ; i< size; i++){
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					child.move="Stake";
					nodelist.add(child);
				}
			}
		}
		for(int i = 0 ; i< size; i++){
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					if(checkRaid(child,i,j) == 1){
						nodelist.add(child);
					}
				}
			}
		}
		Iterator<Node> nitr = nodelist.iterator();
		while(nitr.hasNext()){
			Node child = nitr.next();
			int minv = minval(child);
			value = Math.max(value, minv);
		}
		nodelist.clear();
		return value;
	}
	public static int minval(Node node){
		int value = Integer.MAX_VALUE;
		if(node.depth == Game.depth){
			int score = findscore(node);
			if(node.depth == 1){
				if(list.contains(node))
					list.remove(node);
				node.score = score;
				list.add(node);
			}
			return score;
		}
		ArrayList<Node> nodelist = new ArrayList<Node>();
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					child.move="Stake";
					nodelist.add(child);
				}
			}
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					if(checkRaid(child,i,j) == 1){
						nodelist.add(child);
					}
				}
			}
		Iterator<Node> nitr = nodelist.iterator();
		while(nitr.hasNext()){
			Node child = nitr.next();
			int maxv = maxval(child);
			value = Math.min(value, maxv);
			node.score = value;
			if(node.depth == 1){
				if(list.contains(node))	list.remove(node);
				list.add(node);
			}
			
		}
		nodelist.clear();
		return value;
	}
	
	public static int maxval_alphabeta(Node node, int alpha, int beta){
		int value = -Integer.MAX_VALUE;
		if(node.depth == Game.depth){
			return findscore(node);
		}
		
		ArrayList<Node> nodelist = new ArrayList<Node>();
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					child.move = "Stake";
					nodelist.add(child);
				}
			}
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					if(checkRaid(child,i,j) == 1){
						nodelist.add(child);
					}
				}
			}
		Iterator<Node> nitr = nodelist.iterator();
		while(nitr.hasNext()){
			Node child = nitr.next();
			int minv = minval_alphabeta(child, alpha, beta);
			value = Math.max(value, minv);
			 
			if(value >= beta) {
				node.score = value;
				return value;
			}
			alpha = Math.max(alpha, value);
		}
		nodelist.clear();
		node.score = value;
		return value;
	}
	public static int minval_alphabeta(Node node, int alpha, int beta){
		int value = Integer.MAX_VALUE;
		if(node.depth == Game.depth) {
			int score = findscore(node);
			if(node.depth == 1){
				node.score = score;
				list.add(node);
			}
			return score;
		}
		
		
		ArrayList<Node> nodelist = new ArrayList<Node>();
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					child.move = "Stake";
					nodelist.add(child);		
				}
			}
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					if(checkRaid(child,i,j) == 1){
						nodelist.add(child);
					}
				}
			}
		Iterator<Node> nitr = nodelist.iterator();
		while(nitr.hasNext()){
			Node child = nitr.next();
			int maxv = maxval_alphabeta(child,alpha,beta);
			value = Math.min(value, maxv);
			if(value <=	 alpha){
				node.score = value;
				return value;
			}
			beta = Math.min(beta, value);			
		}
		nodelist.clear();
		node.score = value;
		if(node.depth == 1){
			if(list.contains(node))
				list.remove(node);
			list.add(node);
		}
		return value;
	}
	public static int findscore(Node node){
		int score = 0;
		for(int i = 0 ; i < size ; i ++){
			for(int j = 0 ; j < size ; j++){
				if(node.state[i][j] == player)
					score+= board[i][j];
				else if(node.state[i][j] == opponent)
					score-= board[i][j];
			}
		}
		node.score = score;
		return score;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		try {
			File file = new File("input.txt");
			Scanner scanner = new Scanner(file);
			
			
			size = scanner.nextInt();
			board = new int[size][size];
			char[][] state = new char[size][size];
			mode = scanner.next();
			player = scanner.next().charAt(0);
			opponent = (player == 'X')? 'O' : 'X';
			depth = scanner.nextInt();
			
			for(int i = 0 ; i < size; i++)
				for(int j = 0 ; j < size; j++)
					board[i][j]= scanner.nextInt();
			
			for(int i = 0 ; i < size; i++){
				String row = scanner.next();
				for(int j = 0 ; j < size; j++)
					state[i][j]= row.charAt(j);
			}
			for(int i = 0 ; i < size; i++)
				for(int j = 0 ; j < size; j++)
					if(state[i][j]=='.')emptyspaces ++;
			if(emptyspaces < Game.depth) Game.depth = emptyspaces;

			scanner.close();
			Node head = new Node(size,0, player, state,null);
			
			
			int k=0;
			switch(mode){
				case "MINIMAX" : 	k = maxval(head); break;
				case "ALPHABETA" : 	k = maxval_alphabeta(head, -Integer.MAX_VALUE, Integer.MAX_VALUE); break;
			}
			finalstate = new char[size][size];
			Iterator<Node> itr = list.iterator();
			while(itr.hasNext()){
				Node e  = itr.next();
				if(e.score == k){
					if(move == "Stake")
						continue;				
					if(move =="Raid" && e.move == "Raid")
						continue;
					name = e.name;
					move = e.move;
					for(int i = 0 ; i < size ; i ++)
						for(int j = 0 ; j < size ; j++)
							finalstate[i][j] = e.state[i][j];
						
				}
				e=null;
			}
			
			display(k,name, move, finalstate);
			System.out.println(System.currentTimeMillis() - start);
			finalstate = null;
			name = "";
			move = "";
			depth=0;
			list.clear();
			emptyspaces = 0;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
