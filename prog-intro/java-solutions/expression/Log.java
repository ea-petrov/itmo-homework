package expression;

public class Log extends AbstractBinaryOperation {
    public Log(final AllExpressions a, final AllExpressions b) {
        super(a, b, "//", 3, 0);
    }

    @Override
    protected int doOperation(final int x, final int y) {
        if (x == 0 && y >= 1) {
            return Integer.MIN_VALUE;
        }
        if ((x == 1 && y == 1) || (y <= 0) || (x <= 0)) {
            return 0;
        }
        if (y == 1) {
            return Integer.MAX_VALUE;
        }
        return (int) (Math.log(x) / Math.log(y));
    }

    @Override
    protected long doOperation(long value, long base) {
        return 0;
    }

}
