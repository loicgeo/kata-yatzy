Feature: Yatzy CHANCE roll scoring

  @nominal
  Scenario Outline: Chance a roll

    Given a dice serie "<DICE_SERIE>"
    And a roll type "<ROLL>"
    When Yatzy is scored
    Then the score is <SCORE>

    Examples:
      | DICE_SERIE | ROLL   | SCORE |
      | 1,1,3,3,6  | CHANCE | 14    |
      | 4,5, 5,6,1 | CHANCE | 21    |
      | 4 ,5,5,6,1 | CHANCE | 21    |
