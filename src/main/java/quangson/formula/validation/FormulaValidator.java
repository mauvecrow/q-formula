package quangson.formula.validation;

public interface FormulaValidator {

    ValidationResult hasBalancedParentheses(String input);
    ValidationResult hasVerifiedParams(String input, String... params);

    ValidationResult hasMisusedOperators(String input);

    record ValidationResult(boolean isValid, int errorIndex, ValidationMessage error){}

    enum ValidationMessage {
        UNBALANCED1("Closing parenthesis detected without opening counterpart"),
        UNBALANCED2("Opened parenthesis without closing pair detected"),
        BALANCED("Balance input");

        private final String message;
        ValidationMessage(String message){
            this.message = message;
        }

        public String getMessage(){ return this.message; }

        @Override
        public String toString(){
            return getMessage();
        }
    }
}

