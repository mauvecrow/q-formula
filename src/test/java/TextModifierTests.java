import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quangson.formula.transformation.LocalTextModifier;
import quangson.formula.transformation.TextModifier;

import java.util.Map;

public class TextModifierTests {

    private static TextModifier textModifier;

    @BeforeAll
    static void init(){
        textModifier = new LocalTextModifier();
    }

    @Test
    @DisplayName("Simple test on variable substitution with vars of length 1")
    void test1(){
        String input = "a+b";
        Map<String, String> varsMap = Map.of("a","1","b","2");
        String result = textModifier.substituteVars(input, varsMap);
        String expected = "1+2";
        Assertions.assertEquals(expected,result);
    }

    @Test
    @DisplayName("Test substitution when vars have different length")
    void test2(){
        String input = "a_1 + b_1 - a - b";
        Map<String, String> varsMap = Map.of("a_1","3","b_1", "10","a","22","b","8");
        String result = textModifier.substituteVars(input, varsMap);
        String expected = "3 + 10 - 22 - 8";
        Assertions.assertEquals(expected,result);
    }

    @Test
    @DisplayName("Test Clean - vars next to one another")
    void test3(){
        String input = "3abc";
        Map<String,String> varsMap = Map.of("a","null","b","null","c","null");
        String actual = textModifier.clean(input, varsMap);
        String expected = "3*a*b*c";
//        System.out.println(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test clean -  43a- b3*9. 3 /c^4 ")
    void test3_1(){
        String input = " 43a- b3*9. 3 /c^4 ";
        Map<String,String> varsMap = Map.of("a","null","b","null","c","null");
        String actual = textModifier.clean(input, varsMap);
        String expected = "43*a-b*3*9.3/c^4";
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @DisplayName("Test clean - 4((9-b)(a-3))3(a+b)")
    void test3_2(){
        String input = "4((9-b)(a-3))c4(a+b)";
        Map<String,String> varsMap = Map.of("a","null","b","null","c","null");
        String actual = textModifier.clean(input, varsMap);
        String expected = "4*((9-b)*(a-3))*c*4*(a+b)";
        Assertions.assertEquals(expected,actual);
    }
}
