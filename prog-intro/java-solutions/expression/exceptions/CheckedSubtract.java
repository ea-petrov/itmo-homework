package expression.exceptions;

import expression.AllExpressions;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(final AllExpressions a, final AllExpressions b) {
        super(a, b);
    }

    @Override
    protected int doOperation(int x, int y) {
        if ((y > 0 && x < Integer.MIN_VALUE + y) || (y < 0 && x > Integer.MAX_VALUE + y)) {
            throw new OverflowException("overflow");
        }
        return x - y;
    }
}
