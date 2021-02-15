@tag
Feature: Starting the game by connecting to the server
  I want to use this test connecting to the server

@tag1
  Scenario: Player receives scores from server
  	Given Server is on
  	And Player connects to server at port 3001
  	When Player starts the game
  	Then Player receives player id
  	And Player receives scores of other players 
 
 