package com.a1.yahtzeeGame;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	Game game = new Game();

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/*
	 * test the winner between a list of three players
	 */
	public void testWinners() {
		Player[] pl = new Player[3];
		Player bob = new Player("Bob");
		Player alice = new Player("Alice");
		Player tim = new Player("Tim");
		bob.setScoreSheet(0, 50);
		alice.setScoreSheet(0, 10);
		tim.setScoreSheet(0, 5);

		pl[0] = bob;
		pl[1] = alice;
		pl[2] = tim;

		Player winner = game.getWinner(pl);
		assertEquals("Bob", winner.name);
	}

	/*
	 * for a given number and dice roll it tests the upper score
	 */
	public void testUpper() {
		int[] dieRoll = { 4, 5, 4, 2, 1 };
		int upperNo = 1;
		assertEquals(1, game.scoreUpper(dieRoll, upperNo));

		upperNo = 2;
		assertEquals(2, game.scoreUpper(dieRoll, upperNo));

		upperNo = 3;
		assertEquals(0, game.scoreUpper(dieRoll, upperNo));

		upperNo = 4;
		assertEquals(8, game.scoreUpper(dieRoll, upperNo));

		upperNo = 5;
		assertEquals(5, game.scoreUpper(dieRoll, upperNo));

		upperNo = 6;
		assertEquals(0, game.scoreUpper(dieRoll, upperNo));
	}

	/*
	 * tests if three of a kind
	 */
	public void testThreeOfAKind() {
		int[] dieRoll1 = { 4, 5, 4, 4, 1 }; // three of a kind
		assertTrue(game.isOfAKind(3, dieRoll1));

		int[] dieRoll2 = { 4, 5, 6, 4, 1 };
		assertFalse(game.isOfAKind(3, dieRoll2)); // not three of a kind

		int[] dieRoll3 = { 4, 4, 4, 4, 1 };
		assertTrue(game.isOfAKind(3, dieRoll3)); // 4 of a kind
	}

	/*
	 * tests if four of a kind
	 */
	public void testFourOfAKind() {
		int[] dieRoll = { 4, 5, 4, 4, 4 }; // four of a kind
		assertTrue(game.isOfAKind(4, dieRoll));

		int[] dieRoll2 = { 4, 5, 2, 4, 4 }; // three of a kind
		assertFalse(game.isOfAKind(4, dieRoll2));

		int[] dieRoll3 = { 4, 4, 4, 4, 4 }; // five of a kind
		assertTrue(game.isOfAKind(4, dieRoll3));
	}

	/*
	 * tests if sum of all die returns the right score
	 */
	public void testSumOfRoll() {
		int[] dieRoll = { 1, 2, 3, 4, 5 };
		assertEquals(15, game.sumOfRoll(dieRoll));
	}

	/*
	 * tests if its straight - small
	 */
	public void testSmallStraight() {
		int[] dieRoll = { 1, 4, 3, 2, 1 };
		assertTrue(game.isStraight("s", dieRoll));

		int[] dieRoll2 = { 1, 5, 3, 2, 1 }; // not a straight
		assertFalse(game.isStraight("s", dieRoll2));

		int[] dieRoll3 = { 1, 5, 3, 2, 4 }; // a large straight
		assertTrue(game.isStraight("s", dieRoll3));
	}

	/*
	 * tests if its straight - large
	 */
	public void testLargeStraight() {
		int[] dieRoll = { 6, 5, 3, 4, 2 };
		assertTrue(game.isStraight("l", dieRoll));

		int[] dieRoll2 = { 1, 4, 3, 2, 1 }; // not a large straight
		assertFalse(game.isStraight("l", dieRoll2));
	}

	/*
	 * tests if its a full house
	 */
	public void testFullHouse() {
		int[] dieRoll = { 2, 3, 3, 3, 2 };
		assertTrue(game.isFullHouse(dieRoll));

		int[] dieRoll2 = { 2, 3, 3, 5, 2 }; // not a full house
		assertFalse(game.isFullHouse(dieRoll2));

	}

	/*
	 * tests for a yahtzee
	 */
	public void testYahtzee() {
		int[] dieRoll = { 3, 3, 3, 3, 3 };
		assertTrue(game.isYahtzee(dieRoll));

		int[] dieRoll2 = { 3, 3, 3, 5, 4 };
		assertFalse(game.isYahtzee(dieRoll2));
	}

	/*
	 * tests for the right score of a three of a kind
	 */
	public void testThreeOfAKindScore() {
		int[] dieRoll = { 2, 3, 3, 3, 6 }; // three of a kind
		assertEquals(17, game.scoreThreeOfAKind(dieRoll));

		int[] dieRoll2 = { 2, 1, 3, 3, 6 }; // not three of a kind
		assertEquals(0, game.scoreThreeOfAKind(dieRoll2));
	}

	/*
	 * tests for the right score of a three of a kind
	 */
	public void testFourOfAKindScore() {
		int[] dieRoll = { 2, 3, 3, 3, 3 };
		assertEquals(14, game.scoreFourOfAKind(dieRoll));

		int[] dieRoll2 = { 2, 2, 3, 3, 3 };
		assertEquals(0, game.scoreFourOfAKind(dieRoll2));
	}

	/*
	 * tests for the right score of a full house
	 */
	public void testFullHouseScore() {
		int[] dieRoll = { 2, 3, 3, 3, 2 };
		assertEquals(25, game.scoreFullHouse(dieRoll));

		int[] dieRoll2 = { 2, 6, 3, 3, 2 };
		assertEquals(0, game.scoreFullHouse(dieRoll2));
	}

	/*
	 * test small straight score
	 */
	public void testSmallStraightScore() {
		int[] dieRoll = { 2, 3, 4, 5, 2 };
		assertEquals(30, game.scoreSmallStraight(dieRoll));

		int[] dieRoll2 = { 2, 3, 4, 6, 2 };
		assertEquals(0, game.scoreSmallStraight(dieRoll2));
	}

	/*
	 * test small straight score
	 */
	public void testLargeStraightScore() {
		int[] dieRoll = { 6, 3, 4, 5, 2 };
		assertEquals(40, game.scoreLargeStraight(dieRoll));

		int[] dieRoll2 = { 2, 3, 4, 6, 2 };
		assertEquals(0, game.scoreLargeStraight(dieRoll2));
	}

	/*
	 * test yahtzee score
	 */
	public void testYahtzeeScore() {
		int[] dieRoll = { 2, 2, 2, 2, 2 };
		assertEquals(50, game.scoreYahtzee(dieRoll));

		int[] dieRoll2 = { 2, 3, 4, 6, 2 };
		assertEquals(0, game.scoreYahtzee(dieRoll2));
	}

	/*
	 * test chance score
	 */
	public void testChanceScore() {
		int[] dieRoll = { 1, 2, 3, 4, 5 };
		assertEquals(15, game.scoreChance(dieRoll));

		int[] dieRoll2 = { 6, 2, 3, 4, 5 };
		assertEquals(20, game.scoreChance(dieRoll2));
	}

	/*
	 * test bonus scores for upper if 63 add 35
	 */
	public void testUpperBonus() {
		Player p1 = new Player("test");
		p1.setScoreSheet(0, 6);
		p1.setScoreSheet(1, 12);
		p1.setScoreSheet(2, 18);
		p1.setScoreSheet(3, 24);
		p1.setScoreSheet(4, 30);
		p1.setScoreSheet(5, 36);
		assertEquals(35, game.upperBonus(p1.getUpperScore()));

		p1.setScoreSheet(0, 13);
		p1.setScoreSheet(1, 10);
		p1.setScoreSheet(2, 10);
		p1.setScoreSheet(3, 10);
		p1.setScoreSheet(4, 10);
		p1.setScoreSheet(5, 10);

		assertEquals(35, game.upperBonus(p1.getUpperScore()));

		p1.setScoreSheet(0, 5);
		p1.setScoreSheet(1, 5);
		p1.setScoreSheet(2, 5);
		p1.setScoreSheet(3, 5);
		p1.setScoreSheet(4, 5);
		p1.setScoreSheet(5, 5);
		assertEquals(0, game.upperBonus(p1.getUpperScore()));
	}

	/*
	 * test yahtzee bonus
	 */
	public void testYahtzeeBonus() {
		Player p1 = new Player("test");

		int[] dieRoll = { 1, 1, 1, 1, 1 };
		p1.setScoreSheet(11, 100);
		assertEquals(100, game.yahtzeeBonus(p1.getScoreSheet(), dieRoll));

		int[] dieRoll2 = { 1, 1, 1, 1, 1 };
		p1.setScoreSheet(11, -1);
		assertEquals(0, game.yahtzeeBonus(p1.getScoreSheet(), dieRoll2));

		int[] dieRoll3 = { 4, 1, 6, 1, 5 };
		p1.setScoreSheet(11, 100);
		assertEquals(0, game.yahtzeeBonus(p1.getScoreSheet(), dieRoll3));

	}

	/*
	 * checks if the total score is the sum of the upper and lower score
	 */
	public void testScore() {
		Player p1 = new Player("test");
		p1.setScoreSheet(0, 100);
		assertEquals(100, p1.getScore());

		p1.setScoreSheet(13, 100);
		assertEquals(200, p1.getScore());

		p1.setScoreSheet(14, 100);
		assertEquals(300, p1.getScore());

	}

//	How would i test rerolls? 
//	check the index position to see that the ones held are not changed

	public void testReroll() {
		int[] dieRoll = { 1, 2, 3, 4, 5 };
		String[] held = { "1" };
		dieRoll = game.reRollNotHeld(dieRoll, held);
		assertEquals(1, dieRoll[0]);

		dieRoll[1] = 1;
		String[] held2 = { "2" };
		dieRoll = game.reRollNotHeld(dieRoll, held2);
		assertEquals(1, dieRoll[1]);

		dieRoll[2] = 1;
		String[] held3 = { "3" };
		dieRoll = game.reRollNotHeld(dieRoll, held3);
		assertEquals(1, dieRoll[2]);

		dieRoll[3] = 1;
		String[] held4 = { "4" };
		dieRoll = game.reRollNotHeld(dieRoll, held4);
		assertEquals(1, dieRoll[3]);

		dieRoll[4] = 1;
		String[] held5 = { "5" };
		dieRoll = game.reRollNotHeld(dieRoll, held5);
		assertEquals(1, dieRoll[4]);

		dieRoll[0] = 1;
		dieRoll[1] = 2;
		String[] held6 = { "1", "2" };
		dieRoll = game.reRollNotHeld(dieRoll, held6);
		assertEquals(1, dieRoll[0]);
		assertEquals(2, dieRoll[1]);
	}

	/*
	 * test print die roll
	 */
	public void testPrint() {
		int[] dieRoll = { 1, 2, 3, 4, 5 };
		game.printDieRoll(dieRoll);
		Player p = new Player("name");
		game.printScoreSheet(p);
	}

	public void testRound() {
		Player p = new Player("test");
		game.printScoreSheet(p);
		int[] dieRoll = game.rollDice();
		//p.playRound(dieRoll);
		game.printScoreSheet(p);
	}
}
