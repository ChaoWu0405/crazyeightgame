package com.a1.yahtzeeGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class GameServer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int turnsMade;
	private int maxTurns;

	Server[] playerServer = new Server[3];
	Player[] players = new Player[3];
	ArrayList<String> cardSpace = new ArrayList<String>(Arrays.asList("4H","4S","9S","7D","7S","6S","6C","JH","5D","KC","9C","QH"
			,"6D","8H","JD","KH","9D","10D","3H","5C","6C","6D","6H","6S","7C","7D","7H","7S","8C","8D","8H","8S","9C","9D","9H","9S","10C","10D","10H","10S",
			"JC","JD","JH","JS","QC","QD","QH","QS","KC","KD","KH","KS"));

	ServerSocket ss;

	Game game = new Game();
	int numPlayers;

	public static void main(String args[]) throws Exception {
		GameServer sr = new GameServer();

		sr.acceptConnections();
		sr.gameLoop();
	}

	public GameServer() {
		System.out.println("Starting game server");
		numPlayers = 0;
		turnsMade = 0;
		maxTurns = 13;
		// initialize the players list with new players
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(" ");
		}

		try {
			ss = new ServerSocket(1112);
		} catch (IOException ex) {
			System.out.println("Server Failed to open");
		}

	}

	/*
	 * -----------Networking stuff ----------
	 * 
	 */
	public void acceptConnections() throws ClassNotFoundException {
		try {
			System.out.println("Waiting for players...");
			while (numPlayers < 3) {
				Socket s = ss.accept();
				numPlayers++;

				Server server = new Server(s, numPlayers);

				// send the player number
				server.dOut.writeInt(server.playerId);
				server.dOut.flush();

				// get the player name
				Player in = (Player) server.dIn.readObject();
				System.out.println("Player " + server.playerId + " ~ " + in.name + " ~ has joined");
				// add the player to the player list
				players[server.playerId - 1] = in;
				playerServer[numPlayers - 1] = server;
			}
			System.out.println("Three players have joined the game");

			// start the server threads
			for (int i = 0; i < playerServer.length; i++) {
				Thread t = new Thread(playerServer[i]);
				t.start();
			}
			// start their threads
		} catch (IOException ex) {
			System.out.println("Could not connect 3 players");
		}
	}

	public void gameLoop() {
		try {
			playerServer[0].sendPlayers(players);
			playerServer[1].sendPlayers(players);
			playerServer[2].sendPlayers(players);
			int count =0;

			while(count <5){
				int rand = (int) (Math.random() * (cardSpace.size()-1) + 1);
				//playerServer[0].sendFirstCard(cardSpace.get(rand));
				playerServer[0].sendFirstCard(cardSpace.get(0));//24//30//19
				cardSpace.remove(0);
				//cardSpace.remove(rand);
				System.out.println("player1 draw a card");

				int rand2 = (int) (Math.random() * (cardSpace.size()-1) + 1);
				playerServer[1].sendFirstCard(cardSpace.get(0));
				cardSpace.remove(0);
				System.out.println("player2 draw a card");

				int rand3 = (int) (Math.random() * (cardSpace.size()-1) + 1);
				playerServer[2].sendFirstCard(cardSpace.get(0));
				cardSpace.remove(0);
				System.out.println("player3 draw a card");
				count++;
			}
			//show a top card for each player
			int rand = (int) (Math.random() * (cardSpace.size()-1) + 1);
			//playerServer[0].sendFirstCard(cardSpace.get(rand));
			//playerServer[1].sendFirstCard(cardSpace.get(rand));
			//playerServer[2].sendFirstCard(cardSpace.get(rand));

			//playerServer[0].sendFirstCard(cardSpace.get(rand));/////////////////////////
			playerServer[0].sendFirstCard("4D");
			while (turnsMade < maxTurns) {

				turnsMade++;

				// send the round number
				System.out.println("*****************************************");
				//sent 111111
				System.out.println("Round number " + turnsMade);
				playerServer[0].sendTurnNo(turnsMade);
				playerServer[0].sendScores(players);
				players[0].setScoreSheet(playerServer[0].receiveScores());
				//receive the new top card save in the topCard array
				players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
				//
				System.out.println(players[0].topCard);
				//playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
				//System.out.println("sent new top card to p2");
				//playerServer[2].sendFirstCard(playerServer[0].receiveTopCard());
				//System.out.println("sent new top card to p3");
				System.out.println("Player 1 completed turn and their score is " + players[0].getScore());

//sent2
				playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));///////////////
				System.out.println("play1 top sent");
				playerServer[1].sendTurnNo(turnsMade);
				playerServer[1].sendScores(players);
				players[1].setScoreSheet(playerServer[1].receiveScores());
				players[0].topCard.add(playerServer[1].receiveTopCard());///////////////////////
				System.out.println(players[0].topCard.get(players[0].topCard.size()-1));

				System.out.println("Player 2 completed turn and their score is " + players[1].getScore());
//sent3
				playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
				playerServer[2].sendTurnNo(turnsMade);
				playerServer[2].sendScores(players);
				players[2].setScoreSheet(playerServer[2].receiveScores());
				players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
				System.out.println("Player 3 completed turn and their score is " + players[2].getScore());

				playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
				//players[0].topCard.add(playerServer[0].receiveTopCard());


			}
			// add the upper bonus
			players[0].setScoreSheet(14, game.upperBonus(players[0].getUpperScore()));
			players[1].setScoreSheet(14, game.upperBonus(players[1].getUpperScore()));
			players[2].setScoreSheet(14, game.upperBonus(players[2].getUpperScore()));

			playerServer[0].sendTurnNo(-1);
			playerServer[1].sendTurnNo(-1);
			playerServer[2].sendTurnNo(-1);

			// send final score sheet after bonus added
			playerServer[0].sendScores(players);
			playerServer[1].sendScores(players);
			playerServer[2].sendScores(players);

			Player p = game.getWinner(players);
			System.out.println("The winner is " + p.name);
			for (int i = 0; i < playerServer.length; i++) {
				playerServer[i].dOut.writeObject(p);
				playerServer[i].dOut.flush();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public class Server implements Runnable {
		private Socket socket;
		private ObjectInputStream dIn;
		private ObjectOutputStream dOut;
		private int playerId;

		public Server(Socket s, int playerid) {
			socket = s;
			playerId = playerid;
			try {
				dOut = new ObjectOutputStream(socket.getOutputStream());
				dIn = new ObjectInputStream(socket.getInputStream());
			} catch (IOException ex) {
				System.out.println("Server Connection failed");
			}
		}

		/*
		 * run function for threads --> main body of the thread will start here
		 */
		public void run() {
			try {
				while (true) {
				}

			} catch (Exception ex) {
				{
					System.out.println("Run failed");
					ex.printStackTrace();
				}
			}
		}

		public void sendFirstCard(String r) {
			try {
				dOut.writeUTF(r);
				dOut.flush();
			} catch (Exception e) {
				System.out.println("first card not received");
				e.printStackTrace();
			}
		}
		/*
		 * send the scores to other players
		 */
		public void sendPlayers(Player[] pl) {
			try {
				for (Player p : pl) {
					dOut.writeObject(p);
					dOut.flush();
				}

			} catch (IOException ex) {
				System.out.println("Score sheet not sent");
				ex.printStackTrace();
			}

		}

		/*
		 * receive scores of other players
		 */
		public void sendTurnNo(int r) {
			try {
				dOut.writeInt(r);
				dOut.flush();
			} catch (Exception e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			}
		}

		/*
		 * receive scores of other players
		 */
		public int[] receiveScores() {
			try {
				int[] sc = new int[15];
				for (int i = 0; i < 15; i++) {
					sc[i] = dIn.readInt();
				}
				return sc;
			} catch (Exception e) {
				System.out.println("Score sheet not received");
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * send scores of other players
		 */
		public void sendScores(Player[] pl) {
			try {
				for (int i = 0; i < pl.length; i++) {
					for (int j = 0; j < pl[i].getScoreSheet().length; j++) {
						dOut.writeInt(pl[i].getScoreSheet()[j]);
					}
				}
				dOut.flush();
			} catch (Exception e) {
				System.out.println("Score sheet not sent");
				e.printStackTrace();
			}
		}
		private String receiveTopCard() {
			try {
				return dIn.readUTF();
			} catch (Exception e) {
				System.out.println("top card not received");
				e.printStackTrace();
			}
			return "null";
		}

	}

}
