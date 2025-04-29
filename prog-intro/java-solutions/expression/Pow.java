package expression;

public class Pow extends AbstractBinaryOperation {
    public Pow(final AllExpressions a, final AllExpressions b) {
        super(a, b, "**", 3, 0);
    }

    @Override
    protected int doOperation(int x, int y) {
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
    protected long doOperation(long x, long y) {
        return 0;
    }
}
