package expression.exceptions;

import expression.AllExpressions;
import expression.Square;

public class CheckedSquare extends Square {
    public CheckedSquare(AllExpressions a) {
        super(a);
    }

    protected int doOperation(int x) {
        if (x >= 46341 || x <= -46341) {
            throw new OverflowException("overflow");
        }
        return x * x;
    }
}
