package expression.exceptions;

import expression.AllExpressions;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(final AllExpressions a, final AllExpressions b) {
        super(a, b);
    }

    public static int checkedDoOperation(int x, int y) {
        if (y == 0) {
            throw new DivideByZeroError("division by zero");
        } else if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("overflow in divide");
        }
        return x / y;
    }

    @Override
    protected int doOperation(int x, int y) {
        return checkedDoOperation(x, y);
    }
}
