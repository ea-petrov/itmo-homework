package expression;

import expression.exceptions.OverflowException;

public class GCD extends AbstractBinaryOperation {
    public GCD(AllExpressions a, AllExpressions b) {
        super(a, b, "gcd", 0, 1);
    }

    private int absolute(int res) {
        if (res > 0) {
            return res;
        } else {
            return -res;
        }
    }

    @Override
    protected int doOperation(int x, int y) {
        while (y != 0) {
            int temp = y;
            y = x % y;
            x = temp;
        }
        return absolute(x);
    }

    @Override
    protected long doOperation(long x, long y) {
        throw new OverflowException("it is very bad idea");
    }
}

