package quangson.formula.evaluation;

import jakarta.enterprise.context.Dependent;

import java.util.*;

@Dependent
public class LocalEvaluator implements Evaluator {

    @Override
    public String evaluate(final String expression) {
        String exprCopy = expression;
        exprCopy = expression.charAt(0) == '-' ? "~" + exprCopy.substring(1) : exprCopy;
        List<int[]> operations = getOperations(exprCopy);

        while(!operations.isEmpty()){
            int[] next = operations.remove(0);
            int opIndex = next[0];
            int left = getLeftIndex(opIndex,exprCopy);
            int right = getRightIndex(opIndex, exprCopy);
            String op1 = exprCopy.substring(left,opIndex);
            String op2 = exprCopy.substring(opIndex+1, right);
            char operator = exprCopy.charAt(opIndex);
            String result = calculate(operator,op1, op2);
            String calcStr = exprCopy.substring(left,right);
            exprCopy = exprCopy.replace(calcStr,result);
            operations = getOperations(exprCopy);
        }
        return exprCopy;
    }

    private List<int[]> getOperations(String expression){
        List<int[]> operations = new ArrayList<>();
        for(int i =0; i < expression.length(); i++){
            char cur = expression.charAt(i);
            if(isAnOperator(cur)){
                operations.add(new int[]{i,operatorPriority(cur)});
            }
        }

        if(operations.isEmpty()) return operations;

        operations.sort( (a,b) -> {
            int index1 = a[0];
            int index2 = b[0];
            int priority1 = a[1];
            int priority2 = b[1];
            if( priority2 > priority1){
                return 1;
            }
            else if(priority2 == priority1){
                return Integer.compare(index1, index2);
            }
            else return -1;
        });
        return operations;
    }

    private int operatorPriority(char c){
        return switch(c){
            case '^' -> 3;
            case '*', '/', '%' -> 2;
            case '+', '-' -> 1;
            default -> throw new IllegalArgumentException("Operator not recognized: " + c);
        };
    }

    private String calculate(char operator, String operand1, String operand2) {
        double d1 = operand1.charAt(0) == '~' ? Double.parseDouble("-"+operand1.substring(1)) : Double.parseDouble(operand1);
        double d2 = operand2.charAt(0) == '~' ? Double.parseDouble("-"+operand2.substring(1)) : Double.parseDouble(operand2);

        double result = switch (operator) {
            case '^' -> Math.pow(d1, d2);
            case '*' -> d1 * d2;
            case '/' -> d1 / d2;
            case '%' -> d1 % d2;
            case '+' -> d1 + d2;
            case '-' -> d1 - d2;
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
        if(Double.isNaN(result)) throw new RuntimeException("Result is NaN");
        return result > 0 ? String.valueOf(result) : "~" + String.valueOf(result).substring(1);
    }

    private boolean isAnOperator(char c) {
        return c == '^' || c == '*' || c == '/' || c == '%' || c == '+' || c == '-';
    }

    private int getLeftIndex(int operatorIndex, String expression) {
        int l = operatorIndex - 1;
        if (l == 0) return l;

        while (!isAnOperator(expression.charAt(l))) {
            if(l == 0) return l;
            l--;
        }

        return ++l;
    }

    private int getRightIndex(int operatorIndex, String expression) {
        int r = operatorIndex + 1;
        if (r == expression.length()-1) return ++r;

        while (r < expression.length() && !isAnOperator(expression.charAt(r))) {
            ++r;
        }
        return r;
    }

}
