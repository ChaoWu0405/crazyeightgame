@tag
Feature: Test single player components 
  I want to use this feature to test single player functions  
		
  @threeOfAKind
  Scenario Outline: Score three of a kind
  	Given Roll is <roll>
    When Player wants to score it as three of a kind
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 1 1 1 1 		|	5			|
  		|1 1 1 1 2 		|	6			|
  		|1 1 1 2 2 		|	7			|
  		|1 1 2 3 4 		|	0			|
  		|1 2 3 4 5 		|	0			|
  		
  		
	@fourOfAKind
  Scenario Outline: Score four of a kind
  	Given Roll is <roll>
    When Player wants to score it as four of a kind
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 1 1 1 1 		|	5			|
  		|1 1 1 1 2 		|	6			|
  		|1 1 1 2 3 		|	0			|
  		|1 1 2 3 4 		|	0			|
  		|1 2 3 4 5 		|	0			|
  		
  
  @fullHouse
  Scenario Outline: Score full house
  	Given Roll is <roll>
    When Player wants to score it as full house
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 1 1 2 2 		|	25		|
  		|1 1 1 1 1 		|	25		|
  		|1 2 3 4 5 		|	0			|
  		

  @smallStraight
  Scenario Outline: Score small straight
  	Given Roll is <roll>
    When Player wants to score it as small straight
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 2 3 1 2		|	0 		|
  		|1 2 3 4 5 		|	30		|
  		|1 2 3 4 5 		|	30		|
  		|1 3 3 3 3 		|	0 		|
 
  
  @largeStraight
  Scenario Outline: Score large straight
  	Given Roll is <roll>
    When Player wants to score it as large straight
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 2 3 1 2 		|	0 		|
  		|1 2 3 4 1 		|	0 		|
  		|1 2 3 4 5 		|	40		|
  		|1 2 3 3 5 		|	0 		|


  @yahtzee
  Scenario Outline: Score yahtzee
  	Given Roll is <roll>
    When Player wants to score it as yahtzee
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 1 1 1 1 		|	50 		|
  		|1 2 3 4 1 		|	0 		|
  		|1 2 3 4 4 		|	0 		|
  		|5 5 5 5 5 		|	50		|


	 @chance
  Scenario Outline: Score chance
  	Given Roll is <roll>
    When Player wants to score it as chance
    Then <score> is returned 
  	Examples:
  		|roll					| score	| 
  		|1 2 3 4 5 		|	15		|
  		|1 1 1 1 1 		|	5 		|
  		|6 6 6 6 6 		|	30		|


  @upper
  Scenario Outline: Score upper
  	Given Roll is <roll>
    When Player wants to score it as upper <number>
    Then <score> is returned 
  	Examples:
  		|roll					| number	| score	| 
  		|1 1 1 1 1 		| 1				|	5 		|
  		|2 4 3 4 5 		| 1				|	0 		|
			|2 2 2 2 2 		| 2				|	10 		|
  		|6 4 3 4 5 		| 2				|	0 		|
			|3 3 3 3 3 		| 3				|	15 		|
  		|6 4 2 4 5 		| 3				|	0 		|
  		|4 4 4 4 4 		| 4				|	20 		|
  		|6 6 2 1 5 		| 4				|	0 		|
  		|5 5 5 5 5 		| 5				|	25 		|
  		|6 6 2 1 1 		| 5				|	0 		|
  		|6 6 6 6 6 		| 6				|	30 		|
  		|3 4 2 1 5 		| 6				|	0 		|
  		  					
	
	@yahtzeeBonus
	Scenario: Yahtzee bonus 
	Given A yahtzee bonus is already scored
	When Roll is a yahtzee
	Then A lower bonus is added
	
	
	@noYahtzeeBonus
	Scenario: No yahtzee bonus
	Given A yahztee bonus is not scored
	When Roll is a yahtzee
	Then No lower bonus is added 
	
	
	@UpperBonus 
	Scenario: Upper bonus
	Given Upper score is more than sixty three
	When The game ends 
	Then An upper Bonus is added  
	
	@NoUpperBonus 
	Scenario: No Upper bonus
	Given Upper score is less than sixty three
	When The game ends 
	Then No upper Bonus is added 
	
	@rerollDice
	Scenario Outline: Reroll dice
	Given A random dice roll
	When Player wants to hold <held> and reroll
	Then The die <held> do not change
	Examples:
	| held					|
	| "1"						|
	| "1,2"					|
	| "1, 2"				|
	| "1, 2,  3"		|
	|	"1, 2, 3, 4"	|
	
	
	
		
  		