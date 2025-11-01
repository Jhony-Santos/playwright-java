

Feature: Practice Form

  Background:
    Given que estou na página "Practice Form"

  @negativo
  Scenario: E-mail preenchido com formato inválido deve bloquear o submit
    When eu preencho o email com "foo@bar"
    And eu clico em Submit
    Then o campo email deve estar "typeMismatch"
    And o formulário não deve ser submetido

  @negativo
  Scenario Outline: Mobile inválido por quantidade de dígitos
    When eu preencho o mobile com "<valor>"
    And eu clico em Submit
    Then o campo mobile deve estar "<validity>"

    Examples:
      | valor       | validity     |
      | 123456789   | tooShort     |
      | 12345abcde  | patternMismatch |

  @positivo
  Scenario: Preenche mínimos obrigatórios e submete
    When eu preencho os campos obrigatórios corretamente
    Then o modal de sucesso deve aparecer
