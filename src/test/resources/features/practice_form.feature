@regression @practiceForm
Feature: Student Registration via Practice Form
  As a user
  I want to complete and submit the practice form
  So that my registration details are successfully recorded

  Background:
    Given I am on the "Practice Form" page

  # ----------------------- Business Rules -----------------------
  Rule: Required fields
  - First name, Last name, Gender, and Mobile are mandatory
  - The form must not submit if any required field is missing

    Rule: Input format validations
    - Email must be a valid HTML5 email
    - Mobile must contain exactly 10 digits

  # ----------------------- Positive Scenarios -----------------------
    @smoke @positive
    Scenario: Submit the form with minimum required fields
      When I fill the form with:
        | firstName | Ana         |
        | lastName  | Silva       |
        | gender    | Female      |
        | mobile    | 9998887766  |
      And I submit the form
      Then I should see the success modal "Thanks for submitting the form"

    @positive
    Scenario: Submit the form with all optional fields
      When I fill the form with:
        | firstName       | John             |
        | lastName        | Doe              |
        | email           | john@demoqa.com  |
        | gender          | Male             |
        | mobile          | 1112223333       |
        | dateOfBirth     | 02 Nov 1995      |
        | currentAddress  | 123 Test Street  |
        | state           | NCR              |
        | city            | Delhi            |
        | picturePath     | resources/photo.png |
      And I add subjects:
        | Maths  |
        | English|
      And I select hobbies:
        | Sports |
        | Music  |
      And I submit the form
      Then I should see the success modal "Thanks for submitting the form"

  # ----------------------- Negative: Format validations -----------------------
    @negative
    Scenario Outline: Invalid email blocks submission
      When I fill the form with:
        | email | <email> |
      And I submit the form
      Then the "email" field should be "<validity>"
      And the form should not be submitted

      Examples:
        | email    | validity     |
        | foo@bar  | typeMismatch |
        | abc      | typeMismatch |

    @negative
    Scenario Outline: Invalid mobile format blocks submission
      When I fill the form with:
        | mobile | <value> |
      And I submit the form
      Then the "mobile" field should be "<validity>"

      Examples:
        | value      | validity        |
        | 123456789  | tooShort        |
        | 12345abcde | patternMismatch |

  # ----------------------- Negative: Missing required fields -----------------------
    @negative
    Scenario Outline: Missing required fields prevents submission
      When I fill the form with:
        | firstName | <firstName> |
        | lastName  | <lastName>  |
        | gender    | <gender>    |
        | mobile    | <mobile>    |
      And I submit the form
      Then the "<blockingField>" field should be "valueMissing"
      And the form should not be submitted

      Examples:
        | firstName | lastName | gender | mobile     | blockingField |
        |            | Silva    | Female | 9998887766 | firstName     |
        | Ana        |          | Female | 9998887766 | lastName      |
        | Ana        | Silva    |        | 9998887766 | gender        |
        | Ana        | Silva    | Female |            | mobile        |

  # ----------------------- Accessibility checks -----------------------
    @a11y
    Scenario: All required fields should have accessible labels
      When I check the labels of required fields
      Then all required inputs should be associated with labels
