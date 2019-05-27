package test;

import org.junit.Test;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

public class ParsiiTest {

    @Test
    public void xx() throws ParseException {
        String exp = "2 + (7-5) * x";

        Scope scope = Scope.create();
        Expression parsiiExpr = Parser.parse(exp, scope);
        Variable var = scope.getVariable("x");
        var.setValue(3);

        double result = parsiiExpr.evaluate();
        System.out.println(result);
    }


    @Test
    public void sin() throws ParseException {
        Scope scope = Scope.create();
        Expression expr = Parser.parse("sin(a)", scope);
        Variable a = scope.getVariable("a");
        a.setValue(90 * Math.PI / 180);
        System.out.println(expr.evaluate());
    }

    @Test
    public void mainxxx() throws ParseException {

        Scope scope = Scope.create();
        Expression expr = Parser.parse("a + (b - c) * 10 * d^(e) + sin(f)", scope);
        Variable a = scope.getVariable("a");
        Variable b = scope.getVariable("b");
        Variable c = scope.getVariable("c");
        Variable d = scope.getVariable("d");
        Variable e = scope.getVariable("e");
        Variable f = scope.getVariable("f");

        a.setValue(1);
        b.setValue(10);
        c.setValue(7);
        d.setValue(2);
        e.setValue(3);
        f.setValue(30 * Math.PI / 180);
        //        1+3*10*8+1/2 = 241.5

        System.out.println(expr.evaluate());
    }
}
