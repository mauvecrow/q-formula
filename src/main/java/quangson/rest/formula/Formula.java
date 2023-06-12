package quangson.rest.formula;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import quangson.formula.Solvable;

import java.util.Map;

@RequestScoped
@Path("formula")
public class Formula {

    @Inject
    private Solvable solver;

    @GET
    @Produces("text/plain")
    public String sayHi(){
        return "hello";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Number solve(FormulaRequest req){
        return solver.solve(req.input(), req.varMap());
    }

}
