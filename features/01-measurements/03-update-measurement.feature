Feature: Update a measurement
  In order to correct the record of weather conditions
  I want to be able to update a measurement taken at a specific time

  Background:
    # POST /measurements
    Given I have submitted new measurements as follows:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |
      | "2015-09-01T16:10:00.000Z" | 27.3        | 16.9     | 0             |

  Scenario: Replace a measurement with valid (numeric) values
    # PUT /measurements/2015-09-01T16:00:00.000Z
    When I replace the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 15.2          |
    Then the response has a status code of 204
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 15.2          |

  Scenario: Replace a measurement with invalid values
    # PUT /measurements/2015-09-01T16:00:00.000Z
    When I replace the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | temperature    | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | "not a number" | 16.7     | 15.2          |
    Then the response has a status code of 400
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp              | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |

  Scenario: Replace a measurement with mismatched timestamps
    # PUT /measurements/2015-09-01T16:00:00.000Z
    When I replace the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-02T16:00:00.000Z" | 27.1        | 16.7     | 15.2          |
    Then the response has a status code of 409
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp              | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |

  Scenario: Replace a measurement that does not exist
    # PUT /measurements/2015-09-02T16:00:00.000Z
    When I replace the measurement for "2015-09-02T16:00:00.000Z" as follows:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-02T16:00:00.000Z" | 27.1        | 16.7     | 15.2          |
    Then the response has a status code of 404

  Scenario: Update metrics of a measurement with valid (numeric) values
    # PATCH /measurements/2015-09-01T16:00:00.000Z
    When I update the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | precipitation |
      | "2015-09-01T16:00:00.000Z" | 12.3          |
    Then the response has a status code of 204
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 12.3          |

  Scenario: Update metrics of a measurement with invalid values
    # PATCH /measurements/2015-09-01T16:00:00.000Z
    When I update the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | precipitation  |
      | "2015-09-01T16:00:00.000Z" | "not a number" |
    Then the response has a status code of 400
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |

  Scenario: Update metrics of a measurement with mismatched timestamps
    # PATCH /measurements/2015-09-01T16:00:00.000Z
    When I update the measurement for "2015-09-01T16:00:00.000Z" as follows:
      | timestamp                  | precipitation |
      | "2015-09-02T16:00:00.000Z" | 12.3          |
    Then the response has a status code of 409
    And the measurement for "2015-09-01T16:00:00.000Z" is:
      | timestamp                  | temperature | dewPoint | precipitation |
      | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |

  Scenario: Update metrics of a measurement that does not exist
    # PATCH /measurements/2015-09-02T16:00:00.000Z
    When I update the measurement for "2015-09-02T16:00:00.000Z" as follows:
      | timestamp                  | precipitation |
      | "2015-09-02T16:00:00.000Z" | 12.3          |
    Then the response has a status code of 404
