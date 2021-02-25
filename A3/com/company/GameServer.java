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



    Server[] playerServer = new Server[3];
    Player[] players = new Player[3];
    ArrayList<String> cardSpace = new ArrayList<String>(Arrays.asList("1C","1D","1H","1S","2C","2D","2H","2S","3C","3D","3H","3S"
            ,"4C","4D","4H","4S","5C","5D","5H","5S","6C","6D","6H","6S","7C","7D","7H","7S","9C","9D","9H","10C","10D","10H","10S",
            "JC","JD","JH","JS","QC","QD","QH","QS","KC","KD","KH","KS"));

    ServerSocket ss;

    Game game = new Game();
    int numPlayers;
    int p1;
    int p2;
    int p3;

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
                playerServer[0].sendFirstCard(cardSpace.get(rand));
                //playerServer[0].sendFirstCard(cardSpace.get(0));
                cardSpace.remove(0);
                //cardSpace.remove(rand);
                System.out.println("player1 draw a card");

                int rand2 = (int) (Math.random() * (cardSpace.size()-1) + 1);
                playerServer[1].sendFirstCard(cardSpace.get(rand2));
                cardSpace.remove(0);
                System.out.println("player2 draw a card");

                int rand3 = (int) (Math.random() * (cardSpace.size()-1) + 1);
                playerServer[2].sendFirstCard(cardSpace.get(rand3));
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
                System.out.println("turn number " + turnsMade);
                playerServer[0].sendTurnNo(turnsMade);
                players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                playerServer[0].sendTurnNo(-10);
                p1 = playerServer[0].receiveInt();
                System.out.println("Player 1 completed turn and their score is "+ p1);
                if(p1==0){
                    System.out.println("p1 score:"+ p1+" p2 score: "+p2+" p3 score: "+p3);
                    System.exit(-1);
                }
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='Q'){
                    System.out.println(players[0].topCard);
                    System.out.println("player 2 has been skipped");
                    while(turnsMade< maxTurns){
                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[2].sendTurnNo(-10);
                        p3 = playerServer[2].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);

                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[0].sendTurnNo(-10);
                        p3 = playerServer[0].receiveInt();

                        System.out.println("Player 1 completed turn and their score is "+p1);
                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(-10);
                        p1 = playerServer[1].receiveInt();
                        System.out.println("Player 2 completed turn and their score is "+ p2);
                    }
                }
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='1' && players[0].topCard.get(players[0].topCard.size()-1).length()==2){
                    System.out.println(players[0].topCard);
                    System.out.println("now direction change to : player3 -> player2 -> player1 (going right)");
                    while(turnsMade< maxTurns){
                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[2].sendTurnNo(-10);
                        p3 = playerServer[2].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);

                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[1].sendTurnNo(-10);
                        p3 = playerServer[1].receiveInt();

                        System.out.println("Player 2 completed turn and their score is "+p2);
                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(-10);
                        p1 = playerServer[0].receiveInt();
                        System.out.println("Player 1 completed turn and their score is "+ p1);
                    }
                }





               //sent2
                playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));///////////////

                playerServer[1].sendTurnNo(turnsMade);
                players[0].topCard.add(playerServer[1].receiveTopCard());///////////////////////
                System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                playerServer[1].sendTurnNo(-10);
                p2 = playerServer[1].receiveInt();
                System.out.println("Player 2 completed turn and their score is " + p2);
                if(p2==0){
                    System.out.println("p1 score:"+ p1+" p2 score: "+p2+" p3 score: "+p3);
                    System.out.println("player 2 is the winner!");
                    System.exit(-1);
                }
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='Q'){
                    System.out.println(players[0].topCard);
                    System.out.println("player 2 has been skipped");
                    while(turnsMade< maxTurns){
                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[0].sendTurnNo(-10);
                        p3 = playerServer[0].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);

                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[1].sendTurnNo(-10);
                        p3 = playerServer[1].receiveInt();

                        System.out.println("Player 1 completed turn and their score is "+p1);
                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(-10);
                        p1 = playerServer[2].receiveInt();
                        System.out.println("Player 2 completed turn and their score is "+ p2);
                    }
                }
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='1' && players[0].topCard.get(players[0].topCard.size()-1).length()==2){
                    System.out.println(players[0].topCard);
                    System.out.println("now direction change to : player3 -> player2 -> player1 (going right)");
                    while(turnsMade< maxTurns){
                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[0].sendTurnNo(-10);
                        p3 = playerServer[0].receiveInt();

                        System.out.println("Player 1 completed turn and their score is "+p1);

                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[2].sendTurnNo(-10);
                        p3 = playerServer[2].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);
                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[1].sendTurnNo(-10);
                        p1 = playerServer[1].receiveInt();
                        System.out.println("Player 2 completed turn and their score is "+ p2);
                    }
                }



                 //sent3
                playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                playerServer[2].sendTurnNo(turnsMade);
                players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                playerServer[2].sendTurnNo(-10);
                p3 = playerServer[2].receiveInt();
                System.out.println("Player 3 completed turn and their score is "+p3);
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='Q'){
                    System.out.println(players[0].topCard);
                    System.out.println("player 1 has been skipped");
                    while(turnsMade< maxTurns){
                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[1].sendTurnNo(-10);
                        p3 = playerServer[1].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);

                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[2].sendTurnNo(-10);
                        p3 = playerServer[2].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);
                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(-10);
                        p1 = playerServer[0].receiveInt();
                        System.out.println("Player 1 completed turn and their score is "+ p1);
                    }
                }
                if(players[0].topCard.get(players[0].topCard.size()-1).charAt(0)=='1' && players[0].topCard.get(players[0].topCard.size()-1).length()==2){
                    System.out.println(players[0].topCard);
                    System.out.println("now direction change to : player3 -> player2 -> player1 (going right)");
                    while(turnsMade< maxTurns){
                        playerServer[1].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));/////////
                        playerServer[1].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[1].sendTurnNo(-10);
                        p3 = playerServer[1].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);

                        playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[0].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[2].receiveTopCard());/////////////////////
                        playerServer[0].sendTurnNo(-10);
                        p3 = playerServer[0].receiveInt();

                        System.out.println("Player 3 completed turn and their score is "+p3);
                        playerServer[2].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(turnsMade);
                        players[0].topCard.add(playerServer[0].receiveTopCard());//////////////////
                        System.out.println(players[0].topCard.get(players[0].topCard.size()-1));
                        playerServer[2].sendTurnNo(-10);
                        p1 = playerServer[2].receiveInt();
                        System.out.println("Player 1 completed turn and their score is "+ p1);
                    }
                }


                playerServer[0].sendFirstCard(players[0].topCard.get(players[0].topCard.size()-1));
                System.out.println();

                if(p1==0 || p2==0 || p3==0){
                    System.out.println("p1 score:"+ p1+" p2 score: "+p2+" p3 score: "+p3);
                    System.exit(-1);
                }

                System.out.println("p1 score:"+ p1+" p2 score: "+p2+" p3 score: "+p3);
               // System.exit(-1);

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

