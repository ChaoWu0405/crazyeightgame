package com.company;

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
    public int myscore =0;
    int r=0;


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
    public int[] playRound(int dieRoll) {
        Scanner myObj = new Scanner(System.in);
        int count = 0; // reroll 3 times
        int stop = 0;
        int count1=0;

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
                    card.remove(0);
                    break;

//                     System.out.println("choose rank:");
//                     System.out.println("(1) CLUB");
//                      System.out.println("(2) SPADE");
//                      System.out.println("(3) DIAMOND");
//                     System.out.println("(4) HEART");
//                     int choose = myObj.nextInt();
//                     if(choose==1){
//                         topCard.add("3C");
//                         topCard.remove(0);
//                     }
//                     else if(choose==2){
//                           topCard.add("3S");
//                           topCard.remove(0);
//                     }
//                     else if(choose==3){
//                         topCard.add("3D");
//                         topCard.remove(0);
//                      }
//                     else if(choose==4){
//                         topCard.add("3H");
//                         topCard.remove(0);
//                     }
//

                }
                if(topCard.get(0).charAt(0)=='2'){
                    if(game.isCardOk(card.get(index),topCard)&& count1!=2){
                        card.remove(index);
                        System.out.println("Hand Card:"+card);
                        count1++;
                    }
                }
                else{
                    System.out.println("before"+topCard);
                    if(topCard.size()>1){
                        topCard.remove(0);}
                    if(game.isCardOk(card.get(index),topCard)){
                        System.out.println("Top Card :");
                        System.out.println(topCard.get(0));
                        card.remove(index);
                        System.out.println("Hand Card:"+card);

                    //clientConnection.sendString("topcard");
                    ///if()
                        stop=-1;
                    }
                    else{
                        System.out.println("after"+topCard);
                        System.out.println("you can not push this card out");
                        System.out.println("Your Hand Card:");
                        System.out.println(card);
                        System.out.println("The card you need to follow:");
                        System.out.println(topCard.get(0));
                    }
                }
            }

            if (act == 2) {
                int c1=0;
                if(topCard.get(0).charAt(0)=='2'){
                    game.cardspace.remove(0);
                    card.add(game.cardspace.get(0));
                    game.cardspace.remove(0);
                    card.add(game.cardspace.get(0));
                    game.cardspace.remove(0);
                    topCard.add("3C");
                    topCard.remove(0);
                   // System.out.println(topCard);
                    System.out.println("new Hand card: "+card);
                }
                else {

                    for (int i = 0; i < card.size(); i++) {
                        if (game.isOk(card.get(i), topCard)) {
                            c1++;
                        }
                    }
                    if(topCard.size()>1){
                        topCard.remove(0);}
                    if (c1 == 0) {
                        int rand = (int) (Math.random() * (game.cardspace.size()-1) + 1);
                        card.add(game.cardspace.get(r));
                        game.cardspace.remove(r);
                        r++;
                        //card.add();
                        System.out.println("new hand card: " + card);
                        //System.out.println("Top card :"+topCard.get(0));
                        count++;
                        if (count == 3) {
                            System.out.println("you can not draw anything turn end");
                            stop = -1;
                        }

                    } else {
                        System.out.println("you can not draw card");
                        System.out.println(card);
                        System.out.println("top card: " + topCard.get(0));
                    }
                }

            }
            if(act==3){stop=-1;}

        }
        myscore =0;
       // card.forEach((n)->myscore = myscore + ((int)n.charAt(0)));

        for(int i=0;i< card.size();i++){
            if(card.get(i).length()==2){
                if(card.get(i).charAt(0)=='1'){
                    myscore = myscore +1;
                }
                if(card.get(i).charAt(0)=='2'){
                    myscore = myscore +2;
                }
                if(card.get(i).charAt(0)=='3'){
                    myscore = myscore +3;
                }
                if(card.get(i).charAt(0)=='4'){
                    myscore = myscore +4;
                }
                if(card.get(i).charAt(0)=='5'){
                    myscore = myscore +5;
                }
                if(card.get(i).charAt(0)=='6'){
                    myscore = myscore +6;
                }
                if(card.get(i).charAt(0)=='7'){
                    myscore = myscore +7;
                }
                if(card.get(i).charAt(0)=='8'){
                    myscore = myscore +50;
                }
                if(card.get(i).charAt(0)=='9'){
                    myscore = myscore +9;
                }
                if(card.get(i).charAt(0)=='J'){
                    myscore = myscore +10;
                }
                if(card.get(i).charAt(0)=='Q'){
                    myscore = myscore +10;
                }
                if(card.get(i).charAt(0)=='K'){
                    myscore = myscore +10;
                }
            }
            else{
                myscore = myscore + 10;
            }
        }
        System.out.println("MY SCORE IS @@@@@@@@@@@@@@@@@" + myscore);
        return this.scoreSheet;

    }




    /*
     * loop through the first 6 elements of the score sheet and return
     */


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


    public void startGame() {
        // receive players once for names
        players = clientConnection.receivePlayer();
        System.out.println("ready to play");

        for (int i=0;i<5;i++) {
            card.add(clientConnection.receiveFirstCard());
        }



        System.out.println("Hand card: "+card);
        //topCard.add(clientConnection.receiveFirstCard());
        //System.out.println("Top Card: ");
        //System.out.println(topCard);
        //clientConnection.sendString(topCard.get(0));

        while (true) {
            topCard.add(clientConnection.receiveFirstCard());
            System.out.println("top receive");
            System.out.println("Top card:");
            System.out.println(topCard.get(topCard.size()-1));
            if(topCard.get(topCard.size()-1).charAt(0) == '1'){
                System.out.println("now change direction to opposite(right)");
            }
//            if(topCard.get(topCard.size()-1).charAt(0) == '2'){
//                card.remove(0);
            if(topCard.get(topCard.size()-1).charAt(0) == 'Q'){
                //System.out.println("player 1 has been skipped");
            }
//
//            }
            System.out.println("Hand Card: ");
            System.out.println(card);
            int round = clientConnection.receiveRoundNo();
            if (round == -1)
                break;
            //System.out.println("\n \n \n ********Round Number " + round + "********");

            playRound(3);



            System.out.println("Top Card now is:");
            System.out.println(topCard.get(0));
            clientConnection.sendString(topCard.get(0));
            if(card.isEmpty()){
                System.out.println("winner");
                //clientConnection.sendInt(1);
                // clientConnection.sendString(getPlayer().name);
            }
            round = clientConnection.receiveRoundNo();
            if(round == -10){
                clientConnection.sendInt(myscore);
            }

        }

    }

    public boolean checkEmpty(){
        if(card.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }



    private class Client {
        Socket socket;
        private ObjectInputStream dIn;
        private ObjectOutputStream dOut;

        public Client() {
            try {
                socket = new Socket("localhost", 1114);
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

        public void sendInt(int r) {
            try {
                dOut.writeInt(r);
                dOut.flush();
            } catch (Exception e) {
                System.out.println("int not sent");
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
       // p.returnWinner();
        myObj.close();
    }
}