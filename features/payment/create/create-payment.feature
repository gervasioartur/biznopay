Feature: Create payment
  As a user
  I want to create a new payment
  So that I can pay for a product or service

  Scenario: Payment fails after all retry attempts are exhausted
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    And the payment gateway is permanently unavailable
    And the system retries processing the payment up to maximum attempts
    Then the system should return status code 200
    And the payment status should be "FAILED"
    And the response should contain error code "PAYMENT_FAILED"
    And the error severity should be "CRITICAL"
    And the error should be retryable

  Scenario: Payment succeeds after transient M-Pesa gateway failure
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    And the payment gateway fails temporarily on first attempt
    And the system retries processing the payment
    Then the system should return status code 201
    And the payment status should be "COMPLETED"

  Scenario: Payment creation fails when amount is missing
    When the user sends a POST request to "/api/v1/payments" with:
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the system should return status code 400
    And the response should contain error code "MISSING_REQUIRED_FIELD"
    And the error severity should be "LOW"
    And the response should contain error message "amount is required"

  Scenario: Payment creation fails when idempotency key is missing
    When the user sends a POST request to "/api/v1/payments" with:
      | amount       | 2000      |
      | description  | Bill      |
      | phone_number | 841234567 |
    Then the system should return status code 400
    And the response should contain error code "MISSING_REQUIRED_FIELD"
    And the error severity should be "LOW"
    And the response should contain error message "idempotency key is required"

  Scenario: Payment creation fails when phone number is missing
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000   |
      | description     | Bill   |
      | idempotency-key | 123abc |
    Then the system should return status code 400
    And the response should contain error code "MISSING_REQUIRED_FIELD"
    And the error severity should be "LOW"
    And the response should contain error message "phone number is required"

  Scenario: Payment creation fails when amount is zero or negative
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 0         |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the system should return status code 422
    And the response should contain error code "INVALID_AMOUNT"
    And the error severity should be "LOW"
    And the response should contain error message "amount must be greater than zero"

  Scenario: Payment creation fails when amount is less than mim amount limit
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 99        |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the system should return status code 422
    And the response should contain error code "INVALID_AMOUNT"
    And the error severity should be "LOW"
    And the response should contain error message "amount less than the  mim allowed"

  Scenario: Payment creation fails when amount exceeds maximum limit
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 99999999  |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the system should return status code 422
    And the response should contain error code "INVALID_AMOUNT"
    And the error severity should be "LOW"
    And the response should contain error message "amount exceeds maximum allowed"

  Scenario: Payment creation fails when phone number is invalid
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000   |
      | description     | Bill   |
      | phone_number    | 999999 |
      | idempotency-key | 123abc |
    Then the system should return status code 422
    And the response should contain error code "INVALID_PHONE_NUMBER"
    And the error severity should be "LOW"
    And the response should contain error message "invalid M-Pesa phone number"

  Scenario: Returning existing payment for same idempotency key
    Given a payment already exists with idempotency-key "123abc"
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the system should return status code 200
    And the response should contain the existing payment
    And no new payment should be created

  Scenario: Concurrent requests with same idempotency key return same payment
    When multiple requests are sent concurrently to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then only one payment should be created
    And all responses should return the same payment

  Scenario: Payment is persisted as PENDING before being sent to M-Pesa
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    Then the payment should be persisted with status "PENDING"
    And the system should initiate provider request
    And the payment status should transition to "PROCESSING"

  Scenario: Payment is successfully processed via M-Pesa
    When the user sends a POST request to "/api/v1/payments" with:
      | amount          | 2000      |
      | description     | Bill      |
      | phone_number    | 841234567 |
      | idempotency-key | 123abc    |
    And the payment gateway responds successfully
    Then the system should return status code 201
    And the payment status should be "COMPLETED"
    And the payment currency should be "MZN"
    And the response should contain a providerPaymentId