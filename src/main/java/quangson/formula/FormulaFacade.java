package quangson.formula;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import quangson.formula.evaluation.Evaluator;
import quangson.formula.transformation.TextModifier;
import quangson.formula.validation.FormulaValidator;

import java.util.*;

@Dependent
public class FormulaFacade {
    private final Evaluator evaluator;
    private final TextModifier textModifier;
    private final FormulaValidator validator;

    private final Map<Integer, List<String>> depthMap;

    @Inject
    public FormulaFacade(Evaluator evaluator, TextModifier textModifier, FormulaValidator validator) {
        this.evaluator = evaluator;
        this.textModifier = textModifier;
        this.validator = validator;
        depthMap = new HashMap<>();
    }


    public String validate(String input){
        var parenthesesCheck = validator.hasBalancedParentheses(input);
        var operatorsCheck = validator.hasMisusedOperators(input);
        String message;
        if(parenthesesCheck.isValid() && operatorsCheck.isValid()){
            message = "OK";
        }
        else if( parenthesesCheck.isValid() && !operatorsCheck.isValid()){
            String errorMsg = operatorsCheck.error().getMessage();
            int errorIndex = operatorsCheck.errorIndex();
            message = String.format("Starting at index %d:\n%s",errorIndex,errorMsg);
        }
        else if(!parenthesesCheck.isValid() && operatorsCheck.isValid()){
            String errorMsg = parenthesesCheck.error().getMessage();
            int errorIndex = parenthesesCheck.errorIndex();
            message = String.format("Starting at index %d:\n%s",errorIndex,errorMsg);
        }
        else {
            message = "There are errors with both the use of parentheses and operators.";
        }
        return message;
    }

    public String validateParams(String input, String ... params){
        var paramsCheck = validator.hasVerifiedParams(input,params);
        String errorMsg = paramsCheck.error().getMessage();
        int errorIndex = paramsCheck.errorIndex();
        return paramsCheck.isValid() ? "OK" : String.format("Starting at index %d:\n%s",errorIndex,errorMsg);
    }

    // Solving Strategy
    public String prepareExpression(String input, Map<String, String> varsMap){
        String initialCheck = validate(input);
        if(!initialCheck.equals("OK")){
            throw new RuntimeException(initialCheck);
        }
        String[] params = varsMap.keySet().toArray(new String[0]);
        String paramsCheck = validateParams(input, params);
        if(!paramsCheck.equals("OK")){
            throw new RuntimeException(paramsCheck);
        }
        String cleanInput = textModifier.clean(input, varsMap);
        return textModifier.substituteVars(cleanInput, varsMap);
    }

    public Map<Integer, List<String>> evaluateAll(String input){
//        String copy = input;
        input = "(" + input + ")";
//        Map<Integer, List<String>> depthMap = new HashMap<>();
        int depth = 0;
        Deque<Integer> leftParenIndices = new ArrayDeque<>();
        for(int i = 0; i < input.length()-1; i++){
            char cur = input.charAt(i);
            if(cur == '('){
                leftParenIndices.add(i);
                ++depth;
            }
            else if (cur == ')'){
                //should never be null if the input passes parentheses validation
                int mostRecentLeft = leftParenIndices.pollLast();
                String innerExpr = input.substring(mostRecentLeft,i+1);
                String evaluableExpr = innerExpr.substring(1,innerExpr.length()-1);
                String answer = evaluator.evaluate(evaluableExpr);
                depthMap.computeIfAbsent(depth, ArrayList::new)
                        .add(answer);
                --depth;
//                copy = copy.replace(innerExpr,answer);
                input = input.replace(innerExpr, answer);
                i = leftParenIndices.peekLast();
            }

        }
        // handle last parenthesis at the end
        if(leftParenIndices.size() == 1){
            int mostRecentLeft = leftParenIndices.pollLast();
            String innerExpr = input.substring(mostRecentLeft);
            String evaluableExpr = innerExpr.substring(1,innerExpr.length()-1);
            String answer = evaluator.evaluate(evaluableExpr);
            depthMap.computeIfAbsent(depth, ArrayList::new)
                    .add(answer);
            --depth; //should be zero now
        }

        return depthMap;
    }

    public double getAnswer(){
        if(depthMap.isEmpty()){
            throw new RuntimeException("Evaluation hasn't performed or answer is not available");
        }
        String result = depthMap.get(1).get(0);
        boolean isNegative = result.charAt(0) == '~';
        return isNegative ? Double.parseDouble("-"+result.substring(1)) : Double.parseDouble(result);
    }


}
