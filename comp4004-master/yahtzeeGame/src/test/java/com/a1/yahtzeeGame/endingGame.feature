@tag
Feature: Ending the game
  I want to use this to test ending the game

  @tag1
  Scenario: Game ends 
    Given Server is on
  	And Player connects to server at port 3000
  	When All rounds are complete
  	Then Player receives complete scores with added bonus for all players 
  	And Player receives winner 
  	
  
  