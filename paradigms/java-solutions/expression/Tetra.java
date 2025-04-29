package expression;

import expression.exceptions.OverflowException;

public class Tetra extends AbstractUnaryOperation {
    public Tetra(AllExpressions a) {
        super("²", 5, a);
    }

    @Override
    protected int doOperation(int x) {
        if (x >= 10 || x <= 0) {
            throw new OverflowException("overflow");
        }
        return tetraByTwo(x, x);
    }

    private int tetraByTwo(int x, int y) {
        if (y <= 0) {
            return 1;
        }
        int ans = 1;
        while (y > 0) {
            if ((y & 1) == 1) {
                ans *= x;
            }
            y >>= 1;
            x *= x;
        }
        return ans;
    }

    @Override
    protected long doOperation(long x) {
        throw new OverflowException("overflow");
    }

    protected boolean isItTetra() {
        return true;
    }
}
