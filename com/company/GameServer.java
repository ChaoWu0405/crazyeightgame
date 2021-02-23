package com.company;

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
    int[] p1score = new int[3];
    int[] p2score = new int[10];
    int[] p3score = new int[10];

    Server[] playerServer = new Server[3];
    Player[] players = new Player[3];
    ArrayList<String> cardSpace = new ArrayList<String>(Arrays.asList("1C","2H","1D","2C","4H","6S","6C","JH","5D","KC","4C","3C"
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
            ss = new ServerSocket(1114);
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

            playerServer[0].sendFirstCard(cardSpace.get(rand));/////////////////////////
            cardSpace.remove(rand);

            while (turnsMade < maxTurns) {
                turnsMade++;
                // send the round number
                System.out.println("*****************************************");
                //sent 111111
                System.out.println("Round number " + turnsMade);
                playerServer[0].sendTurnNo(turnsMade);
                //int r = playerServer[0].receiveInt();
                //System.out.println(r);
                //if(r==1){break;}

                players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                System.out.println(players[0].topCard);
                playerServer[0].sendTurnNo(-10);
                int s = playerServer[0].receiveInt();
                System.out.println("Player 1 completed turn and their score is "+ s);
                p1score[0]= s;


               //sent2
                playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));///////////////

                playerServer[1].sendTurnNo(turnsMade);
                players[0].topCard.add(playerServer[1].receiveTopCard());///////////////////////
                System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                playerServer[1].sendTurnNo(-10);
                System.out.println("Player 2 completed turn and their score is " + playerServer[1].receiveInt());
                p1score[1]= s;

                 //sent3
                playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                playerServer[2].sendTurnNo(turnsMade);
                players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                playerServer[2].sendTurnNo(-10);
                System.out.println("Player 3 completed turn and their score is "+playerServer[2].receiveInt());
                p1score[2]= s;

                playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                System.out.println();
                for(int i=0;i< p1score.length;i++){
                    if(p1score[i]==0){
                        System.out.println(p1score);
                    }
                }

            }
            playerServer[0].sendTurnNo(-1);
            playerServer[1].sendTurnNo(-1);
            playerServer[2].sendTurnNo(-1);
        } catch (Exception e) {
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
        public int receiveInt() {
            try {
                return dIn.readInt();

            } catch (IOException e) {
                System.out.println("Int not received");
                e.printStackTrace();
            }
            return 0;
        }

        /*
         * receive scores of other players
         */

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

