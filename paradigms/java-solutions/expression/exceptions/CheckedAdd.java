package expression.exceptions;

import expression.Add;
import expression.AllExpressions;

public class CheckedAdd extends Add {

    public CheckedAdd(final AllExpressions a, final AllExpressions b) {
        super(a, b);
    }

    @Override
    protected int doOperation(int x, int y) {
        return checkedDoOperation(x, y);
    }

    public static int checkedDoOperation(int x, int y) {
        if ((x > 0 && y > Integer.MAX_VALUE - x) || (x < 0 && y < Integer.MIN_VALUE - x) ||
                (y > 0 && x > Integer.MAX_VALUE - y) || (y < 0 && x < Integer.MIN_VALUE - y)) {
            throw new OverflowException("overflow in add");
        }
        return x + y;
    }
}
