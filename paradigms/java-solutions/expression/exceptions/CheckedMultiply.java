package expression.exceptions;

import expression.AllExpressions;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(final AllExpressions a, final AllExpressions b) {
        super(a, b);
    }

    public static int checkedDoOperation(int x, int y) {
        if ((x == Integer.MIN_VALUE && y == -1) || (x == -1 && y == Integer.MIN_VALUE) || (y != 0 && (x * y) / y != x)) {
            throw new OverflowException("overflow in multi");
        }
        return x * y;
    }

    @Override
    protected int doOperation(int x, int y) {
        return (checkedDoOperation(x,y));
    }
}
