package com.a1.yahtzeeGame;

import java.io.ByteArrayInputStream;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.TestCase;

public class StepDefSingleGamePlay extends TestCase {
	Game game = new Game();
	Player p = new Player("test");
	int[] scoreSheet = new int[15];
	int[] dieRoll = new int[5];
	String inString = "";

	@Given("A die roll")
	public void A_die_roll() {
		dieRoll = game.rollDice();
	}

	@When("Player adds score at {int}")
	public void player_adds_score_at(Integer int1) {
		scoreSheet = p.scoreRound(int1, dieRoll);
	}

	@Then("Score sheet is updated at {int}")
	public void score_sheet_is_updated_at(Integer int1) {
		assertFalse(scoreSheet[int1 - 1] == -1);
	}

	@Given("A round is being played")
	public void a_round_is_being_played() {
		p = new Player("test");
		inString = "";
	}

	@When("Player chooses to hold {string} and reroll")
	public void player_chooses_to_hold_and_reroll(String string) {
		inString = "1" + System.lineSeparator() + string;
	}

	@When("Player chooses to hold {string} and reroll again")
	public void player_chooses_to_hold_and_reroll_again(String string) {
		inString = System.lineSeparator() + "1" + System.lineSeparator() + string;

	}

	@When("Player chooses to reroll all")
	public void player_chooses_to_reroll_all() {
		inString = "2";
	}

	@When("Player scores the new roll at {int}")
	public void Player_scores_the_new_roll_at(Integer int1) {
		inString += System.lineSeparator() + "3" + System.lineSeparator() + int1;
		ByteArrayInputStream in = new ByteArrayInputStream((inString).getBytes());
		System.setIn(in);
		p.playRound(dieRoll);
	}

	@When("Player rerolls three times")
	public void player_rerolls_three_times() {
		inString = "1" + System.lineSeparator() + "1" + System.lineSeparator();
		inString += "1" + System.lineSeparator() + "2" + System.lineSeparator();
		inString += "1" + System.lineSeparator() + "3" + System.lineSeparator();
	}

	@Then("Player cannot reroll again")
	public void player_cannot_reroll_again() {
		// trying to reroll again should just loop back into the while loop
		inString += "1";
	}

	@When("Player rerolls all three times")
	public void player_rerolls_all_three_times() {
		inString = "2" + System.lineSeparator();
		inString += "2" + System.lineSeparator();
		inString += "2" + System.lineSeparator();
	}

	@Then("Player cannot reroll all again")
	public void player_cannot_reroll_all_again() {
		// trying to reroll again should just loop back into the while loop
		inString += "2";
	}

	@Given("{int} is already filled")
	public void is_already_filled(Integer int1) {
		p.setScoreSheet(int1, 50);

	}

	@When("Player scores again in {int}")
	public void player_scores_again_in(Integer int1) {
		inString = "3" + System.lineSeparator() + int1;
	}

	@Given("A fixed die roll")
	public void a_fixed_die_roll() {
		dieRoll[0] = 1;
		dieRoll[1] = 1;
		dieRoll[2] = 1;
		dieRoll[3] = 1;
		dieRoll[4] = 1;
	}

	@Then("Scores at {int} should be greater than {int}")
	public void scores_at_should_be_greater_than(Integer pos, Integer int2) {
		System.out.println(p.getScoreSheet()[pos - 1]);
		assertTrue(p.getScoreSheet()[pos - 1] >= int2);
	}

}
