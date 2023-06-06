import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.validation.FormulaValidator;
import quangson.formula.validation.LocalValidation;

import java.util.Arrays;

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
        Assertions.assertEquals(expected,result);
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

    @Test
    @DisplayName("Test misuse operand - detect operand at the start")
    void test8(){
        String[] inputs = {"+1-b*a", "/b-8", "*a*b","%9-4","^3-a+c"};
        ValidationResult[] results = new ValidationResult[inputs.length];
        for(int i = 0; i < inputs.length; i++){
            results[i] = localValidator.hasMisusedOperators(inputs[i]);
        }
        ValidationResult expected =  new ValidationResult(false,0,ValidationMessage.MISUSE_OPERATOR);
        long count = Arrays.stream(results)
                .filter( vr -> vr.equals(expected))
                .count();

        Assertions.assertEquals(inputs.length, count);
    }

    @Test
    @DisplayName("Test misuse operand - detect operand at the end")
    void test9(){
        String[] inputs = {"1-b*a+", "b-8/", "a*b*","9-4%","3-a+c^", "a-b-"};
        ValidationResult[] results = new ValidationResult[inputs.length];
        for(int i = 0; i < inputs.length; i++){
            results[i] = localValidator.hasMisusedOperators(inputs[i]);
        }

        long count = Arrays.stream(results)
                .filter( vr -> !vr.isValid() && vr.errorIndex() > 0 && vr.error().equals(ValidationMessage.MISUSE_OPERATOR))
                .count();

        Assertions.assertEquals(inputs.length, count);
    }

    @Test
    @DisplayName("Test misuse operand - detect operand in the middle")
    void test10(){
        String[] inputs = {"1-b**a", "b--8", "a++b","9%%4","3-a^^c", "a//b"};
        ValidationResult[] results = new ValidationResult[inputs.length];
        for(int i = 0; i < inputs.length; i++){
            results[i] = localValidator.hasMisusedOperators(inputs[i]);
        }

        long count = Arrays.stream(results)
                .filter( vr -> !vr.isValid() && vr.errorIndex() > 0 && vr.error().equals(ValidationMessage.MISUSE_OPERATOR))
                .count();

        Assertions.assertEquals(inputs.length, count);
    }

}
