package quangson.rest.formula;

import java.util.Map;

public record FormulaRequest(String input, Map<String, String> varMap) {
}
