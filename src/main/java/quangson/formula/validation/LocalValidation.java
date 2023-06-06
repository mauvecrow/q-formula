package quangson.formula.validation;

import java.util.Arrays;
import java.util.List;
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
        for(String p : params){
            char[] chars = p.toCharArray();
            Arrays.fill(chars,'*');
            input = input.replaceAll(p,new String(chars));
        }
//        System.out.println("input after fill: " + input);

        String regex = "[a-zA-Z]";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean hasMatch = matcher.find();
        int errorIndex = hasMatch ? matcher.start() : -1;
        var message = hasMatch ? ValidationMessage.PARAMS_FAIL : ValidationMessage.PARAMS_PASS;
        return new ValidationResult(!hasMatch, errorIndex, message);
    }

    @Override
    public ValidationResult hasMisusedOperators(String input) {
        int len = input.length();
        String illegalStart = "[+*/%^]";
        boolean firstCheck = Pattern.compile(illegalStart)
                .matcher(input.substring(0,1))
                .matches();
        if(firstCheck){
            return new ValidationResult(false,0,ValidationMessage.MISUSE_OPERATOR);
        }
        String illegalEnd = "[+*/%^\\-]";
        boolean lastCheck = Pattern.compile(illegalEnd)
                .matcher(input.substring(len-1,len))
                .matches();
        if(lastCheck){
            return new ValidationResult(false, len-1, ValidationMessage.MISUSE_OPERATOR);
        }
        List<Character> operators = List.of('+','-','*','/','%','^');
        for(int i =1; i <len-1; i++){
            char c = input.charAt(i);
            if(operators.contains(c)){
                char prev = input.charAt(i-1);
                char next = input.charAt(i+1);
                switch (prev) {
                    case '(', '+', '-','*', '/', '%', '^' -> {
                        return new ValidationResult(false, i, ValidationMessage.MISUSE_OPERATOR);
                    }
                }
                switch (next) {
                    case ')', '+', '-','*', '/', '%', '^' -> {
                        return new ValidationResult(false, i, ValidationMessage.MISUSE_OPERATOR);
                    }
                }
            }

        }
        return new ValidationResult(true, -1, ValidationMessage.CORRECT);
    }
}
