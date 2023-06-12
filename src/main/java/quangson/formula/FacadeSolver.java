package quangson.formula;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.Map;

@RequestScoped
public class FacadeSolver implements Solvable {

    @Inject
    private FormulaFacade facade;

    @Override
    public Number solve(String input, Map<String, String> varsMap){
        var prepped = facade.prepareExpression(input, varsMap);
        facade.evaluateAll(prepped);
        return facade.getAnswer();

    }
}
