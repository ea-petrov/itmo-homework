package expression.parser;


public class Main {
    public static void main(String[] args) {
        TripleParser p = new ExpressionParser();
        System.out.println(p.parse("(Cdclpaspvx // sdfodsfkosay) + sfdsfsfpz").evaluate(10,2,21));
        System.out.println(p.parse("x**y+z").evaluate(0,-1,0));
        System.out.println(p.parse("-(x)²").toString());
        System.out.println(p.parse("x²").toString());
        System.out.println(p.parse("-1").toString());
        System.out.println(p.parse("(  -( -31302)²)").evaluate(2,3,4));
        System.out.println(p.parse("x + 2").toMiniString());
    }
}
