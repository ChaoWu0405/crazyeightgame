package com.a1.yahtzeeGame;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.TestCase;

public class StepDefSinglePlayer extends TestCase {
	int[] dieRoll = new int[5];
	int[] rerolled = new int[5];
	int score;
	Game game = new Game();
	Player p;

	@Given("Roll is {int} {int} {int} {int} {int}")
	public void roll_is(Integer int1, Integer int2, Integer int3, Integer int4, Integer int5) {
		dieRoll[0] = int1;
		dieRoll[1] = int2;
		dieRoll[2] = int3;
		dieRoll[3] = int4;
		dieRoll[4] = int5;
	}

	/*
	 * Lower scoring
	 */
	@When("Player wants to score it as three of a kind")
	public void player_wants_to_score_it_as_three_of_a_kind() {
		score = game.scoreThreeOfAKind(dieRoll);
	}

	@Then("{int} is returned")
	public void is_returned(Integer expScore) {
		assertTrue(expScore == score);
	}

	@When("Player wants to score it as four of a kind")
	public void player_wants_to_score_it_as_four_of_a_kind() {
		score = game.scoreFourOfAKind(dieRoll);
	}

	@When("Player wants to score it as full house")
	public void player_wants_to_score_it_as_full_house() {
		score = game.scoreFullHouse(dieRoll);
	}

	@When("Player wants to score it as small straight")
	public void player_wants_to_score_it_as_small_straight() {
		score = game.scoreSmallStraight(dieRoll);
	}

	@When("Player wants to score it as large straight")
	public void player_wants_to_score_it_as_large_straight() {
		score = game.scoreLargeStraight(dieRoll);
	}

	@When("Player wants to score it as yahtzee")
	public void player_wants_to_score_it_as_yahtzee() {
		score = game.scoreYahtzee(dieRoll);
	}

	@When("Player wants to score it as chance")
	public void player_wants_to_score_it_as_chance() {
		score = game.scoreChance(dieRoll);
	}

	/*
	 * upper scoring
	 */
	@When("Player wants to score it as upper {int}")
	public void player_wants_to_score_it_as_upper(Integer int1) {
		score = game.scoreUpper(dieRoll, int1);
	}

	/*
	 * bonuses
	 */
	@Given("A yahtzee bonus is already scored")
	public void a_yahtzee_bonus_is_already_scored() {
		p = new Player("test");
		p.setScoreSheet(11, 50);
	}

	@When("Roll is a yahtzee")
	public void roll_is_a_yahtzee() {
		dieRoll[0] = 6;
		dieRoll[1] = 6;
		dieRoll[2] = 6;
		dieRoll[3] = 6;
		dieRoll[4] = 6;
	}

	@Then("A lower bonus is added")
	public void a_lower_bonus_is_added() {
		System.out.println(p.getScoreSheet()[11] > 0);
		assertTrue(game.yahtzeeBonus(p.getScoreSheet(), dieRoll) == 100);
	}

	@Given("A yahztee bonus is not scored")
	public void a_yahztee_bonus_is_not_scored() {
		p = new Player("test");
		p.setScoreSheet(11, -1);
	}

	@Then("No lower bonus is added")
	public void no_lower_bonus_is_added() {
		assertTrue(0 == game.yahtzeeBonus(p.getScoreSheet(), dieRoll));
	}

	@Given("Upper score is more than sixty three")
	public void upper_score_is_more_than_sixty_three() {
		p = new Player("test");
		p.setScoreSheet(0, 6);
		p.setScoreSheet(1, 12);
		p.setScoreSheet(2, 18);
		p.setScoreSheet(3, 24);
		p.setScoreSheet(4, 30);
		p.setScoreSheet(5, 36);
	}

	@When("The game ends")
	public void the_game_ends() {
		// assume the game ends rounds == 13
	}

	@Then("An upper Bonus is added")
	public void an_upper_Bonus_is_added() {
		assertTrue(game.upperBonus(p.getUpperScore()) == 35);
	}

	@Given("Upper score is less than sixty three")
	public void upper_score_is_less_than_sixty_three() {
		p = new Player("test");
		p.setScoreSheet(0, 5);
		p.setScoreSheet(1, 5);
		p.setScoreSheet(2, 5);
		p.setScoreSheet(3, 5);
		p.setScoreSheet(4, 5);
		p.setScoreSheet(5, 5);
	}

	@Then("No upper Bonus is added")
	public void no_upper_Bonus_is_added() {
		assertTrue(game.upperBonus(p.getUpperScore()) == 0);
	}

	/*
	 * rerolling
	 */
	@Given("A random dice roll")
	public void a_random_dice_roll() {
		dieRoll = game.rollDice();
	}

	@When("Player wants to hold {string} and reroll")
	public void player_wants_to_hold_and_reroll(String string) {
		String[] held = string.replaceAll("\\s", "").split(",");
		rerolled = game.reRollNotHeld(dieRoll, held);
	}

	@Then("The die {string} do not change")
	public void the_die_do_not_change(String string) {
		String[] held = string.replaceAll("\\s", "").split(",");
		for (String s : held) {
			int i = Integer.parseInt(s);
			assertTrue(rerolled[i] == dieRoll[i]);
		}

	}

}
