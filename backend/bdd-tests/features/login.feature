Feature: User Login
  As a registered user
  I want to login with my email and password
  So that I can access my dashboard

  Scenario: Successful login
    Given I am a registered user with email "dilini@example.com" and password "12345678"
    When I send a login request with email "dilini@example.com" and password "12345678"
    Then I should receive a success message

  Scenario: Invalid login
    Given I am a registered user with email "dilini@example.com" and password "12345678"
    When I send a login request with email "dilini@example.com" and password "wrongpass"
    Then I should see an error message "Invalid Credentials"
