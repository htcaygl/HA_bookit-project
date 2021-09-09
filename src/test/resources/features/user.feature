@smoke
Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then status code should be 200

  @db
  Scenario: verify information about logged user from api and database
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then the information about current user from api and database should match

   @db
  Scenario: three point verification(UI,DATABASE,API)
    Given user logs in using "sbirdbj@fc2.com" "asenorval"
    When user is on the my self page
    And I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    And I get the current user information from api
    Then UI,API and Database user information must be match

  @wip @db
  Scenario Outline:  three point verification(UI,DATABASE,API) DDF
    Given user logs in using "<email>" "<password>"
    When user is on the my self page
    And I logged Bookit api using "<email>" and "<password>"
    And I get the current user information from api
    Then UI,API and Database user information must be match

    Examples:
      | email                | password     |
      | sbirdbj@fc2.com      | asenorval    |
      | ccornil1h@usnews.com | corniecornil |

    #get name,role,team,batch,campus information from ui,database and api, compare them
    #you might get in one shot from ui and database, but might need multiple api requests to get those information