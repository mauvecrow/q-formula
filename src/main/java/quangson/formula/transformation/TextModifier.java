package quangson.formula.transformation;

import java.util.Map;

public interface TextModifier {

    String substituteVars(String input, Map<String, String> varsMap);
    String clean(String input, Map<String, String> varsMap);
}
