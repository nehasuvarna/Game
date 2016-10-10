package homework;

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
	static int[] pos = new int[2];
	static int[][] board;
	static char[][] finalstate;
	static char player;
	static char opponent;
	static int count;
	static ArrayList<Node> list = new ArrayList<Node>();
	Game(){
		
	}
	
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
	public static void checkRaid(Node node,int row, int col){
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
		if(raid)
			node.move = "Raid";
		else
			node.move = "Stake";
	}
	public static int maxval(Node node){
		int value = -9999;
		if(node.depth == Game.depth) return findscore(node);
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					checkRaid(child,i,j);
					
					int minv = minval(child);
					value = Math.max(value, minv);
				}
			}
		return value;
	}
	public static int minval(Node node){
		int value = 9999;
		if(node.depth == Game.depth) {
			int score = findscore(node);
			if(node.depth == 1){
				node.score = score;
				list.add(node);
			}
			return score;
		}
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					checkRaid(child,i,j);
					int maxv = maxval(child);
					value = Math.min(value, maxv);
					node.score = value;
					if(node.depth == 1)
						list.add(node);
				}
			}
		return value;
	}
	
	public static int maxval_alphabeta(Node node, int alpha, int beta){
		System.out.println("Alpha");
		int value = -9999;
		if(node.depth == Game.depth) return findscore(node);
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j<size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = player;
					child.name = (char)(j+65) +""+(i+1)+" ";
					checkRaid(child,i,j);
					
					int minv = minval_alphabeta(child, alpha, beta);
					value = Math.max(value, minv);
					if(value >= beta) return value;
					alpha = Math.max(alpha, value);
				}
			}
		return value;
	}
	public static int minval_alphabeta(Node node, int alpha, int beta){
		int value = 9999;
		if(node.depth == Game.depth) {
			int score = findscore(node);
			if(node.depth == 1){
				node.score = score;
				list.add(node);
			}
			return score;
		}
		for(int i = 0 ; i< size; i++)
			for(int j =0 ; j< size; j++){
				if(node.state[i][j]=='.'){
					Node child = new Node(node);
					child.state[i][j] = opponent;
					checkRaid(child,i,j);
					int maxv = maxval_alphabeta(child,alpha,beta);
					value = Math.min(value, maxv);
					if(value <= alpha) return value;
					beta = Math.min(beta, value);
					node.score = value;
					if(node.depth == 1)
						list.add(node);
				}
			}
		return value;
	}
	public static int findscore(Node node){
		int score = 0;
		for(int i = 0 ; i < size ; i ++)
			for(int j = 0 ; j < size ; j++)
				System.out.print(node.state[i][j]);
		
		for(int i = 0 ; i < size ; i ++){
			for(int j = 0 ; j < size ; j++){
				char otherplayer = (node.player=='X')? 'O' : 'X';
				if(node.state[i][j] == player)
					score+= board[i][j];
				else if(node.state[i][j] == opponent)
					score-= board[i][j];
			}
		}
		System.out.println(" "+score + " "+ node.move);
		node.score = score;
		return score;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
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
					System.out.print(state[i][j]);
			System.out.println();
			scanner.close();
			Node head = new Node(size,0, player, state,null);
			
			System.out.println();
			int k=0;
			switch(mode){
				case "MINIMAX" : 	k = maxval(head); break;
				case "ALPHABETA" : 	k = maxval_alphabeta(head, -99999, 99999); break;
			}
			System.out.println();
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
			}
			
			display(k,name, move, finalstate);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
