import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.evaluation.Evaluator;
import quangson.formula.evaluation.LocalEvaluator;

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
        var after = le.evaluate(input);
        var expected = "12.0";
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("4+~2^3")
    void test2(){
        String input = "4+~2^3";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        var after = le.evaluate(input);
        var expected = "~4.0";
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("100/4+2^3-7^2")
    void test3(){
        String input = "100/4+2^3-7^2";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        var after = le.evaluate(input);
        var expected = "~16.0";
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("25^0.5")
    void test4(){
        String input = "25^0.5";
        LocalEvaluator le = (LocalEvaluator) evaluator;
        var after = le.evaluate(input);
        var expected = "5.0";
        Assertions.assertEquals(expected,after);
    }

    @Test
    @DisplayName("~10-25")
    void test5(){
        String input = "~10-25";
        var actual = evaluator.evaluate(input);
        var expected = "~35.0";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @DisplayName("~3*8/6")
    void test6(){
        String input = "~3*8/60";
        var actual = evaluator.evaluate(input);
        var expected = "~0.4";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @DisplayName("-3*8/6")
    void test7(){
        String input = "-3*8/60";
        var actual = evaluator.evaluate(input);
        var expected = "~0.4";
        Assertions.assertEquals(expected,actual);
    }
}
