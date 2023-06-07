package quangson.formula.transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LocalTextModifier implements TextModifier{
    @Override
    public String substituteVars(String input, Map<String, String> varsMap) {
        String[] variables = getAndSortVars(varsMap);

        for(String variable : variables){
            input = input.replaceAll(variable, varsMap.get(variable));
        }
        return input;
    }

    @Override
    public String clean(String input, Map<String, String> varsMap) {
        input = input.strip();
        input = input.replaceAll(" ", "");
        String[] variables = varsMap.keySet().toArray(new String[0]);
        String temp = input;
        for(String variable : variables){
            temp = temp.replaceAll(variable, "\\$");
            // note: the "replacement" arg is used as a regex behind the scenes, and
            // $ is a special character used and needs to escape it
        }
        temp = temp.replaceAll("[\\d.]","#");
        System.out.println("temp: " + temp);

        List<Integer> indices = new ArrayList<>();
        for(int i=0;i<temp.length()-1;i++){
            char next = temp.charAt(i+1);
            char cur = temp.charAt(i);
            if(cur == '#'){
                if(next == '$' || next == '('){
                    indices.add(i);
                }
            }
            else if(cur == '$' || cur == ')'){
                if(next == '$' || next == '(' || next == '#'){
                    indices.add(i);
                }
            }
        }
        StringBuilder sb = new StringBuilder(input);
        int offset = 1;
        for(int index : indices){
            sb.insert(index+offset++,"*");
        }
        return sb.toString();


    }

    private String[] getAndSortVars(Map<String, String> varsMap){
        String[] variables = varsMap.keySet().toArray(new String[0]);
        Arrays.sort(variables, (a,b) -> {
            if(b.length() > a.length()){
                return 1;
            }
            else if(b.length() < a.length()){
                return -1;
            }
            else {
                return b.compareTo(a);
            }
        });
        return variables;
    }

}
