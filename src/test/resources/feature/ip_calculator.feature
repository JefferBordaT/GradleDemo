@DTPIpCalculator
Feature: Implement Ip Calculator

  @DTPIpCalculatorTrue
  Scenario Outline: when a url is sent and the service returns its ip
    Given publishes a message in kafka with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    When The message is sent to kafka
    Then the data published in the topic is validated with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    And ip is not null
    And status Code is 200
    Examples:
      | traceabilityId                       | url                     |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https://www.appgate.com |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https://github.com      |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https://www.exito.com/  |

  @DTPIpCalculatorFail
  Scenario Outline: when a url is sent and the service but the url it not exist
    Given publishes a message in kafka with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    When The message is sent to kafka
    Then the data published in the topic is validated with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    And ip is null
    And status Code is 400
    And message is contains "Name does not resolve"
    Examples:
      | traceabilityId                       | url                     |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | http://www.test1.com    |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | http://test2.com        |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https://www.qatest3.com |

  @DTPIpCalculatorFail
  Scenario Outline: when a url is sent to the service but the url format is not correct
    Given publishes a message in kafka with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    When The message is sent to kafka
    Then the data published in the topic is validated with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    And ip is null
    And status Code is 400
    And message is contains "is not a valid URL"
    Examples:
      | traceabilityId                       | url                |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | htt://www.test.com |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https:/test2.com   |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | www.test3          |

  @DTPIpCalculatorFail
  Scenario: when a url is sent to the service but the input message is empty
    Given publishes a message empty in kafka with the following data
    When The message is sent to kafka
    Then data is empty
    And status Code is 400
    And message is contains "Invalid traceabilityId value. / Invalid url value."

  @DTPIpCalculatorFail
  Scenario Outline: when a url is sent to the service but the input message is not correct value
    Given publishes a message in kafka with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    When The message is sent to kafka
    Then the data published in the topic is validated with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    And ip is null
    And status Code is 400
    And message is contains "<message>"
    Examples:
      | traceabilityId                       | url                  | message                                            |
      |                                      | http://www.test.com  | Invalid traceabilityId value.                      |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 |                      | Invalid url value.                                 |
      | #$$$%%&%&                            | www.test2.com        | traceabilityId node may be UUID format             |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | %&/(%$#              | is not a valid URL.                                |
      | aadwe243sfsftt                       | https://www.test.com | traceabilityId node may be UUID format             |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | sfsgfrr34553535      | is not a valid URL.                                |
      | 3123133                              | https://www.test.com | traceabilityId node may be UUID format             |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | 311313               | is not a valid URL.                                |
      | true                                 | https://www.test.com | traceabilityId node may be UUID format             |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | false                | is not a valid URL.                                |
      |                                      |                      | Invalid traceabilityId value. / Invalid url value. |
      | null                                 | null                 | traceabilityId node may be UUID format             |

  @DTPIpCalculatorFail
  Scenario Outline: when a url is sent to the service but the input message has more than one url value
    Given publishes a message in kafka with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    When The message is sent to kafka
    Then the data published in the topic is validated with the following data
      | traceabilityId   | url   |
      | <traceabilityId> | <url> |
    And ip is null
    And status Code is 400
    And message is contains "is not a valid URL"
    Examples:
      | traceabilityId                       | url                                                                |
      | ea1c2fa0-7af2-11ec-90d6-0242ac120003 | https://www.appgate.com,https://github.com/,https://www.exito.com/ |
