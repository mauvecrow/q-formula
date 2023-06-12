package quangson.formula;

import java.util.Map;

public interface Solvable {
    Number solve(String input, Map<String, String> varsMap);
}
