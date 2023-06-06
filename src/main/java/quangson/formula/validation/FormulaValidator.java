package quangson.formula.validation;

public interface FormulaValidator {

    ValidationResult hasBalancedParentheses(String input);
    ValidationResult hasVerifiedParams(String input, String... params);

    ValidationResult hasMisusedOperators(String input);

    record ValidationResult(boolean isValid, int errorIndex, ValidationMessage error){}

    enum ValidationMessage {
        UNBALANCED1("Closing parenthesis detected without opening counterpart"),
        UNBALANCED2("Opened parenthesis without closing pair detected"),
        BALANCED("Balanced input"),
        PARAMS_PASS("No undefined parameters detected"),
        PARAMS_FAIL("An undefined parameter was detected"),
        MISUSE_OPERATOR("Operator with missing operand detected"),
        CORRECT("No issues detected");

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

