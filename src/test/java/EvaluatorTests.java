import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.evaluation.Evaluator;
import quangson.formula.evaluation.LocalEvaluator;

import java.math.BigDecimal;

public class EvaluatorTests {

    private static Evaluator evaluator;

    @BeforeAll
    static void init(){
        evaluator = new LocalEvaluator();
    }

    @Test
    @DisplayName("4+2^3")
    void test1(){
        String input = "4+2^3";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        BigDecimal after = le.evaluate(input);
        BigDecimal expected = BigDecimal.valueOf(12.0);
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("4+!2^3")
    void test2(){
        String input = "4+!2^3";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        BigDecimal after = le.evaluate(input);
        BigDecimal expected = BigDecimal.valueOf(-4.0);
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("100/4+2^3-7^2")
    void test3(){
        String input = "100/4+2^3-7^2";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        BigDecimal after = le.evaluate(input);
        BigDecimal expected = BigDecimal.valueOf(-16.0);
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("25^0.5")
    void test4(){
        String input = "25^0.5";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        BigDecimal after = le.evaluate(input);
        BigDecimal expected = BigDecimal.valueOf(5.0);
        Assertions.assertEquals(expected,after);
    }
}
