@tag
Feature: Test features for an end to end game
  I want to use this to test functions for an end to end game
  
  @tag1
   Scenario: Play an entire game 
  	Given Server is on
  	And Player connects to server at port 3002
  	When Player plays a round 
  	Then Player receives scores of other players before it
 
 
