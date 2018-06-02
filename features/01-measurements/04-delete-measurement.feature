Feature: Delete a measurement
  In order to remove incorrect measurements
  I want to be able to delete a measurement taken at a specific time

  Background:
    Given I have submitted new measurements as follows:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |
      | "2015-09-01T16:10:00.000Z" | 27.3        | 16.9     | 0             |

  Scenario: Delete a specific measurement
    # DELETE /measurements/2015-09-01T16:00:00.000Z
    When I delete the measurement for "2015-09-01T16:00:00.000Z"
    Then the response has a status code of 204
    And the measurement for "2015-09-01T16:00:00.000Z" does not exist
    But the measurement for "2015-09-01T16:10:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:10:00.000Z" | 27.3        | 16.9     | 0             |

  Scenario: Delete a measurement that does not exist
    # DELETE /measurements/2015-09-01T16:20:00.000Z
    When I delete the measurement for "2015-09-01T16:20:00.000Z"
    Then the response has a status code of 404
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |
    And the measurement for "2015-09-01T16:10:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:10:00.000Z" | 27.3        | 16.9     | 0             |
