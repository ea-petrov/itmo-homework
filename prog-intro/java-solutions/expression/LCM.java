package expression;

import expression.exceptions.InvalidStructure;
import expression.exceptions.OverflowException;

public class LCM extends AbstractBinaryOperation {
    public LCM(AllExpressions a, AllExpressions b) {
        super(a, b, "lcm", 0, 2);
    }

    @Override
    protected int doOperation(int x, int y) {
        try {
            return checkedDoOperation(x, y);
        } catch (ArithmeticException e) {
            throw new InvalidStructure("overflow");

        }
    }

    private int checkedDoOperation(int x, int y) {
        if (x == y) {
            if (x < 0) {
                x = -x;
            }
            return x;
        }
        x = x / gcd(x, y);
        return checkedMulty(x, y);
    }

    private int checkedMulty(int x, int y) {
        if ((x == Integer.MIN_VALUE && y == -1) || (x == -1 && y == Integer.MIN_VALUE) || (y != 0 && (x * y) / y != x)) {
            throw new OverflowException("overflow");
        }
        return x * y;
    }

    private int absolute(int res) {
        if (res > 0) {
            return res;
        } else {
            return -res;
        }
    }

    private int gcd(int x, int y) {
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
