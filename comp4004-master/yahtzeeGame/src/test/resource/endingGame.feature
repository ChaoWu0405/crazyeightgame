@tag
Feature: Ending the game
  I want to use this to test ending the game

  @gameEnds
  Scenario: Game ends
    Given Server is on
    And Player connects to server
    When All rounds are complete
    Then Player receives complete scores with added bonus for all players
    And Player receives winner
