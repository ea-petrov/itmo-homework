package expression.exceptions;

import expression.TripleExpression;

public class Main {
    private final static ExpressionParser p = new ExpressionParser();

    private static void run(final TripleExpression expr, int x) {
        try {
            final int result = expr.evaluate(x, 0, 0);
            System.out.println(x + "\t" + result);
        } catch (RuntimeException e) {
            System.out.println(x + "\t" + e.getMessage());
        }
    }

    public static void main(String[] args) throws ParserErrors {
        //System.out.println(p.parse("²²(x)²").toMiniString());
        //System.out.println(p.parse("2147483648"));
        //System.out.println(p.parse("5 gcd5"));
        //System.out.println(p.parse("0 gcd-1"));
        //System.out.println(p.parse("([{1 + 2} * 3] + 5)"));
//        System.out.println(p.parse("([{1 + 2} * 3] + 5)").evaluate(1, 2 ,4));
//        //System.out.println(p.parse("[1 + 2)"));
//        //System.out.println(p.parse("1 + 2]"));
//        //System.out.println(p.parse("("));
//        System.out.println(p.parse("[[x + x] + {x + x}]"));
////        System.out.println(p.parse("5 ◣ 3").evaluate(1,2,4));
////        System.out.println(p.parse("4 ▯ 3").evaluate(1,2,4));
//      System.out.println(p.parse("[({0 area 4})]"));
        System.out.println(p.parse("(1 + 1) + 1)"));
    }
}