package quangson.formula.validation;

import jakarta.validation.Valid;
import jakarta.validation.Validation;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalValidation implements FormulaValidator{
    @Override
    public ValidationResult hasBalancedParentheses(String input) {
        int openCount = 0;
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if( c == '('){
                openCount++;

            }
            else if( c== ')'){
                openCount--;
            }

            if( openCount < 0){
                return new ValidationResult(false, i, ValidationMessage.UNBALANCED1);
            }

        }
        ValidationResult success = new ValidationResult(true, -1, ValidationMessage.BALANCED);
        ValidationResult failure = new ValidationResult(false, -1, ValidationMessage.UNBALANCED2);

        return openCount == 0 ? success : failure;
    }

    @Override
    public ValidationResult hasVerifiedParams(String input, String... params) {
        String prefixPattern = "[\\+\\-\\*\\\\]?";
        String suffixPattern = "[\\-\\+\\*\\\\\\^]?";
        String fullPattern = prefixPattern + params[0] + suffixPattern;

        Pattern pattern = Pattern.compile(fullPattern);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.group();
        return null;
    }

    @Override
    public ValidationResult hasMisusedOperators(String input) {
        return null;
    }
}
