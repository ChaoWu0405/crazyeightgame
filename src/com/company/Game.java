package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Hello world!
 *
 */
public class Game implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    ArrayList<String> cardspace = new ArrayList<String>(Arrays.asList("9H","1C","2C","3C","3S","5H","3S","2S","3C","3D","2D"
            ,"4C","4D","5H","5S","6H","7C","7H","8C","8D","8S","9C","9H","10C","10H","10S",
            "JC","JS","QC","QD","QS","KD","KS"));
//	private ArrayList<String> scoreSheetKey = new ArrayList<String>(Arrays.asList("one", "two", "three", "four", "five",
//			"six", "3ok", "4ok", "full", "sst", "lst", "yahtzee", "chance", "bonus"));


    public boolean isCardOk(String handCard, ArrayList<String> followCard) {
        if (handCard.length() == 2 && followCard.get(0).length() == 2) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char c0 = handCard.charAt(0);

            char c1 = handCard.charAt(1);
            if (c0 == f0 || c1 == f1) {
                followCard.add(handCard);
                followCard.remove(0);
                return true;
            }
        }
        if (handCard.length() == 2 && followCard.get(0).length() == 3) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char f3 = followCard.get(0).charAt(2);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            if (c1 == f3 || c0 == '8') {
                followCard.add(handCard);
                followCard.remove(0);
                return true;
            }
        }

        if (handCard.length() == 3 && followCard.get(0).length() == 2) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            char c2 = handCard.charAt(2);
            if (c0 == f0 && c1 == f1 || c0 == '8' || c2 == f1) {
                followCard.add(handCard);
                followCard.remove(0);
                return true;
            }
        }
        if (handCard.length() == 3 && followCard.get(0).length() == 3) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char f3 = followCard.get(0).charAt(2);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            char c3 = handCard.charAt(2);
            if (c0 == f0 && c1 == f1 || c0 == '8' || c3 == f3) {
                followCard.add(handCard);
                followCard.remove(0);
                return true;
            }
        }


        return false;
    }

    public boolean isOk(String handCard, ArrayList<String> followCard) {
        if (handCard.length() == 2 && followCard.get(0).length() == 2) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            if (c0 == f0 || c1 == f1) {
//                followCard.add(handCard);
//                followCard.remove(0);
                return true;
            }
        }
        if (handCard.length() == 2 && followCard.get(0).length() == 3) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char f3 = followCard.get(0).charAt(2);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            if (c1 == f3 || c0 == '8') {
//                followCard.add(handCard);
//                followCard.remove(0);
                return true;
            }
        }

        if (handCard.length() == 3 && followCard.get(0).length() == 2) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            char c2 = handCard.charAt(2);
            if (c0 == f0 && c1 == f1 || c0 == '8' || c2 == f1) {
//                followCard.add(handCard);
//                followCard.remove(0);
                return true;
            }
        }
        if (handCard.length() == 3 && followCard.get(0).length() == 3) {
            char f0 = followCard.get(0).charAt(0);
            char f1 = followCard.get(0).charAt(1);
            char f3 = followCard.get(0).charAt(2);
            char c0 = handCard.charAt(0);
            char c1 = handCard.charAt(1);
            char c3 = handCard.charAt(2);
            if (c0 == f0 && c1 == f1 || c0 == '8' || c3 == f3) {
//                followCard.add(handCard);
//                followCard.remove(0);
                return true;
            }
        }


        return false;
    }

    //check if you can take a card from the card space
    public boolean takeACard(ArrayList<String> handCard, String followCard) {
        for (int i = 0; i < handCard.size(); i++) {
            if (handCard.get(i).charAt(0) != followCard.charAt(0) && handCard.get(i).charAt(1) != followCard.charAt(1) &&
                    handCard.get(i).charAt(0) != '8') {
                return true;
            } else {
                break;
            }
        }
        return false;

    }

}