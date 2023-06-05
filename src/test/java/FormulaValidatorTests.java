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

    @Test
    @DisplayName("Test Regex patterns and results")
    void regex1() {
        String[] params = new String[]{"a", "b"};
        String input = "a+b";
        String prefixPattern = "[\\+\\-\\*\\\\]?";
        String suffixPattern = "[\\-\\+\\*\\\\\\^]?";
        String fullPattern = prefixPattern + params[0] + suffixPattern;

        Pattern pattern = Pattern.compile(fullPattern);
        System.out.println("Pattern: " + pattern.pattern());
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String result = matcher.group();
            System.out.println(result);
        }
//        System.out.println(matcher.find());
    }

}
