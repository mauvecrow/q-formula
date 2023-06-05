import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.validation.FormulaValidator;
import quangson.formula.validation.LocalValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static quangson.formula.validation.FormulaValidator.ValidationResult;
import static quangson.formula.validation.FormulaValidator.ValidationMessage;


public class FormulaValidatorTests {

    static FormulaValidator localValidator;

    @BeforeAll
    static void initAll() {
        localValidator = new LocalValidation();
    }

    @Test
    @DisplayName("Basic test for open parenthesis")
    void test1() {
        String input = "()";
        var result = localValidator.hasBalancedParentheses(input);
        var expected = new ValidationResult(true, -1, ValidationMessage.BALANCED);
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Basic test of unbalanced input: )(")
    void test2() {
        String input = ")(";
        var result = localValidator.hasBalancedParentheses(input);
        var expected = new ValidationResult(false, 0, ValidationMessage.UNBALANCED1);
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Basic test of unbalanced input: ()(")
    void test3() {
        String input = "()(";
        var result = localValidator.hasBalancedParentheses(input);
        var expected = new ValidationResult(false, -1, ValidationMessage.UNBALANCED2);
    }

//    @Test
//    @DisplayName("Test Regex patterns and results")
//    void regex1() {
//        String[] params = new String[]{"a", "b"};
//        String input = "a+b";
//        String prefixPattern = "[+\\-*\\]?";
//        String suffixPattern = "[\\-+*\\\\^]?";
//        String fullPattern = prefixPattern + params[0] + suffixPattern;
//
//        Pattern pattern = Pattern.compile(fullPattern);
//        System.out.println("Pattern: " + pattern.pattern());
//        Matcher matcher = pattern.matcher(input);
//        while (matcher.find()) {
//            String result = matcher.group();
//            System.out.println(result);
//        }
////        System.out.println(matcher.find());
//    }

    @Test
    @DisplayName("remove params as regex pattern from string and see if only operators and numbers remain")
    void regex2(){
        String input = "((4x-y)/z + 83d)";
        String[] params = {"x","y","z","d"};
        for(String p : params){
            input = input.replaceAll(p,"");
        }
//        System.out.println("input: " + input);
        Pattern p = Pattern.compile("[a-zA-Z_]");
        Matcher m = p.matcher(input);

        boolean actual = m.matches();
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("detect undeclared params")
    void regex3(){
        String input = "((4x-y)/z + 83d)";
        String[] params = {"x","y","z"};
        for(String p : params){
            input = input.replaceAll(p,"");
        }
        System.out.println("input: " + input);
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(input);
        boolean actual = m.find();
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("Method test params: simple success - all params verified")
    void test4(){
        String input = "a+b-3";
        String[] params = {"a","b"};
        var message = localValidator.hasVerifiedParams(input, params);
        Assertions.assertTrue(message.isValid());
        Assertions.assertEquals(-1,message.errorIndex());
        Assertions.assertEquals(ValidationMessage.PARAMS_PASS, message.error());
    }

    @Test
    @DisplayName("Method test params: simple fail - param b not verified")
    void test5(){
        String input = "a+b-3";
        String[] params = {"a"};
        var message = localValidator.hasVerifiedParams(input, params);
        var result = message.isValid();
        var errorIndex = message.errorIndex();
        var errorMsg = message.error();
        Assertions.assertFalse(result);
        Assertions.assertEquals(2,errorIndex);
        Assertions.assertEquals(ValidationMessage.PARAMS_FAIL, errorMsg);
    }

    @Test
    @DisplayName("Check for Pass if params verify with a_1, a_2, ... ")
    void test6(){
        String input = "a_1 + 5*a_2 /3A_3 - b_2";
        String[] params = {"a_1", "a_2", "A_3", "b_2"};
        var message = localValidator.hasVerifiedParams(input, params);
        Assertions.assertTrue(message.isValid());
        Assertions.assertEquals(-1,message.errorIndex());
        Assertions.assertEquals(ValidationMessage.PARAMS_PASS, message.error());
    }

    @Test
    @DisplayName("Check for Fail if params verify with a_1, a_2, ... ")
    void test7(){
        String input = "a_1 + 5*a_2 /3A_3 - b_2";
        String[] params = {"a_1", "a_2", "b_2"};
        var message = localValidator.hasVerifiedParams(input, params);
        Assertions.assertFalse(message.isValid());
        Assertions.assertEquals(14,message.errorIndex());
        Assertions.assertEquals(ValidationMessage.PARAMS_FAIL, message.error());
    }

}
