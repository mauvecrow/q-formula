import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.FormulaFacade;
import quangson.formula.evaluation.LocalEvaluator;
import quangson.formula.transformation.LocalTextModifier;
import quangson.formula.validation.LocalValidation;

import java.util.List;
import java.util.Map;

public class FacadeTests {

    private static FormulaFacade facade;

    @BeforeAll
    static void init(){
        facade = new FormulaFacade(new LocalEvaluator(), new LocalTextModifier(), new LocalValidation());
    }

//    @Test
//    @DisplayName("test depth map")
//    void print1(){
//        String input = "((a+b)*(a-b))";
//        var depthMap = facade.deriveDepthMap(input);
//        System.out.println(depthMap.toString());
//    }

    @Test
    @DisplayName("test prep")
    void print2(){
        String input = "((a+b)*(a-b))";
        Map<String, String> varsMap = Map.of("a","3","b","7");
        String prepped = facade.prepareExpression(input, varsMap);
        System.out.println(prepped);
    }

    @Test
    @DisplayName("evaluate all")
    void print3(){
        String input = "((a+b)*(a-b))";
        Map<String, String> varsMap = Map.of("a","3","b","7");
        String prepped = facade.prepareExpression(input, varsMap);
//        var depthMap = facade.deriveDepthMap(prepped);
        var result = facade.evaluateAll(prepped);
        System.out.println(result);
    }

    @Test
    @DisplayName("((a+b)*(a-b)); a=3, b=7")
    void test1(){
        String input = "((a+b)*(a-b))";
        Map<String, String> varsMap = Map.of("a","3","b","7");
        String prepped = facade.prepareExpression(input, varsMap);
        var map = facade.evaluateAll(prepped);
        var result = facade.getAnswer();
        System.out.println(map);
        Assertions.assertEquals(-40.0, result);
    }

    @Test
    @DisplayName("1 + 1/ (1 + 1/( 1 + 1/( 2)))")
    void test2(){
        String input = "1 + 1/ (1 + 1/( 1 + 1/( 2)))";
        String prepped = facade.prepareExpression(input, Map.of());
        var map = facade.evaluateAll(prepped);
        var result = facade.getAnswer();
        System.out.println(map);
        Assertions.assertEquals(1.6, result);
    }

    @Test
    @DisplayName("(9)^.5")
    void test3(){
        String input = "(9)^.5";
        String prepped = facade.prepareExpression(input, Map.of());
        var map = facade.evaluateAll(prepped);
        var result = facade.getAnswer();
        System.out.println(map);
        Assertions.assertEquals(3, result);
    }

    @Test
    @DisplayName("-(9)^.5")
    void test4(){
        String input = "-(9)^.5";
        String prepped = facade.prepareExpression(input, Map.of());
        Map<Integer, List<String>> map;
        double result;
        try {
            map = facade.evaluateAll(prepped);
            result = facade.getAnswer();
        }
        catch(RuntimeException r){
            map = Map.of();
        }

        System.out.println(map);
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("(-b+(b^2 - 4ac )^.5) / (2a) ")
    void test5(){
        String input = "(-b+(b^2 - 4ac )^.5) / (2a) ";
        var vars = Map.of("a", "3", "b","9", "c", "1");
        String prepped = facade.prepareExpression(input, vars);
        var map = facade.evaluateAll(prepped);
        var result = facade.getAnswer();
        System.out.println(map);
//        Assertions.assertEquals(3, result);
    }
}
