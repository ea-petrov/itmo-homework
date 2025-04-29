package expression.exceptions;

import expression.AllExpressions;
import expression.Negative;

public class CheckedNegate extends Negative {
    public CheckedNegate(AllExpressions a) {
        super(a);
    }

    @Override
    protected int doOperation(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return -x;
    }
}