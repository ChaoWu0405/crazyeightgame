package com.a1.yahtzeeGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player implements Serializable {

	/*
	 * score sheet is saved as a hashmap upper one, two, three, four, five, six
	 * lower 3ok, 4ok, full, sst, lst, yahtzee, chance, lowerbonus, upperbonus
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;

	int playerId = 0;


	Game game = new Game();
	private int[] scoreSheet = new int[15];

	static Client clientConnection;

	Player[] players = new Player[3];
	ArrayList<String> card = new ArrayList<String>();
	ArrayList<String> topCard = new ArrayList();
//	private ArrayList<String> scoreSheetKey = new ArrayList<String>(Arrays.asList("one", "two", "three", "four", "five",
//			"six", "3ok", "4ok", "full", "sst", "lst", "yahtzee", "chance", "bonus"));

	/*
	 * play a round of the game
	 */
	public int[] playRound(int[] dieRoll) {
		Scanner myObj = new Scanner(System.in);
		int count = 0; // reroll 3 times
		int stop = 0;

		//game.printDieRoll(dieRoll);
		while (stop == 0) {
			System.out.println("Select an action: ");
			if (count < 100) {
				System.out.println("(1) select card to push out");
				System.out.println("(2) take a card from the card space");
			}

			int act = myObj.nextInt();
			if (act == 1) {
				System.out.println("enter the index:");
				int index = myObj.nextInt();
				System.out.println(card.get(index));
				if(card.get(index).charAt(0)=='8'){
					System.out.println("choose rank:");
					System.out.println("(1) CLUB");
					System.out.println("(2) SPADE");
					System.out.println("(3) DIAMOND");
					System.out.println("(4) HEART");

				}
				if(game.isCardOk(card.get(index),topCard)){
					System.out.println("Top Card :");
					System.out.println(topCard.get(0));
					card.remove(index);
					System.out.println("Hand Card:"+card);
					//clientConnection.sendString("topcard");
					stop=-1;
				}
				else{
					System.out.println("you can not push this card out");
					System.out.println("Your Hand Card:");
					System.out.println(card);
					System.out.println("The card you need to follow:");
					System.out.println(topCard.get(0));
				}
			}

			if (act == 2) {
				int c1=0;
				for(int i=0;i<card.size();i++){
					if(game.isCardOk(card.get(i),topCard)){
						c1++;
					}
				}
				if(c1==0){
					card.add(game.cardspace.get(0));
					//card.add();
					game.cardspace.remove(0);
					System.out.println("new hand card: "+card);
					//System.out.println("Top card :"+topCard.get(0));
					count++;
					if(count==3){System.out.println("you can not draw anything turn end");stop=-1;}

				}
				else{
					System.out.println("you can not draw card");
					System.out.println(card);
					System.out.println("top card: "+topCard.get(0));
				}

			}

		}
		return this.scoreSheet;

	}

	public int[] scoreRound(int r, int[] dieRoll) {
		if (r == 7)
			setScoreSheet(6, game.scoreThreeOfAKind(dieRoll));
		else if (r == 8)
			setScoreSheet(7, game.scoreFourOfAKind(dieRoll));
		else if (r == 9)
			setScoreSheet(8, game.scoreFullHouse(dieRoll));
		else if (r == 10)
			setScoreSheet(9, game.scoreSmallStraight(dieRoll));
		else if (r == 11)
			setScoreSheet(10, game.scoreLargeStraight(dieRoll));
		else if (r == 12)
			setScoreSheet(11, game.scoreYahtzee(dieRoll));
		else if (r == 13) {
			setScoreSheet(12, game.scoreChance(dieRoll));
		} else
			setScoreSheet(r - 1, game.scoreUpper(dieRoll, r));
		return getScoreSheet();
	}

	public int getScore() {
		int sc = getLowerScore() + getUpperScore();
		if (getScoreSheet()[13] >= 0)
			sc += scoreSheet[13];
		if (getScoreSheet()[14] >= 0)
			sc += scoreSheet[14];
		return sc;
	}

	/*
	 * loop through the first 6 elements of the score sheet and return
	 */
	public int getUpperScore() {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (this.getScoreSheet()[i] >= 0)
				count += this.scoreSheet[i];
		}
		return count;
	}

	/*
	 * sum of elements 6 - 13 including the yahtzee bonus
	 */
	public int getLowerScore() {
		int count = 0;
		for (int i = 6; i < 13; i++) {
			if (this.getScoreSheet()[i] >= 0)
				count += this.scoreSheet[i];
		}
		return count;
	}

	public int[] getScoreSheet() {
		return scoreSheet;
	}

	public void setScoreSheet(int cat, int score) {
		this.scoreSheet[cat] = score;
	}

	public void setScoreSheet(int[] ss) {
		this.scoreSheet = ss;
	}

	public Player getPlayer() {
		return this;
	}

	/*
	 * ----------Network Stuff------------
	 */

	/*
	 * send the to do to test server
	 */
	public void sendStringToServer(String str) {
		clientConnection.sendString(str);
	}

	public void connectToClient() {
		clientConnection = new Client();
	}

	public void connectToClient(int port) {
		clientConnection = new Client(port);
	}

	public void initializePlayers() {
		for (int i = 0; i < 3; i++) {
			players[i] = new Player(" ");
		}
	}

	/*
	 * update turns
	 */
	public void printPlayerScores(Player[] pl) {
		// print the score sheets

		if (playerId == 1) {
			game.printScoreSheet(pl[0]);
			game.printScoreSheet(pl[1]);
			game.printScoreSheet(pl[2]);
		} else if (playerId == 2) {
			game.printScoreSheet(pl[1]);
			game.printScoreSheet(pl[0]);
			game.printScoreSheet(pl[2]);
		} else {
			game.printScoreSheet(pl[2]);
			game.printScoreSheet(pl[0]);
			game.printScoreSheet(pl[1]);
		}
	}

	public void startGame() {
		// receive players once for names
		players = clientConnection.receivePlayer();
		System.out.println("ready to play");
		for (int i=0;i<5;i++) {
			card.add(clientConnection.receiveFirstCard());
		}

		System.out.println("Hand Card: ");
		System.out.println(card);
		//topCard.add(clientConnection.receiveFirstCard());
		//System.out.println("Top Card: ");
		//System.out.println(topCard);
		//clientConnection.sendString(topCard.get(0));

		while (true) {
			topCard.add(clientConnection.receiveFirstCard());
			System.out.println("top receive");
			System.out.println("Top card:");
			System.out.println(topCard.get(topCard.size()-1));
			if(topCard.get(topCard.size()-1).charAt(0) == 'Q'){
				topCard.remove(topCard.size()-1);
				topCard.add("Q1C");
				break;
			}
			if(topCard.get(topCard.size()-1).charAt(0) == '2'){

			}
			int round = clientConnection.receiveRoundNo();
			if (round == -1)
				break;
			//System.out.println("\n \n \n ********Round Number " + round + "********");
			int[][] pl = clientConnection.receiveScores();
			for (int i = 0; i < 3; i++) {
				players[i].setScoreSheet(pl[i]);}
			int[] dieRoll = game.rollDice();

			//System.out.println("update"+clientConnection.receiveFirstCard());
			clientConnection.sendScores(playRound(dieRoll));
			System.out.println("Top Card now is:");
			System.out.println(topCard.get(0));
			clientConnection.sendString(topCard.get(0));

		}

	}

	public Player returnWinner() {
		try {
			int[][] pl = clientConnection.receiveScores();
			for (int i = 0; i < 3; i++) {
				players[i].setScoreSheet(pl[i]);
			}
			printPlayerScores(players);
			Player win = (Player) clientConnection.dIn.readObject();
			if (playerId == win.playerId) {
				System.out.println("You win!");
			} else {
				System.out.println("The winner is " + win.name);
			}

			System.out.println("Game over!");
			return win;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class Client {
		Socket socket;
		private ObjectInputStream dIn;
		private ObjectOutputStream dOut;

		public Client() {
			try {
				socket = new Socket("localhost", 1112);
				dOut = new ObjectOutputStream(socket.getOutputStream());
				dIn = new ObjectInputStream(socket.getInputStream());

				playerId = dIn.readInt();

				System.out.println("Connected as " + playerId);
				sendPlayer();

			} catch (IOException ex) {
				System.out.println("Client failed to open");
			}
		}

		public Client(int portId) {
			try {
				socket = new Socket("localhost", portId);
				dOut = new ObjectOutputStream(socket.getOutputStream());
				dIn = new ObjectInputStream(socket.getInputStream());

				playerId = dIn.readInt();

				System.out.println("Connected as " + playerId);
				sendPlayer();

			} catch (IOException ex) {
				System.out.println("Client failed to open");
			}
		}

		/*
		 * function to send the score sheet to the server
		 */
		public void sendPlayer() {
			try {
				dOut.writeObject(getPlayer());
				dOut.flush();
			} catch (IOException ex) {
				System.out.println("Player not sent");
				ex.printStackTrace();
			}
		}

		/*
		 * function to send strings
		 */
		public void sendString(String str) {
			try {
				dOut.writeUTF(str);
				dOut.flush();
			} catch (IOException ex) {
				System.out.println("Player not sent");
				ex.printStackTrace();
			}
		}

		/*
		 * receive scoresheet
		 */
		public void sendScores(int[] scores) {
			try {
				for (int i = 0; i < scores.length; i++) {
					dOut.writeInt(scores[i]);
				}
				dOut.flush();

			} catch (IOException e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			}
		}

		/*
		 * receive scores of other players
		 */
		public Player[] receivePlayer() {
			Player[] pl = new Player[3];
			try {
				Player p = (Player) dIn.readObject();
				pl[0] = p;
				p = (Player) dIn.readObject();
				pl[1] = p;
				p = (Player) dIn.readObject();
				pl[2] = p;
				return pl;

			} catch (IOException e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("class not found");
				e.printStackTrace();
			}
			return pl;
		}

		/*
		 * receive scores of other players
		 */
		public int[][] receiveScores() {
			try {
				int[][] sc = new int[3][15];
				for (int j = 0; j < 3; j++) {
					for (int i = 0; i < 15; i++) {
						sc[j][i] = dIn.readInt();
					}
					System.out.println();
				}

				return sc;
			} catch (Exception e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * receive scores of other players
		 */
		public int receiveRoundNo() {
			try {
				return dIn.readInt();

			} catch (IOException e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			}
			return 0;
		}
		public String receiveFirstCard() {
			try {

				return dIn.readUTF();

			} catch (IOException e) {
				System.out.println("first card not received");
				e.printStackTrace();
			}
			return "";
		}
		public ArrayList<String> receiveFirstCard2(int pl) {
			try {
				for (int i=0;i<4;i++) {
					players[pl].card.add(dIn.readUTF());
				}
			} catch (IOException e) {
				System.out.println("first card not received");
				e.printStackTrace();
			}
			return players[pl].card;
		}

	}

	/*
	 * ---------Constructor and Main class-----------
	 */

	/*
	 * constructor takes the name of the player and sets the score to 0
	 */
	public Player(String n) {
		name = n;
		for (int i = 0; i < scoreSheet.length; i++) {
			scoreSheet[i] = -1;
		}
	}

	public static void main(String args[]) {
		Scanner myObj = new Scanner(System.in);
		System.out.print("What is your name ? ");
		String name = myObj.next();
		Player p = new Player(name);
		p.initializePlayers();
		p.connectToClient();
		p.startGame();
		p.returnWinner();
		myObj.close();
	}
}
