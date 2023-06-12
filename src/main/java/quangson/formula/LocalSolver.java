package quangson.formula;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import quangson.formula.evaluation.Evaluator;
import quangson.formula.evaluation.LocalEvaluator;
import quangson.formula.transformation.LocalTextModifier;
import quangson.formula.validation.LocalValidation;

import java.util.Map;

@RequestScoped
public class LocalSolver implements Solvable {


    private FormulaFacade facade;

    public LocalSolver() {
    }

    @PostConstruct
    private void init(){
        this.facade = new FormulaFacade(new LocalEvaluator(), new LocalTextModifier(), new LocalValidation());
    }

    @Override
    public Number solve(String input, Map<String, String> varsMap){
        var prepped = facade.prepareExpression(input, varsMap);
        facade.evaluateAll(prepped);
        return facade.getAnswer();

    }
}
