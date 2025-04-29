package expression.exceptions;

import expression.AllExpressions;
import expression.Negative;

public class CheckedNegate extends Negative {
    public CheckedNegate(AllExpressions a) {
        super(a);
    }

    public static int checkedDoOperation(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in negate");
        }
        return -x;
    }

    @Override
    protected int doOperation(int x) {
        return checkedDoOperation(x);
    }
}