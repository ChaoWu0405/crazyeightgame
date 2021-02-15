@tag
Feature: Play a game and test the components from one player  
  I want to use this to test the functionality of playing one game by a player

  @tag1
  Scenario Outline: Scoring in the right slot
  Given A die roll
  When Player adds score at <position>
  Then Score sheet is updated at <position>
  Examples:
  	|position	|
  	| 1				|
  	| 2				|
  	| 3				|
  	| 4				|
  	| 5				|
  	| 6				|
  	| 7				|
  	| 8				|
  	| 9 			|
  	| 10			|
  	| 11			|
  	| 12			|
  	| 13			| 


	@tag2
	Scenario Outline: Player rerolls
	Given A fixed die roll
	And A round is being played
	When Player chooses to hold <hold> and reroll  
	And Player scores the new roll at <pos>
	Then Scores at <pos> should be greater than <scores>
	Examples:
		| hold				| scores	| pos	|
		|	"1"					|	1				| 1		|
		|	"1,2"				|	2				| 1		|
		|	"1,2,3"			|	3				|	1 	|
		|	"1,2,3,4"		|	4				|	1 	|
		|	"1,2,3,4,5"	|	4				|	1 	|

	@tag3
	Scenario Outline: Player rerolls all
	Given A fixed die roll
	When Player chooses to reroll all
	And Player scores the new roll at <pos>
	Then Score sheet is updated at <pos>
	Examples:
		| pos			| 
		|	1				|
		|	3				|
		|	5				|
		
	
	@tag4
	Scenario: Player cannot reroll more than three times
	Given A die roll
	And A round is being played
	When Player rerolls three times
	Then Player cannot reroll again
	And Player scores the new roll at 1
	And Score sheet is updated at 1


	@tag5
	Scenario: Player cannot reroll all more than three times
	Given A die roll
	And A round is being played
	When Player rerolls all three times
	Then Player cannot reroll all again
	And Player scores the new roll at 1
	And Score sheet is updated at 1

		
	@tag6
	Scenario Outline: Player cannot score in the same slot twice
	Given A die roll
	And A round is being played
	And <slot> is already filled
	When Player scores again in <slot>
	Then Player scores the new roll at <new slot>
	And  Score sheet is updated at 1
	Examples:
		| slot 	| new slot	|
		|	1			| 2					|
		| 3			|	5					|
		|	12		| 4					|
	
	
	@tag7
	Scenario Outline: Player rerolls twice
	Given A fixed die roll
	And A round is being played
	When Player chooses to hold <hold> and reroll
	And Player chooses to hold <hold2> and reroll again 
	And Player scores the new roll at <pos>
	Then Score sheet is updated at <pos>
	Examples:
		| hold					| hold2				| pos	|
		|	"1"						| "1"					| 1		|
		|	"1"						| "1,2"				| 1		|
		|	"1"						| "1,2,3"			| 1		|
		|	"1"						| "1,2,3,4"		| 1		|
		|	"1,2"					| "1"					| 1		|
		|	"1,2"					| "1,2"				| 1		|
		|	"1,2"					| "1,2,3"			| 1		|
		|	"1,2"					| "1,2,3,4"		| 1		|
		|	"1,2,3"				| "1"					| 1		|
		|	"1,2,3"				| "1,2"				| 1		|
		|	"1,2,3"				| "1,2,3"			| 1		|
		|	"1,2,3"				| "1,2,3,4"		| 1		|
		|	"1,2,3,4"			| "1"					| 1		|
		|	"1,2,3,4"			| "1,2"				| 1		|
		|	"1,2,3,4"			| "1,2,3"			| 1		|
		|	"1,2,3,4"			| "1,2,3,4"		| 1		|				
		
  


	