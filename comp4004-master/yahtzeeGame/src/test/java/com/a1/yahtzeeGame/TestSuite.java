package com.a1.yahtzeeGame;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions() // features = "src/test/java/com/a1/yahtzeeGame/endingGame.feature")
@Suite.SuiteClasses({ ServerTest.class })

public class TestSuite {

}
